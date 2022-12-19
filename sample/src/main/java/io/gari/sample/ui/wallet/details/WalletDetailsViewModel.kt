package io.gari.sample.ui.wallet.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.domain.wallet.WalletKeyManager
import io.gari.sample.R
import io.gari.sample.ui.login.InputError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletDetailsViewModel(
    private val web3AuthToken: String
) : ViewModel() {

    val walletState = MutableLiveData<GariWalletState>()

    val airdropAmount = MutableLiveData<String>()
    val airdropAmountError = MutableLiveData<InputError?>()

    init {
        loadWalletDetails()
    }

    private fun loadWalletDetails() {
        viewModelScope.launch(Dispatchers.Default) {
            val state = Gari.getWalletState(web3AuthToken)
            walletState.postValue(state)
        }
    }

    fun registerWallet(keyManager: WalletKeyManager) {
        viewModelScope.launch(Dispatchers.Default) {
            Gari.createWallet(
                keyManager = keyManager,
                token = web3AuthToken
            ).whenComplete { walletResult, error ->
                val wallet = walletResult.getOrNull()

                if (walletResult.isFailure
                    || wallet == null
                    || error != null
                ) {
                    walletState.postValue(
                        GariWalletState.Error(
                            walletResult.exceptionOrNull() ?: error
                        )
                    )
                } else {
                    walletState.postValue(
                        GariWalletState.Activated(
                            wallet.publicKey,
                            wallet.balance
                        )
                    )
                }
            }
        }
    }

    fun requestAirdrop(keyManager: WalletKeyManager) {
        val airdropAmount = airdropAmount.value

        if (airdropAmount.isNullOrEmpty()) {
            airdropAmountError.value =
                InputError.EmptyField(R.string.wallet_status_airdrop_amount_empty)
            return
        }

        airdropAmountError.value = null

        viewModelScope.launch(Dispatchers.Default) {
            Gari.getAirDrop(
                keyManager = keyManager,
                token = web3AuthToken,
                amount = airdropAmount
            ).whenComplete { walletResult, error ->
            }
        }
    }
}