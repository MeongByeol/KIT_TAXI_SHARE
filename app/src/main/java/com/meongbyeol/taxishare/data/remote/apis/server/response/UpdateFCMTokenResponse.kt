/*
 * Created by WonJongSeong on 2019-09-03
 */

package com.meongbyeol.taxishare.data.remote.apis.server.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateFCMTokenResponse(
    @Expose
    @SerializedName("responseCode")
    val responseCode: Int
) {
}