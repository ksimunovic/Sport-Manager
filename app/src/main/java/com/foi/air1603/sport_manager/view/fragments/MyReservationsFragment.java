package com.foi.air1603.sport_manager.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foi.air1603.sport_manager.MainActivity;
import com.foi.air1603.sport_manager.R;
import com.foi.air1603.sport_manager.adapters.MyReservationsExpandableItem;
import com.foi.air1603.sport_manager.adapters.MyReservationsRecycleAdapter;
import com.foi.air1603.sport_manager.entities.Reservation;
import com.foi.air1603.sport_manager.presenter.MyReservationsPresenter;
import com.foi.air1603.sport_manager.presenter.MyReservationsPresenterImpl;
import com.foi.air1603.sport_manager.view.MyReservationsView;

import java.util.ArrayList;
import java.util.List;


public class MyReservationsFragment extends android.app.Fragment implements MyReservationsView {

    private static final String TAG = "MyReservationsFragment";
    protected RecyclerView mRecyclerView;
    MyReservationsPresenter myReservationsPresenter;
    MyReservationsRecycleAdapter adapter;

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
        myReservationsPresenter = new MyReservationsPresenterImpl(this);
        myReservationsPresenter.getUserReservationsData();

    }
    @Override
    public void loadRecycleViewData(List<Reservation> reservations) {
        List<MyReservationsExpandableItem> reservationsItems = new ArrayList<>();
        System.out.println("MyReservationsFragment:LoadRecyclerViewData");

        for (Reservation res : reservations) {
            if (res.appointment == null) {
                continue;
            }

            MyReservationsExpandableItem tmp = new MyReservationsExpandableItem(res);
            reservationsItems.add(tmp);
        }
        if (mRecyclerView != null) {
            adapter = new MyReservationsRecycleAdapter(getActivity(), reservationsItems);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        MainActivity.dismissProgressDialog();
    }
}
