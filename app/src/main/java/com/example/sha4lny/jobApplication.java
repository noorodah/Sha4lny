package com.example.sha4lny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha4lny.classes.AcceptedJobs;
import com.example.sha4lny.classes.Job;
import com.example.sha4lny.classes.MessageModel;
import com.example.sha4lny.classes.MessageOFIntereset;
import com.example.sha4lny.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class jobApplication extends AppCompatActivity {
    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    boolean interested;
    //
    String sender,senderFullName;
    User user,senderUser;
    ArrayList<String >contacts;
    // DATABASE REFERENCE
    DatabaseReference myRef;
    FirebaseDatabase database;
    String acceptedJobID;
    ////
    TextView txtJobTitle,txtJobType,txtOpenPos,txtJobLoc,txtWorkingHours,txtWorkingTime,txtPaymentMethod,txtSalary,txtComment;
    Button btnShowInterest;

    Job job;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);
/// Firebase connection
      database= FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("MessageRequests");
        ///
        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        ////this to check if this job is accpeted
        MessageOFIntereset mOFIntereset;
        /////
        Gson gson= new Gson();
        String json = sharedpreferences.getString("jobApp","");
        senderFullName ="";
        String userJson = sharedpreferences.getString("userClass","");
        user = gson.fromJson(userJson,User.class);
        job  = gson.fromJson(json,Job.class);
        contacts = user.getContacts();
        /// Adjusting views

        txtComment = findViewById(R.id.txtComment);
        txtJobLoc = findViewById(R.id.txtJobLoc);
        txtJobType = findViewById(R.id.txtJobType);
        txtJobTitle=findViewById(R.id.txtJobTitleApp);
        txtOpenPos = findViewById(R.id.txtOpenJob);
        txtSalary = findViewById(R.id.txtSalary);
        txtWorkingHours = findViewById(R.id.txtWorkingHours);
        txtWorkingTime = findViewById(R.id.txtWorkingTime);
        txtPaymentMethod=findViewById(R.id.txtPaymentMethod);
        btnShowInterest=findViewById(R.id.btnShowInterest);
        /// filling views with Job info
        txtPaymentMethod.setText(job.getPaymentTime());
        txtWorkingHours.setText(" من " + job.getWorkHoursFrom() + " الى " +job.getWorkHourseTo() + " ساعات ");
        txtComment.setText(job.getPostComment());
        txtJobTitle.setText(job.getPostTitle());
        txtJobLoc.setText(job.getJobLoc());
        txtJobType.setText(job.getJobType());
        txtOpenPos.setText(job.getFreeSpot());
        txtSalary.setText(job.getPaymentAmount());
        txtWorkingTime.setText(job.getWorkTime());

        // showing interest in an application
        boolean comingFromChat = sharedpreferences.getBoolean("fromChat",false);
        boolean comingFromMain = sharedpreferences.getBoolean("fromCheckPost",false);
        editor.putBoolean("fromCheckPost",false);
        editor.commit();
        sender = sharedpreferences.getString("interestedPerson","");
        editor.putBoolean("fromChat",false).commit();

        if(comingFromMain)
            ownerSideWorkFromMain();
        else {
            if (comingFromChat)
                ownerSideWork();
            else
                viewSideWork();
        }




    }

    private void ownerSideWorkFromMain() {
      database.getReference("AcceptedJobs").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              for(DataSnapshot data : snapshot.getChildren()){
                  AcceptedJobs acceptedJobs = data.getValue(AcceptedJobs.class);
                  if(job.getJobID().equals(acceptedJobs.getJobID()))
                  {
                      btnShowInterest.setClickable(true);
                      btnShowInterest.setText("تم بالفعل الموافقة على توظيف " + acceptedJobs.getApplicatnFullName() + " انقر هنا للمحادثة معه");
                      btnShowInterest.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {

                              editor.putString("senderUserName", acceptedJobs.getApplicantUserName());
                              editor.putString("senderName", acceptedJobs.getApplicatnFullName());
                              editor.commit();

                              Intent intent = new Intent(jobApplication.this, ChatActivity.class);
                              startActivity(intent);
                          }
                      });
                  }
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }

    private void ownerSideWork() {


            database.getReference("JobInterests").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MessageOFIntereset messageOFIntereset = new MessageOFIntereset();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.getValue(MessageOFIntereset.class).getSender().
                                equals(sender) && data.getValue(MessageOFIntereset.class).
                                getReciver().equals(user.getUsername()) && data.getValue(MessageOFIntereset.class).
                                getJobID().equals(job.getJobID())) {
                            messageOFIntereset = data.getValue(MessageOFIntereset.class);
                        }
                    }

                    if (messageOFIntereset.isAccepted()) {

                                        senderFullName =sharedpreferences.getString("interestedPerson","");
                                        btnShowInterest.setClickable(true);
                                        btnShowInterest.setText("تم بالفعل الموافقة على توظيف " + senderFullName + " انقر هنا للمحادثة معه");
                                        btnShowInterest.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                editor.putString("senderUserName", sender);
                                                editor.putString("senderName", senderFullName);
                                                editor.commit();

                                                Intent intent = new Intent(jobApplication.this, ChatActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                     else {
                        btnShowInterest.setText("الموافقة على التوظيف ");
                        btnShowInterest.setClickable(false);
                        database.getReference("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.getValue(User.class).getUsername().equals(sender))
                                        senderUser = data.getValue(User.class);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (senderUser != null)
                            btnShowInterest.setClickable(true);

                        btnShowInterest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(jobApplication.this)
                                        .setTitle("تاكيد")
                                        .setMessage("هل انت متاكد انك تريد توظيف " + senderUser.getName() + " لهذا العمل؟")

                                        .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("نعم ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        database.getReference("JobInterests").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                MessageOFIntereset tempInt = new MessageOFIntereset();
                                                for (DataSnapshot data : snapshot.getChildren()) {
                                                    if (job.getJobID().equals(data.getValue(MessageOFIntereset.class).getJobID()) && senderUser.getUsername().equals(data.getValue(MessageOFIntereset.class).getSender())) {
                                                        tempInt = data.getValue(MessageOFIntereset.class);
                                                        tempInt.setAccepted(true);
                                                        AcceptedJobs acceptedJobs = new AcceptedJobs(job.getJobID(),user.getUsername(),user.getName(),tempInt.getSender(),tempInt.getSenderFullName());
                                                        database.getReference("AcceptedJobs").push().setValue(acceptedJobs);
                                                        data.getRef().setValue(tempInt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(jobApplication.this, "تمت الموافقة بنجاح", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(jobApplication.this,MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(jobApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        setJobToTrueFirebase();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                })
                                        .setNegativeButton("لا", null).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        editor.putBoolean("fromChat",false).commit();
    }

    private void setJobToTrueFirebase() {
        database.getReference("Jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if(job.getJobID().equals(data.getValue(Job.class).getJobID())){
                        job.setAccepted(true);
                        data.getRef().setValue(job);

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference("JobInterests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    MessageOFIntereset message = data.getValue(MessageOFIntereset.class);
                    if(message.getJobID().equals(job.getJobID())){
                        message.setAcceptedByOther(true);
                        data.getRef().setValue(message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewSideWork() {
        interested=false;

        database.getReference("JobInterests").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MessageOFIntereset messageOFIntereset = new MessageOFIntereset();
                for(DataSnapshot data : snapshot.getChildren()){
                    if(job.getJobID().equals(data.getValue(MessageOFIntereset.class).getJobID()) && job.getJobOwner().equals(data.getValue(MessageOFIntereset.class).getReciver())
                            && user.getUsername().equals(data.getValue(MessageOFIntereset.class).getSender()))
                    {interested=true;
                    messageOFIntereset = data.getValue(MessageOFIntereset.class);
                        break;}
                }
                dealWithButton(interested,messageOFIntereset);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dealWithButton(boolean in,MessageOFIntereset message) {

        if(!interested)
        btnShowInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(jobApplication.this)
                        .setTitle("تاكيد")
                        .setMessage("هل انت متاكد من انك مهتم بهذه الوظيفة؟ اجابتك بنعم سترسل طلب مراسلة لصاحب الاعلان")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                boolean existsInContacts = false;
                                for(int i =0;i<contacts.size();i++){

                                    if(contacts.get(i).equals(job.getJobOwner()))

                                        existsInContacts=true;

                                }
                                if(existsInContacts){
                                    DatabaseReference ref = database.getReference("JobInterests");
                                    ref.push().setValue(new MessageOFIntereset(user.getUsername(),job.getJobID(),job.getPostTitle(),job.getJobOwner(),user.getName(),job.getJobOwnerName(),false));
                                    editor.putString("jobApp",null);
                                    editor.commit();
                                }
                                else {
                                    MessageModel messageModel = new MessageModel();
                                    messageModel.setFullName(user.getName());
                                    messageModel.setUserName(user.getUsername());
                                    messageModel.setLastMessage("مرحبا انا مهتم بوظيفتك ! ");
                                    messageModel.setReciver(job.getJobOwner());
                                    myRef.push().setValue(messageModel);
                                    DatabaseReference ref = database.getReference("JobInterests");
                                    ref.push().setValue(new MessageOFIntereset(user.getUsername(),job.getJobID(),job.getPostTitle(),job.getJobOwner(),user.getName(),job.getJobOwnerName(),false));
                                    editor.putString("jobApp",null);
                                    editor.commit();
                                }
                            }})
                        .setNegativeButton("لا", null).show();
            }
        });
        else {
            if(message.isAccepted())
            {
                btnShowInterest.setClickable(true);
                btnShowInterest.setText("لقد تم الاتفاق على هذا العمل مع الموظف " + message.getRecieverFullName() + " انقر هنا للحديث معه");
                btnShowInterest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("senderUserName",message.getReciver());
                        editor.putString("senderName",message.getRecieverFullName());
                        editor.commit();

                        Intent intent = new Intent(jobApplication.this, ChatActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else {
                btnShowInterest.setClickable(false);
                btnShowInterest.setText("بانتظار الموافقة على طلب اهتمامك");
            }
        }
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(jobApplication.this,MainActivity.class);
        startActivity(intent);

    }
}