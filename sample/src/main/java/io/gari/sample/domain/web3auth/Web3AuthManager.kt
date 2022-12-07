package io.gari.sample.domain.web3auth

import android.content.Context
import android.content.Intent

interface Web3AuthManager {

    fun onCreate(context: Context, intent: Intent?)

    fun onNewIntent(intent: Intent?)

    fun login()
}