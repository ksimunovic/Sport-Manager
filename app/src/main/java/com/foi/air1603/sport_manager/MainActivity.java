package com.foi.air1603.sport_manager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foi.air1603.sport_manager.entities.User;
import com.foi.air1603.sport_manager.helper.enums.Rights;
import com.foi.air1603.sport_manager.view.LoginView;
import com.foi.air1603.sport_manager.view.ProfileView;
import com.foi.air1603.sport_manager.view.fragments.AllPlacesFragment;
import com.foi.air1603.sport_manager.view.fragments.LoginFragment;
import com.foi.air1603.sport_manager.view.fragments.ProfileFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Karlo on 3.12.2016..
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 35;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static User user;
    private AllPlacesFragment allPlacesFragment;
    private NavigationView navigationView;
    private FragmentTransaction fragmentTransaction;
    private Rights rights;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        user = getIntent().getExtras().getParcelable("User");
        rights = rights.getRightFormInt(user.type);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavigationView();
    }

    private void setNavigationView() {
        switch (rights) {
            case User:
                setUserView();
                break;
            case Admin:
                setAdminView();
                break;
            case Owner:
                setOwnerView();
                break;
            default:
                break;
        }
    }

    private void setUserView() {
        setAllUsersDataToHeaderView();
        hideUserDrawerActionItems();
        initAllPlacesFragment();
    }

    public void updateHeaderView(){
        String TAG = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.v(TAG, "Pokušavam refreshat sliku");

        user = getIntent().getExtras().getParcelable("User");
        setAllUsersDataToHeaderView();
    }

    private void setAllUsersDataToHeaderView() {
        View header = navigationView.getHeaderView(0);
        TextView firstLastName = (TextView) header.findViewById(R.id.textViewFirstLastName);
        TextView email = (TextView) header.findViewById(R.id.textViewUserEmail);
        ImageView userImg = (ImageView) header.findViewById(R.id.imageViewUserPicture);

        String TAG = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.v(TAG, "Učitavam sve podatke u header, img url je: "+user.img);

        if (!user.first_name.isEmpty()
                && !user.last_name.isEmpty()) {
            firstLastName.setText(user.first_name + ' ' + user.last_name + ' ');
        } else {
            firstLastName.setText(null);
        }
        if (!user.email.isEmpty()) {
            email.setText(user.email);
        } else {
            email.setText(null);
        }
        if (user.img != null && !user.img.isEmpty()) {
            Picasso.with(this).load(user.img).into(userImg);
        } else {
            userImg.setImageBitmap(null);
        }

    }

    private void initAllPlacesFragment() {
        allPlacesFragment = new AllPlacesFragment();
        fragmentTransaction.add(R.id.fragment_container, allPlacesFragment, "HELLO");
        fragmentTransaction.commit();
    }

    private void hideUserDrawerActionItems() {
        Menu navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_add_new_reservation).setVisible(false);
    }


    private void setAdminView() {
        setAllUsersDataToHeaderView();
        initAllPlacesFragment();
    }

    private void setOwnerView() {
        setAllUsersDataToHeaderView();
        initAllPlacesFragment();
    }


    //region Activity methods
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            openProfileFragment();
        } else if (id == R.id.nav_places_list) {
            openAllPlacesFragment();
        } else if (id == R.id.nav_my_reserved) {

        } else if (id == R.id.nav_my_reservations) {

        } else if (id == R.id.nav_add_new_reservation) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openProfileFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new ProfileFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void openAllPlacesFragment() {
        if (allPlacesFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, allPlacesFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void logout() {
        pref = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(MainActivity.this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}