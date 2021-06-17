package com.example.sha4lny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sha4lny.classes.AcceptedJobs;
import com.example.sha4lny.classes.Job;
import com.example.sha4lny.classes.JobsModel;
import com.example.sha4lny.classes.MessageOFIntereset;
import com.example.sha4lny.classes.User;
import com.example.sha4lny.classes.jobAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class searchResultsActivity extends AppCompatActivity implements  jobAdapter.OnJobListener{

    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //
ArrayList<MessageOFIntereset> messageOFInteresetsArrayList;
    User user;
// Search items
    String jobType,jobCity;
   // Job job;
    // DATABASE REFERENCE
    DatabaseReference myRef;
int searchType;
private  ArrayList<Job> jobArrayList;
    FirebaseDatabase database;

    // RecyclerView
private RecyclerView myRecycler;
private ArrayList<JobsModel> jobsModelArrayList;
private com.example.sha4lny.classes.jobAdapter jobAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        /// Firebase connection
      database= FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Jobs");
        ///
messageOFInteresetsArrayList = new ArrayList<MessageOFIntereset>();
        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////
        /////
        Gson gson= new Gson();
        String json = sharedpreferences.getString("jobApp","");
        String userJson = sharedpreferences.getString("userClass","");
        user = gson.fromJson(userJson, User.class);
        searchType= sharedpreferences.getInt("searchType",-1);


        // RecyclerView work
        myRecycler = findViewById(R.id.myRecV);
        jobsModelArrayList = new ArrayList<>();
        jobArrayList=new ArrayList<>();
       /* jobsModelArrayList.add(new JobsModel(R.drawable.worker,  "نبحث عن عامل" , "بناء","عامل")  );
        jobsModelArrayList.add(new JobsModel(R.drawable.chef,  "نبحث عن طباخ" , "مطعم","طباخ")  );
        jobsModelArrayList.add(new JobsModel(R.drawable.cashier,  "نبحث عن كاشير" , "سوبرماركت","كاشير")  );

        */
switch (searchType) {
    case 0:   showJobsDependingOnSearch(); break;
    case 1:   showJobsDependingOnInterest();break;
    case 2:   showJobsDependingOnPosted();break;
}


        ////



    }

    private void showJobsDependingOnPosted() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobsModelArrayList.clear();
                jobType = sharedpreferences.getString("jobType","");
                jobCity= sharedpreferences.getString("jobCity","");
                for (DataSnapshot data : snapshot.getChildren()) {
                    jobType = sharedpreferences.getString("jobType","");
                    Job job = data.getValue(Job.class);


                    if (job.getJobOwner().equals(user.getUsername())){
                        if(job.getJobType().equals("مصنع"))
                            jobsModelArrayList.add(new JobsModel(R.drawable.factory, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                        else if(job.getJobType().equals("مطعم"))
                            jobsModelArrayList.add(new JobsModel(R.drawable.rest_vector, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                        else if(job.getJobType().equals("سوبرماركت") ||job.getJobType().equals("محل البسة"))
                            jobsModelArrayList.add(new JobsModel(R.drawable.clerk, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                        else
                            jobsModelArrayList.add(new JobsModel(R.drawable.work_vector, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));

                        jobArrayList.add(job);
                    }


                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchResultsActivity.this, LinearLayoutManager.VERTICAL, false);

                myRecycler.setLayoutManager(linearLayoutManager);
                jobAdapter = new jobAdapter(searchResultsActivity.this,jobsModelArrayList,searchResultsActivity.this);
                myRecycler.setAdapter(jobAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showJobsDependingOnInterest() {
        database.getReference("JobInterests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    MessageOFIntereset intereset = data.getValue(MessageOFIntereset.class);
                    if(intereset.getSender().equals(user.getUsername())){
                        messageOFInteresetsArrayList.add(intereset);

                    }

                }
                startSecondPhaseForInterest();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startSecondPhaseForInterest() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Job job = data.getValue(Job.class);
                    for(int i =0;i<messageOFInteresetsArrayList.size();i++){
                        if ((job.getJobID().equals(messageOFInteresetsArrayList.get(i).getJobID()))){
                            if(job.getJobType().equals("مصنع"))
                                jobsModelArrayList.add(new JobsModel(R.drawable.factory, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                            else if(job.getJobType().equals("مطعم"))
                                jobsModelArrayList.add(new JobsModel(R.drawable.rest_vector, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                            else if(job.getJobType().equals("سوبرماركت") ||job.getJobType().equals("محل البسة"))
                                jobsModelArrayList.add(new JobsModel(R.drawable.clerk, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                            else
                                jobsModelArrayList.add(new JobsModel(R.drawable.work_vector, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));

                            jobArrayList.add(job);
                        }
                    }

                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchResultsActivity.this, LinearLayoutManager.VERTICAL, false);

                myRecycler.setLayoutManager(linearLayoutManager);
                jobAdapter = new jobAdapter(searchResultsActivity.this,jobsModelArrayList,searchResultsActivity.this);
                myRecycler.setAdapter(jobAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showJobsDependingOnSearch() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobsModelArrayList.clear();
                jobType = sharedpreferences.getString("jobType","");
                jobCity= sharedpreferences.getString("jobCity","");
                Toast.makeText(searchResultsActivity.this, jobType, Toast.LENGTH_SHORT).show();
                for (DataSnapshot data : snapshot.getChildren()) {
                    jobType = sharedpreferences.getString("jobType","");
                    Job job = data.getValue(Job.class);
                    if(job.isAccepted())
                        continue;
                    else if (job.getFreeSpot().equals(jobType) && job.getJobLoc().equals(jobCity)){
                        if(job.getJobType().equals("مصنع"))
                            jobsModelArrayList.add(new JobsModel(R.drawable.factory, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                        else if(job.getJobType().equals("مطعم"))
                            jobsModelArrayList.add(new JobsModel(R.drawable.rest_vector, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                        else if(job.getJobType().equals("سوبرماركت") ||job.getJobType().equals("محل البسة"))
                            jobsModelArrayList.add(new JobsModel(R.drawable.clerk, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));
                        else
                            jobsModelArrayList.add(new JobsModel(R.drawable.work_vector, job.getPostTitle(), job.getJobType(), job.getFreeSpot(),job.getJobOwnerName()));

                        jobArrayList.add(job);
                    }


                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchResultsActivity.this, LinearLayoutManager.VERTICAL, false);

                myRecycler.setLayoutManager(linearLayoutManager);
                jobAdapter = new jobAdapter(searchResultsActivity.this,jobsModelArrayList,searchResultsActivity.this);
                myRecycler.setAdapter(jobAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onJobClick(int pos) {
        Gson gson = new Gson();
        String json = gson.toJson(jobArrayList.get(pos));
        editor.putString("jobApp",json);
        editor.commit();
        if(jobArrayList.get(pos).getJobOwner().equals(user.getUsername()))
        {
            if(!jobArrayList.get(pos).isAccepted())
            {  Intent intent = new Intent(this,lookingForEmp.class);   startActivity(intent);}
            else if(jobArrayList.get(pos).isAccepted())
            {

                    editor.putBoolean("fromCheckPost",true);

                    editor.commit();

                    Intent intent = new Intent(searchResultsActivity.this,jobApplication.class);  startActivity(intent);

                }

        }
        else{

        Intent intent = new Intent(this,jobApplication.class);
        startActivity(intent);}
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(searchResultsActivity.this,MainActivity.class);
        startActivity(intent);

    }
}