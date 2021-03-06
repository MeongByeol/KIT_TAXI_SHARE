/*
 * Created by WonJongSeong on 2019-07-13
 */

package com.meongbyeol.taxishare.data.mapper


import com.meongbyeol.taxishare.app.Constant
import com.meongbyeol.taxishare.data.local.room.entity.LocationModel
import com.meongbyeol.taxishare.data.local.room.entity.MyLocationModel
import com.meongbyeol.taxishare.data.model.*
import com.meongbyeol.taxishare.data.remote.apis.server.response.DetailTaxiShareLoadResponse
import com.meongbyeol.taxishare.data.remote.apis.server.response.RegisterCommentResponse
import java.text.SimpleDateFormat
import java.util.*

object TypeMapper {

    val DATE_FORMAT = SimpleDateFormat(Constant.DATE_FORMAT, Locale.KOREA)

    fun locationModelToLocation(locationModelList: List<LocationModel>): MutableList<Location> {
        val convertedList: MutableList<Location> = mutableListOf()

        locationModelList.forEach {
            convertedList.add(
                Location(
                    it.latitude,
                    it.longitude,
                    it.locationName,
                    it.roadAddress,
                    it.jibunAddress
                )
            )
        }

        return convertedList
    }

    fun myLocationModelToMyLocation(myLocationList: MutableList<MyLocationModel>): MutableList<MyLocation> {
        val convertedList: MutableList<MyLocation> = mutableListOf()

        myLocationList.forEach {
            convertedList.add(
                MyLocation(
                    it.saveName,
                    it.latitude,
                    it.longitude,
                    it.locationName,
                    it.roadAddress,
                    it.jibunAddress
                )
            )
        }

        return convertedList
    }

    fun dateToString(date: Date): String = DATE_FORMAT.format(date)

    fun taxiShareInfoModelToData(myTaxiShareInfoModelList: MutableList<TaxiShareInfoModel>): MutableList<TaxiShareInfo> {
        val convertedList: MutableList<TaxiShareInfo> = mutableListOf()

        myTaxiShareInfoModelList.forEach {
            with(it) {
                convertedList.add(
                    TaxiShareInfo(
                        id,
                        uid,
                        title,
                        Date(startDate),
                        startLocation,
                        endLocation,
                        limit,
                        nickname,
                        major,
                        participantsNum,
                        (isParticipate == 1)
                    )
                )
            }
        }

        return convertedList
    }

    fun commentModelToComment(commentList: MutableList<CommentModel>): MutableList<Comment> {
        val convertedList: MutableList<Comment> = mutableListOf()

        commentList.forEach {
            with(it) {
                convertedList.add(
                    Comment(
                        uid,
                        commentId,
                        dateToString(Date(commentDate)),
                        content,
                        nickname
                    )
                )
            }
        }

        return convertedList
    }

    fun registerCommentResponseToComment(response: RegisterCommentResponse): Comment {
        with(response) {
            return Comment(uid, commentId, dateToString(Date(commentDate)), content, nickname)
        }
    }

    fun detailInfoResponseToDetailInfo(response: DetailTaxiShareLoadResponse): TaxiShareDetailInfo {
        with(response) {
            return TaxiShareDetailInfo(
                id, uid, title, Date(startDate),
                startLocation, endLocation, limit, nickname, major, participants.size, isParticipate == 1,
                participants
            )
        }
    }
}