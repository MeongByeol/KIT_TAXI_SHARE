/*
 * Created by WonJongSeong on 2019-07-13
 */

package com.example.taxishare.data.mapper


import com.example.taxishare.app.Constant
import com.example.taxishare.data.local.room.entity.LocationModel
import com.example.taxishare.data.local.room.entity.MyLocationModel
import com.example.taxishare.data.model.Location
import com.example.taxishare.data.model.MyLocation
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

    fun dateToString(date : Date) : String = DATE_FORMAT.format(date)
}