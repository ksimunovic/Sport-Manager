package com.foi.air1603.sport_manager.view.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foi.air1603.sport_manager.MainActivity;
import com.foi.air1603.sport_manager.R;
import com.foi.air1603.sport_manager.adapters.MyPlaceReservationsRecycleAdapter;
import com.foi.air1603.sport_manager.entities.Appointment;
import com.foi.air1603.sport_manager.presenter.MyPlaceReservationsPresenterImpl;
import com.foi.air1603.sport_manager.view.MyPlacesReservationView;

import java.util.List;

/**
 * Created by Korisnik on 29-Dec-16.
 */

public class MyPlacesReservationFragment extends Fragment implements MyPlacesReservationView {
    private RecyclerView recyclerView;
    MyPlaceReservationsPresenterImpl presenter;
    private MainActivity activity;
    int searchParameter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Rezervacije mojih objekata");
        View v = inflater.inflate(R.layout.fragment_my_places_reservations_list, null);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchParameter = bundle.getInt("place_id");
        }
        activity = (MainActivity) getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_MyplacesReservationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter = new MyPlaceReservationsPresenterImpl(this);
        presenter.getAllAppointmentsByPlaceId(searchParameter);

    }

    @Override
    public void showPlaceReservations(List<Appointment> appointmentList) {
        recyclerView.setAdapter(new MyPlaceReservationsRecycleAdapter(this, appointmentList));
    }

}