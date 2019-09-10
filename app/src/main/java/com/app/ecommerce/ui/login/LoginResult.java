package com.app.ecommerce.ui.login;

import android.support.annotation.Nullable;

import com.app.ecommerce.R;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {


        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
