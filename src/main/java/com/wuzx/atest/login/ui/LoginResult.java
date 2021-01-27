package com.wuzx.atest.login.ui;

import androidx.annotation.Nullable;

import com.wuzx.atest.login.data.Result;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Result.Error error;

    LoginResult(@Nullable Result.Error error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    public Result.Error getError() {
        return error;
    }
}