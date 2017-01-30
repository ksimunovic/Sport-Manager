package com.foi.air1603.sport_manager.verifications;

import android.app.Activity;

import com.foi.air1603.nfc_verification_module.NfcVerificationCaller;
import com.foi.air1603.nfc_verification_module.NfcVerificationHandler;
import com.foi.air1603.sport_manager.entities.Appointment;

/**
 * Created by Karlo on 20.1.2017..
 */

class NfcVerification implements Verification, NfcVerificationHandler {
    private VerificationListener mVerificationListener;

    @Override
    public void VerifyApp(VerificationListener verificationListener, Activity activity, Appointment appointment) {
        System.out.println("----------------->4. NfcVerification:VerifyApp");

        mVerificationListener = verificationListener;

        NfcVerificationCaller call = NfcVerificationCaller.getInstance().Init(this);
        if (activity != null) {
            call.startActivity(activity, appointment.id.toString());
        }
    }

    @Override
    public void onResultArrived(Integer result) {
        System.out.println("----------------->7. NfcVerification:onResultArrived");
        mVerificationListener.onVerificationResult(result);
    }
}
