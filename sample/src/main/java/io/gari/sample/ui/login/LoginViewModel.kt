package io.gari.sample.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gari.sample.R
import io.gari.sample.data.DemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: DemoRepository
) : ViewModel() {

    val userId = MutableLiveData<String>("112233")
    val userIdError = MutableLiveData<InputError?>()

    val action = MutableLiveData<LoginAction?>()
    val isProcessing = MutableLiveData(false)

    fun doLogin() {
        if (isProcessing.value == true) {
            return
        }

        val userId = userId.value

        if (userId.isNullOrEmpty()) {
            userIdError.value = InputError.EmptyField(R.string.login_error_empty_user_id)
            return
        }

        userIdError.value = null
        isProcessing.value = true

        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.getWeb3AuthToken(userId)
                .onSuccess { token ->
                    action.postValue(LoginAction.Web3AuthTokenReady(token))
                    isProcessing.postValue(false)
                }.onFailure {
                    isProcessing.postValue(false)
                }
        }
    }
}