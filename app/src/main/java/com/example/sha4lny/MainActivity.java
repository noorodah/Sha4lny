package com.example.sha4lny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha4lny.classes.User;
import com.example.sha4lny.login.loginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //

    TextView txtWelcome;

    // Username and user class
    String username;
    User userClass;

    boolean state;
    // DATABASE REFERENCE
    DatabaseReference myRef;

    /////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

txtWelcome = findViewById(R.id.txtWelcome);
        /// Firebase connection
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Users");
        ///



        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////

        /// checking if user is logged in
        state= sharedpreferences.getBoolean("logged",false);
        if(!state){
            Intent intent=new Intent(MainActivity.this,com.example.sha4lny.login.loginActivity.class);
            startActivity(intent);
        }
        ///

        username = sharedpreferences.getString("username","null");
        // getting user's info
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getValue(User.class).getUsername().equals(username))
                        userClass=data.getValue(User.class);
                    txtWelcome.setText("Welcome to our app "+ userClass.getName() );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//



    }
}