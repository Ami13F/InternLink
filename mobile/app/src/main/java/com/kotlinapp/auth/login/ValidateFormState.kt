package com.kotlinapp.auth.login


class ValidateFormState(var emailError: Int? = null,
                        var passwordError: Int? = null,
                        var isDataValid: Boolean = false) {
}