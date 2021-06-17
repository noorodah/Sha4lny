package com.example.sha4lny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sha4lny.classes.Job;
import com.example.sha4lny.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class lookingForEmp extends AppCompatActivity {
    boolean alreadyPosted;
    //Views
    Spinner spnrJobTitle,spnrJob,spnrJobLoc,spnrWorkHoursFrom,spnrWorkHoursTo,spnrPaymentTime;
    EditText edtJobTitle,edtJobComment,edtJobDailyPayment;
    RadioButton rdbMor,rdbEvn;
    Button btnSubmit;
    //Job class
    Job newJob ,oldjob;

    // Strings

    String jobTitle,seekedJob,jobLoc,workHoursFrom,workHoursTo,paymentTime,jobWrittenTitle,jobComment,dailyPayment,jobTime,jobID;

    // DATABASE REFERENCE
    DatabaseReference myRef;


    // Shared prefereces
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //

    boolean state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_for_emp);
       // Views initialization
        //Spinners
        spnrJob = findViewById(R.id.spnrJobs);
        spnrJobLoc=findViewById(R.id.spnJonPlace);
        spnrJobTitle=findViewById(R.id.spnrEmpJobs);
        spnrPaymentTime = findViewById(R.id.spnrPayTime);
        spnrWorkHoursFrom=findViewById(R.id.spnrTimeFrom);
        spnrWorkHoursTo = findViewById(R.id.spnrTimeTo);
        /// This is to change spinners color
        // job
        ArrayAdapter adapter = ArrayAdapter.createFromResource(lookingForEmp.this,R.array.jobTitle_array,R.layout.spinner_job);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrJob.setAdapter(adapter);
        //job title
        adapter = ArrayAdapter.createFromResource(lookingForEmp.this,R.array.jobs_array,R.layout.spinner_jobtitle);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrJobTitle.setAdapter(adapter);
        // cities
        adapter = ArrayAdapter.createFromResource(lookingForEmp.this,R.array.cities_array,R.layout.spinner_citites);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrJobLoc.setAdapter(adapter);
        //working hours
        adapter = ArrayAdapter.createFromResource(lookingForEmp.this,R.array.workingTime_array,R.layout.spinner_working_hours);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrWorkHoursFrom.setAdapter(adapter);
        spnrWorkHoursTo.setAdapter(adapter);
        //working time
        adapter = ArrayAdapter.createFromResource(lookingForEmp.this,R.array.paymentTime,R.layout.spinner_workingtime);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrPaymentTime.setAdapter(adapter);


        edtJobComment = findViewById(R.id.edtComment);
        /// Edit texts
        edtJobComment = findViewById(R.id.edtComment);
        edtJobDailyPayment = findViewById(R.id.edtPayAmount);
        edtJobTitle = findViewById(R.id.edtTitle);
        /// RadioButtons
        rdbEvn = findViewById(R.id.rdbEvn);
        rdbMor=findViewById(R.id.rdbMor);
        rdbMor.setSelected(true);
        //Button
        btnSubmit = findViewById(R.id.btnPost);
        //----------------------Button on click-----------------------
