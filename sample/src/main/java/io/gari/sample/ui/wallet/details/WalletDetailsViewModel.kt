package io.gari.sample.ui.wallet.details

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletDetailsViewModel(
    private val web3AuthToken: String
) : ViewModel() {

    fun loadWalletDetails(context: Context, intent : Intent) {
        viewModelScope.launch(Dispatchers.Main) {
            Gari.getWalletDetails(context, intent, web3AuthToken)
        }
    }
}