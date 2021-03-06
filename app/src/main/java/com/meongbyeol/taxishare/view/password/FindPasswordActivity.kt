package com.meongbyeol.taxishare.view.password

import android.os.Bundle
import com.meongbyeol.taxishare.R
import com.meongbyeol.taxishare.customview.LoadingDialog
import com.meongbyeol.taxishare.data.remote.apis.server.ServerClient
import com.meongbyeol.taxishare.data.repo.ServerRepositoryImpl
import com.meongbyeol.taxishare.view.BaseActivity
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.activity_find_password.*
import org.jetbrains.anko.toast

class FindPasswordActivity : BaseActivity(), FindPasswordView {


    private lateinit var presenter : FindPasswordPresenter

    private lateinit var alertDialog: LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertDialog = LoadingDialog.newInstance(R.string.temporary_pw_send_message)

        initPresenter()
        initListener()

    }

    override fun getLayoutId(): Int = R.layout.activity_find_password

    override fun sendTemporaryPasswordSuccess() {
        toast(R.string.temporary_pw_send_success)
        finish()
    }

    override fun sendTemporaryPasswordFail() {
        toast(R.string.temporary_pw_send_fail)
    }

    override fun changeLoginButtonState(isValidate: Boolean) {
        btn_find_pw_send.isEnabled = isValidate
    }

    override fun showLoginLoadingDialog() {
        if(!alertDialog.isVisible)
            alertDialog.show(supportFragmentManager, "Test")
    }

    override fun dismissLoginLoadingDialog() {
        if(alertDialog.isVisible)
            alertDialog.dismiss()
    }

    override fun changeIdEditTextState(isMatched: Boolean) {
        text_input_layout_find_pw.error =
            if (isMatched) null
            else resources.getString(R.string.sign_up_student_id_error)
    }

    private fun initPresenter() {
        presenter = FindPasswordPresenter(ServerRepositoryImpl.getInstance(ServerClient.getInstance()), this)
    }

    @SuppressWarnings("all")
    private fun initListener() {
        text_input_find_pw.textChanges().skipInitialValue().subscribe({
            presenter.checkIdValidation(it.toString())
        }, {
            it.stackTrace[0]
        })

        btn_find_pw_send.setOnClickListener {
            presenter.findPassword(text_input_find_pw.text.toString())
        }

        tb_find_pw.setNavigationOnClickListener {
            finish()
        }
    }
}
