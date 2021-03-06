package com.meongbyeol.taxishare.view.main.taxisharelist

import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meongbyeol.taxishare.R
import com.meongbyeol.taxishare.app.AlarmManagerImpl
import com.meongbyeol.taxishare.app.Constant
import com.meongbyeol.taxishare.data.model.Location
import com.meongbyeol.taxishare.data.model.TaxiShareInfo
import com.meongbyeol.taxishare.data.remote.apis.server.ServerClient
import com.meongbyeol.taxishare.data.repo.ServerRepositoryImpl
import com.meongbyeol.taxishare.extension.observeBottomDetectionPublisher
import com.meongbyeol.taxishare.extension.setOnBottomDetection
import com.meongbyeol.taxishare.view.main.register.RegisterTaxiShareActivity
import com.meongbyeol.taxishare.view.main.taxisharelist.detail.TaxiShareInfoDetailActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_taxi_share_list.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivityForResult
import org.jetbrains.anko.support.v4.toast
import java.util.*


class TaxiShareListFragment : Fragment(), TaxiShareListView {

    companion object {
        fun newInstance() =
            TaxiShareListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private lateinit var alertDialog: AlertDialog

    private lateinit var presenter: TaxiShareListPresenter
    private lateinit var taxiShareListAdapter: TaxiShareListAdapter
    private lateinit var subscribe: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        initPresenter()
        initAdapter()

        alertDialog = with(AlertDialog.Builder(context)) {
            setCancelable(false)
            setView(R.layout.loading_dialog_layout)
            setMessage(R.string.tedpermission_setting)
        }.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_taxi_share_list, container, false)

    override fun setTaxiShareList(taxiShareList: MutableList<TaxiShareInfo>, isRefresh: Boolean) {
        taxiShareListAdapter.setTaxiShareInfoList(taxiShareList, isRefresh)
//        if (isRefresh) {
//            rcv_taxi_list.scheduleLayoutAnimation()
//        }
    }


    override fun showLoadTaxiShareListNotFinishedMessage() {
        toast(getString(R.string.taxi_share_list_load_not_finish))
    }

    override fun showLoadTaxiShareListFailMessage() {
        toast(getString(R.string.taxi_share_list_load_fail))
    }

    override fun showLastPageOfTaxiShareListMessage() {
        toast(getString(R.string.taxi_share_list_last_page))
    }

    override fun showParticipateTaxiShareSuccess(postId: String) {
        taxiShareListAdapter.changeTaxiShareParticipateInfo(postId, true)
        toast(getString(R.string.participate_taxi_share_success))
    }

    override fun showParticipateTaxiShareFail() {
        toast(getString(R.string.participate_taxi_share_fail))
    }

    override fun showParticipateTaxiShareNotFinish() {
        toast(getString(R.string.participate_taxi_share_waiting))
    }

    override fun showRemoveTaxiShareSuccess(postId: Int) {
        taxiShareListAdapter.removeTaxiShare(postId.toString())
        toast(getString(R.string.remove_taxi_share_success))
    }

    override fun showRemoveTaxiShareFail() {
        toast(getString(R.string.remove_taxi_share_fail))
    }

    override fun showRemoveTaxiShareNotFinish() {
        toast(getString(R.string.remove_taxi_share_waiting))
    }

    override fun showLeaveTaxiShareSuccess(postId: Int) {
        taxiShareListAdapter.changeTaxiShareParticipateInfo(postId.toString(), false)
        toast(getString(R.string.leave_taxi_share_success))
    }

    override fun showLeaveTaxiShareFail() {
        toast(getString(R.string.leave_taxi_share_fail))
    }

    override fun showLeaveTaxiShareNotFinish() {
        toast(getString(R.string.leave_taxi_share_waiting))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()

        setDialogMessage(R.string.loading_taxi_list)
        presenter.loadTaxiShareInfoList(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (resultCode == Constant.DATA_REMOVED) {
                taxiShareListAdapter.removeTaxiShare(data.getStringExtra(Constant.POST_ID))
            } else if (requestCode == Constant.MODIFY_TAXI_SHARE) {
                taxiShareListAdapter.updateTaxiShareInfo(
                    data.getSerializableExtra(Constant.MODIFY_TAXI_SHARE_STR) as TaxiShareInfo
                )
            } else if (requestCode == Constant.TAXISHARE_DETAIL) {
                taxiShareListAdapter.updateTaxiShareInfo(
                    data.getSerializableExtra(Constant.TAXISHARE_DETAIL_STR) as TaxiShareInfo
                )
            }
        }
    }

    override fun showLoadingDialog() {
        if (pb_taxi_list.visibility == View.GONE)
            pb_taxi_list.visibility = View.VISIBLE
    }

    override fun dismissLoadingDialog() {
        if (pb_taxi_list.visibility == View.VISIBLE)
            pb_taxi_list.visibility = View.GONE
    }

