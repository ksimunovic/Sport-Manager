package com.foi.air1603.sport_manager.presenter;

import com.foi.air1603.sport_manager.model.UserModel;

import com.foi.air1603.sport_manager.view.RegisterView;

/**
 * Created by Robert on 11-Nov-16.
 */

public class RegisterPresenterImpl implements RegisterPresenter {

    private final RegisterView view;
    boolean pass = false;
    boolean email = false;
    boolean valid = false;
    boolean validation = true;
    UserModel userModel;


    public RegisterPresenterImpl(RegisterView registerView) {
        this.view = registerView;
        this.userModel = new UserModel();
    }
    @Override
    public boolean validateUserRegister() {


        if (view.getEmailFromEditText().isEmpty()) {
            String message = "Polje je obavezno";
            String editTextName = "etMail";
            view.displayError(editTextName, message);
            validation = false;

        } else {
            valid = isValidEmailAddress(view.getEmailFromEditText());

            if (valid) {
                email = true;
            } else {
                validation = false;
                String message = "Email nije validan";
                String editTextName = "etMail";
                view.displayError(editTextName, message);
            }
        }
        if (view.getUsernameFromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etUsernameR";
            view.displayError(editTextName, message);
        }

        if (view.getPasswordFromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etPasswordR1";
            view.displayError(editTextName, message);
        }
        if (view.getPassword1FromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etPasswordR2";
            view.displayError(editTextName, message);
        } else {
            if (view.getPasswordFromEditText().equals(view.getPassword1FromEditText())) {
                pass = true;

            } else {
                validation = false;
                String message = "Lozinke se moraju podudarati!";
                String editTextName = "etPasswordR2";
                view.displayError(editTextName, message);
            }
        }
        if (view.getNameFromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etName";
            view.displayError(editTextName, message);
        }
        if (view.getLastNameFromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etLastName";
            view.displayError(editTextName, message);
        }
        if (view.getAddressFromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etAddress";
            view.displayError(editTextName, message);
        }
        if (view.getPhoneNumberFromEditText().isEmpty()) {
            validation = false;
            String message = "Polje je obavezno";
            String editTextName = "etPhoneNumber";
            view.displayError(editTextName, message);
        }

        return validation;
    }

    private boolean isValidEmailAddress(String emailFromEditText) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(emailFromEditText);
        return m.matches();
    }


}



