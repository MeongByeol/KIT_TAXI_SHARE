/*
 * Created by WonJongSeong on 2019-07-18
 */

package com.example.taxishare.view.main.taxisharelist

interface TaxiShareParticipantBtnClickListener {
        fun onParticipantsButtonClicked(postId : String, isParticipating : Boolean, startLocation : String, endLocation : String)
}