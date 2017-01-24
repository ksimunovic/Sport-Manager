package com.foi.air1603.sport_manager.view.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.foi.air1603.sport_manager.MainActivity;
import com.foi.air1603.sport_manager.R;
import com.foi.air1603.sport_manager.entities.Appointment;
import com.foi.air1603.sport_manager.entities.Place;
import com.foi.air1603.sport_manager.entities.Reservation;
import com.foi.air1603.sport_manager.entities.Team;
import com.foi.air1603.sport_manager.entities.User;
import com.foi.air1603.sport_manager.presenter.AppointmentPresenter;
import com.foi.air1603.sport_manager.presenter.AppointmentPresenterImpl;
import com.foi.air1603.sport_manager.presenter.MyReservationsPresenterImpl;
import com.foi.air1603.sport_manager.presenter.SportPresenter;
import com.foi.air1603.sport_manager.presenter.SportPresenterImpl;
import com.foi.air1603.sport_manager.view.ReservationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Korisnik on 26-Dec-16.
 */

public class ReservationFragment extends android.app.Fragment implements ReservationView {

    static Map<String, Integer> inputs = new HashMap<>();

    static {
        inputs.put("appointmentLabel", R.id.tvReservationAppointmentLabel);
        inputs.put("appointmentImage", R.id.reservationAppointmentImage);
        inputs.put("sportImage", R.id.reservationSportImage);
        inputs.put("playersImage", R.id.reservationPlayersImage);
        inputs.put("spinnerAppointment", R.id.spinnerAppointments);
        inputs.put("spinnerSport", R.id.spinnerSports);
        inputs.put("spinnerMaxPlayers", R.id.spinnerPlayers);
        inputs.put("privateSwitch", R.id.switchAppointmentPrivate);
        inputs.put("setAppointmentButton", R.id.buttonSetAppointment);
    }

    AppointmentPresenter appointmentPresenter;
    SportPresenter sportPresenter;
    CalendarView calendar;
    List<String> maxPlayers = new ArrayList<>();
    private Map<String, Integer> sportsMap = new HashMap<>();
    private Map<String, Integer> appointmentsMap = new HashMap<>();
    private int yearGet, monthGet, dayGet, id_place, currentPickedDate;
    private Place place;
    private Spinner spinnerAppointment, spinnerSport, spinnerMaxPlayers;
    private View view;
    private Context context;
    private Reservation userReservation;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Rezervacija termina");
        MyReservationsPresenterImpl.updateData = true;

        context = container.getContext();
        View v = inflater.inflate(R.layout.fragment_reservation, null);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String place_serialized = bundle.getString("Place");
            Place place = new Gson().fromJson(place_serialized, Place.class);
            this.place = place;
            id_place = place.id;
        }
        return v;
    }

    private void showAllViews(Boolean show) {
        for (Map.Entry<String, Integer> entry : inputs.entrySet()) {
            View tmpView = view.findViewById(entry.getValue());
            if (tmpView != null) {
                if (show) {
                    tmpView.setVisibility(View.VISIBLE);
                } else {
                    tmpView.setVisibility(View.GONE);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        this.view = view;

        calendar = (CalendarView) view.findViewById(R.id.calendarViewReservation);
        calendar.setFirstDayOfWeek(2);
        calendar.setMinDate(System.currentTimeMillis() - 1000);

        appointmentPresenter = AppointmentPresenterImpl.getInstance().Init(this);
        MainActivity.showProgressDialog("Učitavanje termina");
        appointmentPresenter.loadAllAppointments();

        sportPresenter = new SportPresenterImpl(this);
        showAllViews(false);

        spinnerAppointment = (Spinner) view.findViewById(R.id.spinnerAppointments);
        spinnerSport = (Spinner) view.findViewById(R.id.spinnerSports);
        spinnerMaxPlayers = (Spinner) view.findViewById(R.id.spinnerPlayers);

        Button setAppointmentButton = (Button) view.findViewById(R.id.buttonSetAppointment);
        setAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userReservation = new Reservation();
                userReservation.sportId = sportsMap.get(spinnerSport.getSelectedItem());
                userReservation.appointmentId = appointmentsMap.get(spinnerAppointment.getSelectedItem());

                java.util.Date utilDate = new java.util.Date(System.currentTimeMillis());
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                userReservation.created = sqlDate.toString();
                Team team = new Team();
                team.users = new ArrayList<>();
                team.name = "Team_" + userReservation.appointmentId + "_" + userReservation.sportId;
                team.created = sqlDate.toString();
                team.userId = MainActivity.user.id;

                User ownerUser = new User();
                ownerUser.id = MainActivity.user.id;
                ownerUser.type = MainActivity.user.type;
                team.users.add(ownerUser);

                userReservation.team = team;

                //MainActivity.showProgressDialog("Kreiranje rezervacije");
                //appointmentPresenter.reservateAppointment(userReservation);

                MainActivity.replaceFragment(new InviteFriendsFragment());
            }
        });

        spinnerAppointment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showAllViews(true);

                int max = Integer.parseInt(maxPlayers.get(position));
                List<Integer> maxPly = new ArrayList<>();
                for (int i = 1; i <= max; i++) {
                    maxPly.add(i);
                }

                ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, maxPly);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMaxPlayers.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        sportPresenter.getMultipleSports();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initializeCalendar() {
        MainActivity.dismissProgressDialog();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        currentPickedDate = (int) (c.getTimeInMillis() / 1000); //Today

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                //date in unix
                yearGet = year;
                monthGet = month;
                dayGet = day;

                if (currentPickedDate == getDate()) {
                    return;
                }

                showAllViews(false);

                currentPickedDate = getDate();
                appointmentPresenter.getAppointmentsForDate(getDate());
            }
        });
        appointmentPresenter.getAppointmentsForDate(currentPickedDate);
    }

    @Override
    public void successfulReservation() {
        MainActivity.dismissProgressDialog();
        Toast.makeText(getActivity(),
                "Uspješno ste rezervirali termin!", Toast.LENGTH_LONG).show();
        getFragmentManager().popBackStack();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getDate() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, yearGet);
        c.set(Calendar.MONTH, monthGet);
        c.set(Calendar.DAY_OF_MONTH, dayGet);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return (int) (c.getTimeInMillis() / 1000L);
    }

    @Override
    public int getIdPlace() {
        return id_place;
    }


    @Override
    public void showAppointmentsForDate(List<Appointment> appointments) {
        if (appointments.size() == 0) {
            showAllViews(false);
            return;
        }

        showAllViews(true);
        maxPlayers.clear();

        List<String> spinnerAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            spinnerAppointments.add(appointment.start + "-" + appointment.end);
            appointmentsMap.put(appointment.start + "-" + appointment.end, appointment.id);
            maxPlayers.add(appointment.maxPlayers.toString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerAppointments);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAppointment.setAdapter(dataAdapter);
    }

    @Override
    public void showSports(List<Integer> id, List<String> name) {

        List<String> sports = new ArrayList<>();
        for (int i = 0; i < id.size(); i++) {
            sports.add(name.get(i));
            sportsMap.put(name.get(i), id.get(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sports);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSport.setAdapter(dataAdapter);
    }
}
