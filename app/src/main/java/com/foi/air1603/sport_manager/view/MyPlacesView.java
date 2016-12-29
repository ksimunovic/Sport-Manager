package com.foi.air1603.sport_manager.view;

import java.util.List;

/**
 * Created by Korisnik on 28-Dec-16.
 */

public interface MyPlacesView {
    void showMyPlaces(List<Integer> id, List<String> name, List<String> address, List<String> contact, List<String> img , List<String> workingHoursFrom, List<String> workingHoursTo, List<String> lat, List<String> lon);
    void changeFragmentToAddAppointmentFragment(Integer id);
    void changeFragmentToPlaceReservationFragment(Integer id);
}