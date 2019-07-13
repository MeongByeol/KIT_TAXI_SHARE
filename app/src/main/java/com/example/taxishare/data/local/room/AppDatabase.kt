/*
 * Created by WonJongSeong on 2019-07-13
 */

package com.example.taxishare.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taxishare.app.Constant
import com.example.taxishare.data.local.room.converter.RoomTypeConverter
import com.example.taxishare.data.local.room.dao.LocationDAO
import com.example.taxishare.data.local.room.entity.LocationModel

@Database(entities = [LocationModel::class], version = 1)
@TypeConverters(value = [RoomTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, Constant.ROOM_DB_NAME
            )
                .build()
    }
}