/*
 * Created by WonJongSeong on 2019-03-26
 */

package com.meongbyeol.taxishare.view.signup

interface SignUpView {

    fun changeSignUpButtonState(canActivate : Boolean)

    fun changeIdEditTextState(isMatched : Boolean)
    fun changePwEditTextState(isMatched: Boolean)
    fun changePwConfirmEditTextState(isMatched: Boolean)
    fun changeNicknameEditTextState(isMatched: Boolean)
    // 전공 선택 변경

    fun sameIdExist()
    fun sameIdNotExist()

    fun sameNicknameExist()
    fun sameNicknameNotExist()

    fun signUpSuccess()
    fun signUpFail()
    fun showCheckEmailMessage()

    fun showSignUpRequestLoadingDialog()
    fun dismissSignUpLoadingDialog()
}