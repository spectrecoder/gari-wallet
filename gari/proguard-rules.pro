-keep class io.coin.gari.ui.app.GariApp { *; }
-keep class io.coin.gari.domain.web3.Web3AuthConfig { *; }
-keep class io.coin.gari.domain.web3.Web3Network { *; }
-keep class io.coin.gari.domain.Gari {
    public <methods>;
    public <fields>;
}

-keep class io.coin.gari.utils.StringExtKt {
  *;
}
-keep class io.coin.gari.utils.BigDecimalExtKt {
  *;
}

-keep class io.coin.gari.domain.entity.GariWalletState { *; }
-keep class io.coin.gari.domain.entity.GariWalletState$* { *; }
-keep class io.coin.gari.domain.entity.GariWallet { *; }

-keep class io.coin.gari.network.entity.ApiGariWallet { *; }
-keep class io.coin.gari.network.response.GariResponse { *; }
-keep class io.coin.gari.network.response.WalletDetailsResponse { *; }

-keepclassmembers class io.coin.gari.network.entity.** { *; }
-keepclassmembers class io.coin.gari.network.response.** { *; }

-keepnames class io.coin.gari.domain.wallet.WalletKeyManager

-keep class com.portto.solana.web3.** { *; }
-dontwarn com.portto.solana.web3.**