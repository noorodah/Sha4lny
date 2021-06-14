package com.example.sha4lny.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sha4lny.R;
import com.example.sha4lny.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class regPage2Activity extends AppCompatActivity {
String fullName,Age,loc,phone,username,password,job;
User user;
EditText edtUserName,edtPassword,edtPasswordConfirm,edtJob;
    SharedPreferences sharedpreferences;
    Button btnFinish;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_page2);


        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        btnFinish=findViewById(R.id.btnRegFinish);
        /////



        // Firebase con
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Users");
        ///


        /// edit texts
        edtUserName=findViewById(R.id.edtRegUserName);
        edtJob=findViewById(R.id.edtRegJob);
        edtPassword= findViewById(R.id.edtRegPass);
        edtPasswordConfirm= findViewById(R.id.edtRegPassConfirm);
        ///



        // Getting text from prefrences
        fullName = sharedpreferences.getString("regName","NotAssigned");
        Age = sharedpreferences.getString("regAge","NotAssigned");
        phone = sharedpreferences.getString("regPhone","NotAssigned");
        loc = sharedpreferences.getString("regLoc","NotAssigned");


        ///



        // JOB IS BEING DONE HERE
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CHECKING IF SOME FIELDS ARE EMPTY
                if(edtJob.getText().toString().trim().isEmpty()|| edtPassword.getText().toString().trim().isEmpty()|| edtPasswordConfirm.getText().toString().trim().isEmpty()|| edtUserName.getText().toString().trim().isEmpty())
                    Toast.makeText(regPage2Activity.this, "الرجاء تعبئة كل الفراغات", Toast.LENGTH_SHORT).show();
                else if ( edtUserName.getText().toString().length()<3 )
                    Toast.makeText(regPage2Activity.this, "اسم المستخدم يجب الا يقل عن 3 منازل", Toast.LENGTH_SHORT).show();

                else if(edtUserName.getText().toString().contains(" ") )
                    Toast.makeText(regPage2Activity.this, "اسم المستخدم يجب الا يحتوي على فراغات", Toast.LENGTH_SHORT).show();

                else if(edtPassword.getText().toString().length() <6)
                    Toast.makeText(regPage2Activity.this, "كلمة السر يجب الا تقل عن 6 منازل", Toast.LENGTH_SHORT).show();

             // CHECKING IF PASSWORDS DON'T MATCH
                else if (!edtPasswordConfirm.getText().toString().equals(edtPassword.getText().toString()))
                    Toast.makeText(regPage2Activity.this, "كلمات السر غير متطابقة", Toast.LENGTH_SHORT).show();



                else{
                    username = edtUserName.getText().toString();
                    password = edtPassword.getText().toString();
                    job= edtJob.getText().toString();
                    ArrayList<String>temp = new ArrayList<>();
                    temp.add("none");
                    user = new User(username,fullName,password,Age,phone,loc,job,temp);
                    Query query = myRef.orderByChild("username").equalTo(edtUserName.getText().toString());
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // CHECKING IF USER EXISTS
                            if(!snapshot.exists()) {
                                myRef.push().setValue(user);
                                Toast.makeText(regPage2Activity.this, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(regPage2Activity.this,com.example.sha4lny.login.loginActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(regPage2Activity.this, "اسم المستخدم ماخوذ بالفعل", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);


                }

            }
        });

    }
}