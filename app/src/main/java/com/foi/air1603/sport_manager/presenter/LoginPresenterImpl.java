package com.foi.air1603.sport_manager.presenter;

import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.example.webservice.AirWebServiceResponse;
import com.example.webservice.User;
import com.foi.air1603.sport_manager.R;
import com.foi.air1603.sport_manager.model.UserInteractor;
import com.foi.air1603.sport_manager.model.UserInteractorImpl;
import com.foi.air1603.sport_manager.view.LoginView;
import com.google.gson.Gson;

import static com.foi.air1603.sport_manager.helper.enums.LoginViewEnums.Password;
import static com.foi.air1603.sport_manager.helper.enums.LoginViewEnums.Username;

/**
 * Created by Generalko on 10.11.2016..
 */

public class LoginPresenterImpl implements LoginPresenter, UserInteractor.OnLoginFinishedListener, PresenterHandler {
    private final LoginView view;
    private UserInteractor userInteractor;
    private User user;

    public LoginPresenterImpl(LoginView loginView) {
        this.view = loginView;
        this.userInteractor = new UserInteractorImpl(this);
    }

    @Override
    public void checkIfInputDataIsEmpty() {
        if (view.getUsernameFromEditText().isEmpty()) {
            view.displayError(Username, "Unesite vrijednost");
        }
        else if (view.getPasswordFromEditText().isEmpty()) {
            view.displayError(Password, "Unesite vrijednost");
        } else {
            compareInputTextToData();
        }
    }

    @Override
    public void onUsernameError() {
        view.displayError(Username, "Korisničko ime ne postoji");
    }

    @Override
    public void onPasswordError() {
        view.displayError(Password, "Unijeli ste krivu lozinku");
    }

    @Override
    public void onSuccess() {
        //todo: go to the next screen
    }

    private void compareInputTextToData() {
        userInteractor.getUserObject(this, "getUserByUsername", view.getUsernameFromEditText());

    }

    @Override
    public void getResponseData(Object result) {
        AirWebServiceResponse response = (AirWebServiceResponse) result;
        user = new Gson().fromJson(response.getData(), User.class);

        if (user == null) {
            onUsernameError();
        } else{
            if(!view.getPasswordFromEditText().equals(user.password)){
                onPasswordError();
            }
        }
    }
}
