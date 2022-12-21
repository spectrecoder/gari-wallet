package io.gari.sample.ui.wallet.airdrop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import io.coin.gari.utils.decodeHex
import io.gari.sample.R
import io.gari.sample.ui.login.InputError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AirdropViewModel(
    private val web3AuthToken: String,
    private val publicKey: String
) : ViewModel() {

    val airdropSponsor =
        MutableLiveData<String>("03f6cac889d362c37589868ed67f068783fb433c7d0a0b907db24afd3a843e27d8c6f04cf5c6f33f2b83ba3e0e83e827bb305157105442c72a544c4e70c568b1")
    val airdropSponsorError = MutableLiveData<InputError?>()

    val airdropAmount = MutableLiveData<String>()
    val airdropAmountError = MutableLiveData<InputError?>()

    val isProcessing = MutableLiveData(false)

    fun requestAirdrop() {
        val airdropAmount = airdropAmount.value

        if (airdropAmount.isNullOrEmpty()) {
            airdropAmountError.value =
                InputError.EmptyField(R.string.wallet_status_airdrop_amount_empty)
            return
        }

        airdropAmountError.value = null

        val airdropSponsor = airdropSponsor.value
        if (airdropSponsor.isNullOrEmpty()) {
            airdropSponsorError.value =
                InputError.EmptyField(R.string.airdrop_sponsor_required)
            return
        }

        airdropSponsorError.value = null

        isProcessing.value = true
        viewModelScope.launch(Dispatchers.Default) {
            Gari.getAirDrop(
                token = web3AuthToken,
                amount = airdropAmount,
                destinationPublicKey = publicKey,
                sponsorPrivateKey = airdropSponsor.decodeHex()
            ).onSuccess {
                isProcessing.postValue(false)
            }.onFailure {
                isProcessing.postValue(false)
            }
        }
    }
}