package com.example.taxishare.view.login

import com.example.taxishare.data.remote.apis.server.ServerClient
import com.example.taxishare.data.remote.apis.server.request.LoginRequest
import com.example.taxishare.util.RegularExpressionChecker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class LoginPresenter(
    private val loginView: LoginView,
    private val serverClient: ServerClient,
    private val compositeDisposable: CompositeDisposable
) {

    private var isIdValidate: Boolean = false
    private var isPwValidate: Boolean = false

    private var preLoginRequestDisposable: Disposable? = null

    /* 로그인 요청 */
    fun login(id: String, pw: String) {
        // 이전에 로그인 요청이 있었으면 무시하고 다시 요청
        if (preLoginRequestDisposable != null && preLoginRequestDisposable!!.isDisposed) {
            preLoginRequestDisposable?.dispose()
        }

        preLoginRequestDisposable = serverClient.loginRequest(LoginRequest(id, pw))
            .subscribe({
                if (it.isLoginSuccess) {
                    loginView.loginSuccess()
                } else {
                    loginView.loginFail()
                }
            }, {
                it.stackTrace[0]
            })
    }

    /* 로그인이 가능한 상태인지 확인 */
    private fun changeLoginButtonState() {
        loginView.changeLoginButtonState(isIdValidate && isPwValidate)
    }

    fun checkIdValidation(id: String) {
        isIdValidate = RegularExpressionChecker.checkEmailValidation(id)
        loginView.changeIdEditTextState(isIdValidate)
        changeLoginButtonState()
    }

    fun checkPwValidation(pw: String) {
        isPwValidate = RegularExpressionChecker.checkPasswordValidation(pw)
        loginView.changePwEditTextState(isPwValidate)
        changeLoginButtonState()
    }
}