    override fun showMessageDialog() {
        if (!alertDialog.isShowing)
            alertDialog.show()
    }

    override fun dismissMessageDialog() {
        if (alertDialog.isShowing)
            alertDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        subscribe.dispose()
    }

    override fun setBackgroundGray() {
        rcv_taxi_list.backgroundResource = R.color.temp_gray
    }

    override fun setBackgroundWhite() {
        rcv_taxi_list.backgroundResource = R.color.common_white
    }

    override fun dismissRefresh() {
        srl_main.isRefreshing = false
    }

    fun addTaxiShareInfo(taxiShareInfo: TaxiShareInfo) {
        taxiShareListAdapter.addTaxiShareInfo(taxiShareInfo, isVisible)
    }

    fun setStartLocation(location: Location) {
        presenter.setStartLocation(location)
        //presenter.filterTaxiShareList(taxiShareListAdapter.getTaxiShareList(), true)
    }

    fun setEndLocation(location: Location) {
        presenter.setEndLocation(location)
        //presenter.filterTaxiShareList(taxiShareListAdapter.getTaxiShareList(), true)
    }

    fun setStartTime(startDate: Date) {
        presenter.setStartTime(startDate)
        //presenter.filterTaxiShareList(taxiShareListAdapter.getTaxiShareList(), true)
    }


    private fun initPresenter() {
        presenter = TaxiShareListPresenter(
            this, ServerRepositoryImpl.getInstance(ServerClient.getInstance()),
            AlarmManagerImpl(
                context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                context!!
            )
        )
    }

    private fun initAdapter() {
        taxiShareListAdapter = TaxiShareListAdapter()
    }

    private fun initView() {

        with(rcv_taxi_list) {
            adapter = taxiShareListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            //layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        }
    }

    private fun initListener() {

        nsc_taxi_list.setOnBottomDetection().apply {
            subscribe = nsc_taxi_list.observeBottomDetectionPublisher().subscribe {
                setDialogMessage(R.string.loading_taxi_list)
                presenter.loadTaxiShareInfoList(false)
            }
        }

        srl_main.onRefresh {
            presenter.loadTaxiShareInfoList(true)
        }

        taxiShareListAdapter.setTaxiShareInfoItemClickListener(object :
            TaxiShareInfoItemClickListener {
            override fun onTaxiShareInfoItemClicked(selectedTaxiShareInfo: TaxiShareInfo) {
                this@TaxiShareListFragment.startActivityForResult<TaxiShareInfoDetailActivity>(
                    Constant.TAXISHARE_DETAIL,
                    Constant.TAXISHARE_DETAIL_STR to selectedTaxiShareInfo
                )
            }
        })
        taxiShareListAdapter.setTaxiShareInfoModifyClickListener(object :
            TaxiShareInfoModifyClickListener {
            override fun onTaxiShareInfoModifyClicked(
                selectedTaxiShareInfo: TaxiShareInfo,
                pos: Int
            ) {
                this@TaxiShareListFragment.startActivityForResult<RegisterTaxiShareActivity>(
                    Constant.MODIFY_TAXI_SHARE,
                    Constant.MODIFY_TAXI_SHARE_STR to selectedTaxiShareInfo
                )
            }
        })
        taxiShareListAdapter.setTaxiShareInfoRemoveClickListener(object :
            TaxiShareInfoRemoveClickListener {
            override fun onTaxiShareInfoRemoveClicked(postId: String) {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.taxi_share_remove_title))
                    .setMessage(getString(R.string.taxi_share_remove_content))
                    .setPositiveButton(getString(R.string.ok), ({ _, _ ->
                        setDialogMessage(R.string.remove_taxi_share_waiting)
                        presenter.removeTaxiShareInfo(postId)
                    }))
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setCancelable(false)
                    .show()
            }
        })
        taxiShareListAdapter.setTaxiShareParticipantsClickListener(object :
            TaxiShareParticipantBtnClickListener {
            override fun onParticipantsButtonClicked(
                postId: String,
                isParticipating: Boolean, startLocation: String, endLocation: String
            ) {
                if (isParticipating) {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.taxi_share_leave_title))
                        .setMessage(getString(R.string.taxi_share_leave_content))
                        .setPositiveButton(getString(R.string.ok), ({ _, _ ->
                            setDialogMessage(R.string.leave_taxi_share_waiting)
                            presenter.leaveTaxiShare(postId)
                        }))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setCancelable(false)
                        .show()
                } else {
                    presenter.participateTaxiShare(postId, Date(), startLocation, endLocation)
                }
            }
        })
    }

    fun reloadTaxiShareList() {
        presenter.loadTaxiShareInfoList(true)
    }

    private fun setDialogMessage(@StringRes id: Int) {
        alertDialog.setMessage(getString(id))
    }

    fun resetFilteringSetting() {
        presenter.resetFilteringSetting()
        presenter.loadTaxiShareInfoList(true)
    }
}
