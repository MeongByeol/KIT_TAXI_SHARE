/*
 * Created by WonJongSeong on 2019-07-13
 */

package com.example.taxishare.data.repo

import com.example.taxishare.data.model.Location
import com.example.taxishare.data.model.ServerResponse
import com.example.taxishare.data.remote.apis.server.ServerClient
import com.example.taxishare.data.remote.apis.server.request.SearchPlacesRequest
import com.example.taxishare.data.remote.apis.server.request.ServerRequest
import com.example.taxishare.data.remote.apis.server.request.SignUpRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ServerRepositoryImpl(private val serverClient: ServerClient) : ServerRepository {

    companion object {
        @Volatile
        private var INSTANCE: ServerRepository? = null

        fun getInstance(serverClient: ServerClient): ServerRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServerRepositoryImpl(serverClient)
            }
    }

    override fun loginRequest(loginRequest : ServerRequest.PostRequest) : Observable<ServerResponse> =
        serverClient.loginRequest(loginRequest)
            .map { ServerResponse.fromServerResponseCode(it.responseCode) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun isSameIdExist(serverRequest: ServerRequest.PostRequest): Observable<ServerResponse> =
        serverClient.isSameIdExist(serverRequest)
            .map { ServerResponse.fromServerResponseCode(it.responseCode) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun isSameNicknameExist(serverRequest: ServerRequest.PostRequest) : Observable<ServerResponse> =
       serverClient.isSameNicknameExist(serverRequest)
            .map { ServerResponse.fromServerResponseCode(it.responseCode)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun signUpRequest(signUpRequest: SignUpRequest) : Observable<ServerResponse> =
        serverClient.signUpRequest(signUpRequest)
            .map { ServerResponse.fromServerResponseCode(it.responseCode) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getSearchPlacesInfo(searchPlacesRequest: SearchPlacesRequest) : Observable<MutableList<Location>> =
        serverClient.getSearchPlacesInfo(searchPlacesRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}