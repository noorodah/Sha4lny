package com.example.sha4lny.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha4lny.MainActivity;
import com.example.sha4lny.R;
import com.example.sha4lny.classes.User;
import com.example.sha4lny.register.regPage2Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class loginActivity extends AppCompatActivity {

    /// checking if a user is already logged in
    boolean state;
    //
    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //
   // DATABASE REFERENCE
    DatabaseReference myRef;

    /////

    // views
    EditText edtUser,edtPass;
    Button btnLogIn;
    TextView txtReg;
//

    // strings
    String username,password;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /// Firebase connection
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Users");
        ///



        // views
        btnLogIn=findViewById(R.id.btnSignIn);
        edtPass = findViewById(R.id.edtLoginPassword);
        edtUser= findViewById(R.id.edtLoginUser);
        //
        edtUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {  edtUser.setGravity(Gravity.CENTER | Gravity.START); edtUser.setHint("");}
                else
                {edtUser.setGravity(Gravity.CENTER | Gravity.START);  edtUser.setHint("اسم المستخدم");}
            }
        });
        edtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {  edtPass.setGravity(Gravity.CENTER | Gravity.START); edtPass.setHint("");}
                else
                {
                    if(edtPass.getText().toString().isEmpty())
                    {edtPass.setGravity(Gravity.CENTER | Gravity.END);  edtPass.setHint("كلمة المرور");}
                    else
                    {edtPass.setGravity(Gravity.CENTER | Gravity.START); edtPass.setHint("");}
              }
            }
        });
        // Login action starts

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // fetching written info from edit texts

                username = edtUser.getText().toString().trim();
                password=edtPass.getText().toString().trim();

                ////
                if(username.isEmpty() || password.isEmpty())
                    Toast.makeText(loginActivity.this, "يرجى ملئ كل الفراغات", Toast.LENGTH_SHORT).show();
                else {

                   myRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           boolean logged=false;
                       for (DataSnapshot data : snapshot.getChildren()){
                                User user = data.getValue(User.class);
                                if(user.getUsername().equals(username))
                                {
                                    logged=true;
                                    if(user.getPassword().equals(password))
                                    {   Toast.makeText(loginActivity.this, "تم تسجيل دخولك بنجاح", Toast.LENGTH_SHORT).show();
                                    editor.putString("username",username);
                                    editor.putBoolean("logged",true);
                                    editor.commit();
                                    Intent intent = new Intent(loginActivity.this,MainActivity.class);
                                    startActivity(intent);

                                    }
                                    else
                                        Toast.makeText(loginActivity.this, "كلمة السر خاطئة", Toast.LENGTH_SHORT).show();

                                }


                          }
                       if(!logged)
                           Toast.makeText(loginActivity.this, "اسم المستخدم غير متوفر في قاعدة البيانات", Toast.LENGTH_SHORT).show();

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });


                }
            }
        });


        ////////////




        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        /////


// Checking if a user is already logged in
        state= sharedpreferences.getBoolean("logged",false);
        if(state){
            Intent intent=new Intent(loginActivity.this,MainActivity.class);
            startActivity(intent);
        }
        ///

        // MOVING TO REGISTERATION PAGE
      txtReg = findViewById(R.id.txtNotRegistered);
      txtReg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(loginActivity.this, com.example.sha4lny.register.registerationActivity.class);
            startActivity(intent);
          }
      });




    }
}