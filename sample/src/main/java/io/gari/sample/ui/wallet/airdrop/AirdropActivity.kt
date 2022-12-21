package io.gari.sample.ui.wallet.airdrop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.gari.sample.R
import io.gari.sample.databinding.ActivityRequestAirdropBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AirdropActivity : AppCompatActivity() {

    private val web3AuthToken: String
        get() = intent.getStringExtra(ARG_TOKEN)
            ?: throw IllegalStateException("Forget to pass web3 auth token?")

    private val publicKey: String
        get() = intent.getStringExtra(ARG_PUBLIC_KEY)
            ?: throw IllegalStateException("Forget to pass public key for airdrop?")

    private val viewModel: AirdropViewModel by viewModel { parametersOf(web3AuthToken, publicKey) }
    private lateinit var screenBinding: ActivityRequestAirdropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding = DataBindingUtil.setContentView<ActivityRequestAirdropBinding>(
            this, R.layout.activity_request_airdrop
        )
        screenBinding.clickListener = PageClickListener()
        screenBinding.lifecycleOwner = this
        screenBinding.viewModel = viewModel
    }

    private fun requestAirdrop() {
        viewModel.requestAirdrop()
    }

    private inner class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnRequestAirdrop -> requestAirdrop()
            }
        }
    }

    companion object {

        private const val ARG_TOKEN = "ARG_TOKEN"
        private const val ARG_PUBLIC_KEY = "ARG_PUBLIC_KEY"

        fun buildIntent(context: Context, token: String, publicKey: String): Intent {
            return Intent(context, AirdropActivity::class.java)
                .also {
                    it.putExtra(ARG_TOKEN, token)
                    it.putExtra(ARG_PUBLIC_KEY, publicKey)
                }
        }
    }
}