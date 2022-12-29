package io.gari.sample.ui.login

import androidx.annotation.StringRes

sealed class InputError {

    abstract val errorMessageResId: Int

    class EmptyField(
        @StringRes
        override val errorMessageResId: Int
    ) : InputError()
}