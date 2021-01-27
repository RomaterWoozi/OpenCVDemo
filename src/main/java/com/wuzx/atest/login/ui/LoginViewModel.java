package com.wuzx.atest.login.ui;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.jchsmart.network.AndroidNetworking;
import com.jchsmart.network.error.ANError;
import com.jchsmart.network.interfaces.AnalyticsListener;
import com.jchsmart.network.interfaces.JSONObjectRequestListener;
import com.orhanobut.logger.Logger;
import com.wuzx.atest.R;
import com.wuzx.atest.login.data.Constants;
import com.wuzx.atest.login.data.LoginRepository;
import com.wuzx.atest.login.data.Result;
import com.wuzx.atest.login.data.model.LoggedInUser;
import com.wuzx.atest.login.data.model.User;

import org.json.JSONObject;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    private AnalyticsListener analyticsListener = new AnalyticsListener() {
        @Override
        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
            Logger.d(" timeTakenInMillis : " + timeTakenInMillis);
            Logger.d(" bytesSent : " + bytesSent);
            Logger.d(" bytesReceived : " + bytesReceived);
            Logger.d(" isFromCache : " + isFromCache);
        }
    };

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        User user = new User(username, password);
        String paramBodyJsonStr = new Gson().toJson(user);
        Logger.d("paramBodyJsonStr=" + paramBodyJsonStr);
        // handle login
        AndroidNetworking.post()
                .setContentType("application/json; charset=utf-8")
                .setTag(this)
                .setUrl(Constants.URL_LOGIN)
                .build()
                .setApplicationJsonString(paramBodyJsonStr)
                .setAnalyticsListener(analyticsListener)
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject result) {
                        Logger.d("response result=" + result.toString());
                        Result<LoggedInUser> defultResult = loginRepository.getDataSource().login(username, password);
                        LoggedInUser data = ((Result.Success<LoggedInUser>) defultResult).getData();
                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                    }

                    @Override
                    public void onError(ANError anError) {
                        loginResult.setValue(new LoginResult(new Result.Error(anError)));
                        Logger.d("anError=" + anError.toString());
                        if (!TextUtils.isEmpty(anError.getErrorDetail())) {
                            Logger.d("getMessage=" + anError.getMessage() + " getErrorDetail=" + anError.getErrorDetail());
                        }
                    }
                });

    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}