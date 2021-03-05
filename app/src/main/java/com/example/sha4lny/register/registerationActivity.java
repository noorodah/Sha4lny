package com.example.sha4lny.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sha4lny.R;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

public class registerationActivity extends AppCompatActivity  {
EditText edtFullName,edtAge,edtPhone,edtLoc;
Button btnNext;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);


        edtAge=findViewById(R.id.edtRegAge);
        edtFullName=findViewById(R.id.edtRegFullName);
        edtPhone=findViewById(R.id.edtRegPhone);
        edtLoc= findViewById(R.id.edtRegLoc);
        btnNext = findViewById(R.id.btnRegNext);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(edtAge.getText().toString().trim().isEmpty()|| edtFullName.getText().toString().trim().isEmpty()|| edtPhone.getText().toString().trim().isEmpty()|| edtLoc.getText().toString().trim().isEmpty())
                    Toast.makeText(registerationActivity.this, "الرجاء تعبئة كل الفراغات", Toast.LENGTH_SHORT).show();
                else if (edtFullName.getText().toString().length() < 4)
                    Toast.makeText(registerationActivity.this, "الاسم يجب الا يقل عن 4 منازل", Toast.LENGTH_SHORT).show();

                else  if (edtPhone.getText().toString().length()<10 || edtPhone.getText().toString().length()>14 )
                    Toast.makeText(registerationActivity.this, "تاكد من ادخالك لرقم هاتف صحيح", Toast.LENGTH_SHORT).show();
                else {
                    editor.putString("regAge",edtAge.getText().toString());
                    editor.putString("regName",edtFullName.getText().toString());
                    editor.putString("regPhone",edtPhone.getText().toString());
                    editor.putString("regLoc",edtLoc.getText().toString());

                    editor.commit();
                    Toast.makeText(registerationActivity.this, "Done!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(registerationActivity.this,regPage2Activity.class);
                    startActivity(intent);


                }
            }
        });




    }


}