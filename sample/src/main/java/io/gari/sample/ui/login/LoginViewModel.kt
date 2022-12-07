package io.gari.sample.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gari.sample.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val userId = MutableLiveData<String>()

    fun doLogin() {
        val userId = userId.value

        if (userId.isNullOrEmpty()) {
            // todo: validation error
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.getWeb3AuthToken(userId)
                .onSuccess {

                }.onFailure {

                }
        }
    }
}