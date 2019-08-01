/*
 * Created by WonJongSeong on 2019-07-30
 */

package com.example.taxishare.data.remote.apis.server

import com.example.taxishare.data.model.TaxiShareInfoModel
import com.example.taxishare.data.remote.apis.server.response.ParticipateTaxiShareResponse
import com.example.taxishare.data.remote.apis.server.response.TaxiShareModifyResponse
import com.example.taxishare.data.remote.apis.server.response.TaxiShareRegisterResponse
import com.example.taxishare.data.remote.apis.server.response.TaxiShareRemoveResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TaxiShareInfoAPI {
    @POST("registerTaxiShareInfo")
    fun registerTaxiShareInfo(@Body serverRequest: Map<String, String>): Observable<TaxiShareRegisterResponse>

    @GET("loadTaxiShareInfo")
    fun getTaxiShareInfo(@Query("nextPageNum") nextPageNum: Int, @Query("uid") uid : Int): Observable<MutableList<TaxiShareInfoModel>>

    @POST("participateTaxiShare")
    fun participateTaxiShare(@Body serverRequest: Map<String, String>) : Observable<ParticipateTaxiShareResponse>

    @POST("removeTaxiShare")
    fun removeTaxiShare(@Body serverRequest: Map<String, String>) : Observable<TaxiShareRemoveResponse>

    @POST("updateTaxiShare")
    fun updateTaxiShare(@Body serverRequest: Map<String, String>) : Observable<TaxiShareModifyResponse>
}