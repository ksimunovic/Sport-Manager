package com.foi.air1603.sport_manager.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Generalko on 28-Dec-16.
 */

public class Reservation {

    public int id;
    public String created;
    public String submitted;
    public String confirmed;
    public Appointment appointment;
    public Sport sport;
    public Team team;
    private List<Reservation> reservationChildList;

    public Reservation() {
    }

    public Reservation(Reservation reservation) {
        id = reservation.id;
        created = reservation.created;
        appointment = reservation.appointment;
        submitted = reservation.submitted;
        confirmed = reservation.confirmed;
        sport = reservation.sport;
    }

    public List<Reservation> getReservationsChildList() {
        reservationChildList = new ArrayList<>();
        reservationChildList.add(this);
        return reservationChildList;
    }


}