alreadyPosted = false;
//
        User userclass;

        /// Firebase connection
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Jobs");
        ///

        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();





        oldjob= null;
        String json = sharedpreferences.getString("jobApp",null);
        Gson gson= new Gson();
        String userJson = sharedpreferences.getString("userClass","");
        userclass = gson.fromJson(userJson,User.class);
        editor.putString("jobApp",null).commit();
        if(json != null) {

            oldjob = gson.fromJson(json, Job.class);

        }/////
        if (oldjob!=null){
            alreadyPosted=true;

        }
        buttonOperation(alreadyPosted);
        /// checking if user is logged in
        state= sharedpreferences.getBoolean("logged",false);
        if(!state){
            Intent intent=new Intent(this,com.example.sha4lny.login.loginActivity.class);
            startActivity(intent);
        }
        ///

        editor.putString("jobApp",null).commit();


    }

    private void buttonOperation(boolean alreadyPosted) {

        editor.putString("jobApp",null).commit();
        if (alreadyPosted)
        {   btnSubmit.setText("حفظ التعديلات");
        edtJobTitle.setText(oldjob.getPostTitle());
        edtJobComment.setText(oldjob.getPostComment());
        edtJobDailyPayment.setText(oldjob.getPaymentAmount());
        spnrWorkHoursTo.setSelection(Integer.parseInt(oldjob.getWorkHourseTo())-1);
        spnrWorkHoursFrom.setSelection(Integer.parseInt(oldjob.getWorkHoursFrom())-1);
        int spnrJobTitlePos=0,spnrJobPos=0,spnrPaymentTimePos=0,spnrJobLocPos =0;

        for(int i=0;i<12;i++){

            if (spnrJobTitle.getItemAtPosition(i).equals(oldjob.getFreeSpot()))
            {spnrJobTitlePos=i;break;}
        }
        for (int i = 0 ; i <14 ; i++){
            if(spnrJob.getItemAtPosition(i).equals(oldjob.getJobType()))
            {spnrJobPos=i;break;}
        }
        for (int i =0 ; i < 3; i++)
        {
            if(spnrPaymentTime.getItemAtPosition(i).equals(oldjob.getPaymentTime()))
            {spnrPaymentTimePos=i;break;}
        }
        for(int i = 0;i<11;i++){
            if(spnrJobLoc.getItemAtPosition(i).equals(oldjob.getJobLoc()))
            {spnrJobLocPos=i ;break;}
        }
        spnrJobLoc.setSelection(spnrJobLocPos);
        spnrPaymentTime.setSelection(spnrPaymentTimePos);
            spnrJob.setSelection(spnrJobPos);
        spnrJobTitle.setSelection(spnrJobTitlePos);

        if(oldjob.getWorkTime().equals("مسائي"))
            rdbEvn.setChecked(true);
        else
            rdbMor.setChecked(true);

        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtJobTitle.getText().toString().trim().isEmpty() || edtJobDailyPayment.getText().toString().trim().isEmpty()){
                    Toast.makeText(lookingForEmp.this, "الرجاء التاكد من ملئ العنوان والاجرة اليومية", Toast.LENGTH_SHORT).show();}
                else {
                    // String allignment

                    jobTitle = spnrJobTitle.getSelectedItem().toString();
                    jobWrittenTitle = edtJobTitle.getText().toString();
                    seekedJob = spnrJob.getSelectedItem().toString();
                    jobLoc=spnrJobLoc.getSelectedItem().toString();
                    jobComment=edtJobComment.getText().toString();
                    workHoursFrom=spnrWorkHoursFrom.getSelectedItem().toString();
                    workHoursTo=spnrWorkHoursTo.getSelectedItem().toString();
                    paymentTime=spnrPaymentTime.getSelectedItem().toString();
                    dailyPayment=edtJobDailyPayment.getText().toString();



                    //job time radio buttons

                    if(rdbEvn.isChecked())
                        jobTime = "مسائي";
                    else
                        jobTime = "صباحي";

                    Gson gson= new Gson();
                    String userJson = sharedpreferences.getString("userClass","");
                 User   userclass = gson.fromJson(userJson,User.class);
                    //New Job init
                    if(!alreadyPosted) {
                        jobID  =myRef.push().getKey();
                        newJob = new Job(jobWrittenTitle, seekedJob, jobTitle, workHoursFrom, workHoursTo, jobTime, paymentTime, dailyPayment, jobComment, jobLoc, jobID, sharedpreferences.getString("username", ""),userclass.getName(),false);

                        // pushing it to database

                        myRef.push().setValue(newJob).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(lookingForEmp.this, "تم نشر طلبك بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data : snapshot.getChildren()){
                                    if(oldjob.getJobID().equals(data.getValue(Job.class).getJobID())){
                                        jobID  =oldjob.getJobID();
                                        newJob = new Job(jobWrittenTitle, seekedJob, jobTitle, workHoursFrom, workHoursTo, jobTime, paymentTime, dailyPayment, jobComment, jobLoc, jobID, sharedpreferences.getString("username", ""),userclass.getName(),false);
                                        data.getRef().setValue(newJob).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(lookingForEmp.this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        editor.putString("jobApp",null);
                                        editor.commit();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
                editor.putString("jobApp",null);
                editor.commit();}

        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(lookingForEmp.this,MainActivity.class);
        startActivity(intent);

    }
}