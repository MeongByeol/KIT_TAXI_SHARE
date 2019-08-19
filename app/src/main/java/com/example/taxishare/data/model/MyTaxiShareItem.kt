package com.example.taxishare.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyTaxiShareItem(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("startDate")
    val startDate: Long,
    @Expose
    @SerializedName("startLocation")
    val startLocation: Location,
    @Expose
    @SerializedName("partyNum")
    val partyNum: Int
) {
    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<MyTaxiShareItem> = object : DiffUtil.ItemCallback<MyTaxiShareItem>() {
            override fun areItemsTheSame(oldItem: MyTaxiShareItem, newItem: MyTaxiShareItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MyTaxiShareItem, newItem: MyTaxiShareItem): Boolean =
                oldItem.id == newItem.id
        }
    }
}