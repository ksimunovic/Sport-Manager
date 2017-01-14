package com.foi.air1603.sport_manager.view.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foi.air1603.sport_manager.MainActivity;
import com.foi.air1603.sport_manager.R;
import com.foi.air1603.sport_manager.adapters.MyReservationsExpandableItem;
import com.foi.air1603.sport_manager.adapters.MyReservationsRecycleAdapter;
import com.foi.air1603.sport_manager.entities.Reservation;
import com.foi.air1603.sport_manager.verifications.PasswordVerification;
import com.foi.air1603.sport_manager.verifications.Verification;
import com.foi.air1603.sport_manager.verifications.VerificationListener;
import com.foi.air1603.sport_manager.presenter.MyReservationsPresenter;
import com.foi.air1603.sport_manager.presenter.MyReservationsPresenterImpl;
import com.foi.air1603.sport_manager.view.MyReservationsView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MyReservationsFragment extends android.app.Fragment implements MyReservationsView, VerificationListener {

    private static final String TAG = "MyReservationsFragment";
    protected RecyclerView mRecyclerView;
    MyReservationsPresenter myReservationsPresenter;
    MyReservationsRecycleAdapter adapter;
    public Reservation reservation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Moje rezervacije");
        MainActivity.showProgressDialog("Dohvaćanje podataka");

        View rootView = inflater.inflate(R.layout.fragment_my_reservations, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myReservationsPresenter = MyReservationsPresenterImpl.getInstance().Init(this);
        myReservationsPresenter.getUserReservationsData();
    }

    @Override
    public void loadRecycleViewData(List<Reservation> reservations) {
        List<MyReservationsExpandableItem> reservationsItems = new ArrayList<>();
        System.out.println("MyReservationsFragment:LoadRecyclerViewData");

        if (reservations == null) {
            MainActivity.dismissProgressDialog();
            Toast.makeText(getActivity(),
                    "Trenutno nemate svojih rezervacija!", Toast.LENGTH_LONG).show();
            return;
        }

        for (Reservation res : reservations) {
            if (res.appointment == null) {
                continue;
            }

            MyReservationsExpandableItem tmp = new MyReservationsExpandableItem(res);
            reservationsItems.add(tmp);
        }
        if (mRecyclerView != null) {
            adapter = new MyReservationsRecycleAdapter(getActivity(), reservationsItems, this);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        MainActivity.dismissProgressDialog();
    }

    @Override
    public void verifyByPassword(String pass) {
        Verification passwordVerification = new PasswordVerification();
        passwordVerification.VerifyApp(this, getActivity(), pass);
    }

    @Override
    public void setObject(Reservation reservation) {
         this.reservation = reservation;
    }

    @Override
    public Reservation getObject() {
        return this.reservation;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onVerificationResult(Boolean result) {
        System.out.println("MyReservationsFragment:onVerificationResult ---- result is -- " + result);

        if(result){
            Toast.makeText(getActivity(), "Uspješno potvrđen termin!", Toast.LENGTH_LONG).show();
            myReservationsPresenter.updateReservation(getObject());

        } else {
            Toast.makeText(getActivity(), "Greška!", Toast.LENGTH_LONG).show();
        }
    }
}
