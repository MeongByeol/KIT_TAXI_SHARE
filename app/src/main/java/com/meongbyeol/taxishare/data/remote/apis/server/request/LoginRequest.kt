/*
 * Created by WonJongSeong on 2019-04-11
 */

package com.meongbyeol.taxishare.data.remote.apis.server.request

class LoginRequest(private val id : String, private val pw : String) :
    ServerRequest.PostRequest {

    companion object {
        private const val ID : String = "ID"
        private const val PW : String = "PW"
    }

    override fun getRequest(): Map<String, String> {
        val request : MutableMap<String, String> = HashMap()
        request[ID] = id
        request[PW] = pw
        return request
    }
}