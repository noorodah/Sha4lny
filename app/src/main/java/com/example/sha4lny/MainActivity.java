
package com.example.sha4lny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha4lny.classes.DataModel;
import com.example.sha4lny.classes.DrawerItemCustomAdapter;
import com.example.sha4lny.classes.MessageModel;
import com.example.sha4lny.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";
    //

// Nav drawer

    Toolbar toolbar;


    //Fragments
    HomeFragment homeFrag;


    // Chip nav components
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;


    // Username and user class
    String username;
    User userClass;


    boolean state;
    // DATABASE REFERENCE
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupToolbar();


        //bottom nav work

        bottomNav = findViewById(R.id.bottm_nav);


        // bottom nav on item selection
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;

                switch (id) {

                    case R.id.nav_home:
                        fragment = homeFrag;
                        break;
                    case R.id.nav_srch_worker:
                        fragment = new srchForEmpFrag();
                        break;
                    case R.id.nav_search:
                        fragment = new srchForJobFrag();
                        break;

                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.myFrame, fragment).commit();
                } else
                    Log.e(TAG, "error in creating frag");
            }
        });


        /// Firebase connection
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Users");
        ///


        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////

        /// checking if user is logged in

        state = sharedpreferences.getBoolean("logged", false);
        if (!state) {
            Intent intent = new Intent(MainActivity.this, com.example.sha4lny.login.loginActivity.class);
            startActivity(intent);
        }
        ///

        username = sharedpreferences.getString("username", "null");


        // getting user's info
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getValue(User.class).getUsername().equals(username))
                        userClass = data.getValue(User.class);
                    /// Showing user info in profile

                }
                Gson gson = new Gson();
                String Json = gson.toJson(userClass);
                editor.putString("userClass", Json);
                editor.commit();

                homeFrag = new HomeFragment(userClass.getName(), userClass.getAge(), userClass.getLocation(), userClass.getJob());
                // first time to open activity
                if (savedInstanceState == null) {
                    bottomNav.setItemSelected(R.id.nav_home, true);
                    fragmentManager = getSupportFragmentManager();


                    fragmentManager.beginTransaction().replace(R.id.myFrame, homeFrag, "HOME").commit();
                    HomeFragment f = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HOME");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//

/// this is to exit when back button is pressed

    }

    int backButtonCount = 0;

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId()){
    case R.id.menuLogOut:

        new AlertDialog.Builder(this)
                .setTitle("تاكيد")
                .setMessage("هل انت متاكد من انك ترغب في تسجيل الخروج")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        editor.putBoolean("logged",false);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this,com.example.sha4lny.login.loginActivity.class);
                        startActivity(intent);

                    }})
                .setNegativeButton("لا", null).show();

        break;
}
        //respond to menu item selection
return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}