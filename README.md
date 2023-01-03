# Gari Wallet Android SDK

Provides solution for easy implementation custodial Gari Wallet in your products. Web3Auth is used as a layer for saving wallet credentials. Check Web3Auth documentation to get more information (https://web3auth.io/docs/)


## 💡 Features
- create wallet
- get wallet state (activated, not registered yet)
- requesting airdrop
- transfer Gari token

## ⏪ Requirements

- Android API version 24 or newer is required.

## ⚡ Installation

### Connect Gari via Gradle

Open your app module gradle.build file, and add gari dependency:

```groovy
dependencies {
   ...
   implementation 'io.github.gari-network:gari-wallet:0.2'
   ...
}
```

### Permissions

Open your app's `AndroidManifest.xml` file and add the following permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Manifest placeholder

Open your gradle.build file for your main application module - override manifest variable **$authRedirect**.
This url will be used as deeplink scheme for Web3Auth redirects.

You should add it to whitelist during configuring Web3Auth dashboard.
Read documentation to get more information - https://web3auth.io/docs/sdk/android/#configure-a-plug-n-play-project

All Logic for handling deeplink redirects has been already handled by Gari Wallet SDK, you just need to declare manifest variable in your gradle scripts.

```groovy
android {
    namespace 'io.gari.sample'
    compileSdk appConfig.compileSdk

    defaultConfig {
        ...
        manifestPlaceholders = [authRedirect: "io.coin.gari"]
        ...
    }
```

## 💥 Initialization

- Extend your Application class from GariApp

```kotlin
class DemoApplication : GariApp() {

    override fun onCreate() {
        super.onCreate()

        ...
    }
}
```

Set web3auth config:
- **web3AuthClientId:** client id from web3auth dashboard
- **redirectUrl:** consist of 2 parts (package name + path, e.g. - *io.coin.gari://auth*), it should be added into white-list in web3auth dashboard, and scheme of the url should be added as **manifestPlaceholder** (check [here] (#manifest-placeholder))
- **clientId:** - your personal gari client id
- **verifierIdField:** - field key which identify user in your JWT token
- **verifier:** - verifier name which is registered in web3auth dashboard
- **verifierTitle:** - title for web3auth webpage
- **verifierDomain:** - domain where your API service deployed for token verification
- **network:** - type of network for web3auth configuration (check documentation of web3auth https://web3auth.io/docs/sdk/android/initialize#web3authoptions)

```kotlin
class DemoApplication : GariApp() {

    override fun onCreate() {
        super.onCreate()

        val web3AuthConfig = Web3AuthConfig(
            web3AuthClientId = "",
            redirectUrl = "io.coin.gari://auth",
            verifierIdField = "uid",
            verifier = "your-verifier-name",
            verifierTitle = "Gari Wallet Demo",
            verifierDomain = "https://demo-gari-sdk.vercel.app",
            network = Web3Network.TESTNET
        )

        Gari.initialize(
            clientId = "d8817deb-dceb-40a4-a890-21f0286c8fba",
            web3AuthConfig = web3AuthConfig
        )
    }
}
``` 



- Start using Gari..

## 💥 Usage

Gari wallet SDK is completely friendly with coroutines API. It should be easy to implement into every project with MVVM architecture.

### Get Wallet State

Here is example how you can retrieve wallet state by user's token:

```kotlin
class WalletDetailsViewModel : ViewModel() {

    val walletState = MutableLiveData<GariWalletState>()

    init {
        loadWalletDetails()
    }

    private fun loadWalletDetails() {
        viewModelScope.launch {
            val state = Gari.getWalletState(web3AuthToken)
            walletState.postValue(state)
        }
    }
}
```

Design of GariWalletState class allow to properly handle every situation on your UI:

```kotlin
sealed class GariWalletState {

    class Activated(val pubKey: String, val balance: String) : GariWalletState()

    object NotExist : GariWalletState()

    class Error(val error: Throwable?) : GariWalletState()
}
```

### Create Wallet

##### *Important:
Token of the user should refreshed before calling function Gari.createWallet(). This limitation is coming from Web3Auth SDK.

What is WalletKeyManager and where you can get it - check "Key Management" documentation below on the page

```kotlin
class WalletDetailsViewModel : ViewModel() {

    fun registerWallet(keyManager: WalletKeyManager, userJwtToken : String) {
        viewModelScope.launch {
            val walletResult = Gari.createWallet(
                keyManager = keyManager,
                token = userJwtToken
            )
        }
    }
}
```

### Transfer Gari Token

##### *Important:
Token of the user should refreshed before calling function Gari.transferGariToken(). This limitation is coming from Web3Auth SDK.

Every transaction consist of 2 steps:
1. Create signed transaction through Gari Wallet Sdk
2. Send transaction into your backend for future processing

For getting signed transaction you can follow this example:
```kotlin
class WalletDetailsViewModel : ViewModel() {

    fun sendTransaction(walletKeyManager: WalletKeyManager) {
        val receiverPublicKey = ""
        val transactionAmount = "1" // 1 GARI
        val user userJwtToken = ""

        val amount = transactionAmount.toBigDecimalOrNull()
            ?.toLamports()
            ?.toString()
            ?: return

        viewModelScope.launch {
            Gari.transferGariToken(
                token = userJwtToken,
                keyManager = walletKeyManager,
                receiverPublicKey = receiverPublicKey,
                transactionAmount = amount
            ).onSuccess { signedTransaction ->
                /* send transaction into your backend for future processing */
            }.onFailure {
                /* handle error on UI */
            }
        }
    }
}
```

### Request airdrop

Airdrop operation requires wallet private key - from which Gari tokens will be transferred into user account.

```kotlin
class AirdropViewModel : ViewModel() {

    fun requestAirdrop() {
        val airdropAmount = "1" // 1 Gari
        val airdropSponsor = "" // private key of wallet from which amount will be transferred (encoded in HEX)
        val userJwtToken = ""
        val publicKey = "" // user who will be credited with Gari tokens

        viewModelScope.launch {
            Gari.getAirDrop(
                token = userJwtToken,
                amount = airdropAmount,
                destinationPublicKey = publicKey,
                sponsorPrivateKey = airdropSponsor.decodeHex()
            ).onSuccess { signature ->
                // signature - it's transaction signature
            }.onFailure {
                // handle error on UI
            }
        }
    }
}
```


## 🌟 Key Management

For operations which are required wallet private key (like transactions), you have instantiate WalletKeyManager, which is part of Gari Wallet SDK

```kotlin
object Gari {

    ...

    fun provideWalletKeyManager(resultCaller: ActivityResultCaller): WalletKeyManager {
        return WalletKeyManager(resultCaller)
    }

    ...
}
```

This action depends on Activity Result APIs - so you have to create it before .onCreate() lifecycle function of your Activity or Fragment

```kotlin
class WalletDetailsActivity : AppCompatActivity() {

    private val walletKeyManager = Gari.provideWalletKeyManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ...
    }
}

```