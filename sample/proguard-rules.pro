-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable

-keep class io.gari.sample.di.KoinLoader { *; }

-keep class com.portto.solana.web3.** { *; }
-dontwarn com.portto.solana.web3.**
-dontwarn kotlin.Unit

