package io.gari.sample.ui.core

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import io.gari.sample.ui.login.InputError

@BindingAdapter("inputError")
fun TextInputLayout.bindError(error: InputError?) {
    if (error == null) {
        isErrorEnabled = false
        setError(null)
    } else {
        isErrorEnabled = true
        setError(context.getString(error.errorMessageResId))
    }
}