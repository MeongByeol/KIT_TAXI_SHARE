/*
 * Created by WonJongSeong on 2019-08-02
 */

package com.meongbyeol.taxishare.data.remote.apis.server.request

import com.meongbyeol.taxishare.app.Constant
import java.util.*

data class LeaveTaxiShareRequest (
    private val postId : String
) : ServerRequest.PostRequest {

    companion object {
        private const val UID  = "uid"
        private const val POST_ID = "postId"
    }

    override fun getRequest(): Map<String, String> {
        val params: MutableMap<String, String> = HashMap()

        params[UID] = Constant.CURRENT_USER.studentId.toString()
        params[POST_ID] = postId

        return params
    }
}