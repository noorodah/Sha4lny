package com.example.sha4lny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha4lny.classes.ChatMessageAdapter;
import com.example.sha4lny.classes.ChatMessageModel;
import com.example.sha4lny.classes.MessageOFIntereset;
import com.example.sha4lny.classes.Messages;
import com.example.sha4lny.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    //Recycle view things
RecyclerView recyclerView;
List<ChatMessageModel> chatMessageModelList;
/////
    ///// to check if in contacts
    boolean isInContacts;
/////


TextView toolbarTitle;
EditText edtChatText;
ImageView imgSend;
    Toolbar toolbar;


    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";
    //
    ChatMessageAdapter adapter;

    // Username and user class
    String senderUserName,senderFullName;
    User userClass,senderUserClass;
    ///

    boolean state;
    // DATABASE REFERENCE
    DatabaseReference myRef;
    FirebaseDatabase database;
    //
    ArrayList<String>Contacts;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /// Firebase connection
      database  = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Messages");
        ///


        /// shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////

        isInContacts=false;



        /// checking if user is logged in
        state = sharedpreferences.getBoolean("logged", false);
        if (!state) {
            Intent intent = new Intent(ChatActivity.this, com.example.sha4lny.login.loginActivity.class);
            startActivity(intent);
        }
        ///


        ///// To use already passed user class
        Gson gson= new Gson();
        String userJson = sharedpreferences.getString("userClass","");
        userClass = gson.fromJson(userJson,User.class);
        /////// getting info from shared preferences
        senderFullName = sharedpreferences.getString("senderName","");
        senderUserName= sharedpreferences.getString("senderUserName","");
        for(int i =0 ; i < userClass.getContacts().size();i++){
            if(userClass.getContacts().get(i).equals(senderUserName))
                isInContacts=true;
        }

        if(!isInContacts)
        {
            new AlertDialog.Builder(this)
                    .setTitle("تاكيد")
                    .setMessage("هذا الشخص غير مضاف لقائمة معارفك اذا كنت تود السماح له بمراسلتك اضغط نعم حتى يتم اضافته لقائمة معارفك, علما بانه لن يتمكن الطرفان من المحادثة مالم يكونا بقائمة المعارف المشتركة.")

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> temp= userClass.getContacts();
                            temp.add(senderUserName);

                            userClass.setContacts(temp);
                            database.getReference("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot data : snapshot.getChildren()){
                                        User user = data.getValue(User.class);
                                        if(user.getUsername().equals(userClass.getUsername()))
                                        {
                                            data.getRef().setValue(userClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(ChatActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        if(user.getUsername().equals(senderUserName)){
                                            ArrayList<String> SenderTemp = user.getContacts();
                                            SenderTemp.add(userClass.getUsername());
                                            user.setContacts(SenderTemp);
                                            data.getRef().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(ChatActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
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
                    }).setNegativeButton("لا",null).show();
        }

        /// Toolbar work



  toolbar = findViewById(R.id.chatToolbar);
  setSupportActionBar(toolbar);
  if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayShowHomeEnabled(true);
/////
getSupportActionBar().setTitle("");

        /// adjusting views
        edtChatText=findViewById(R.id.edtMessage);

        imgSend=findViewById(R.id.imgSend);
        imgSend.setOnClickListener(this);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(senderFullName);
        recyclerView = findViewById(R.id.chatRecycler);
        ////




        // Linear Layout manager
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);


        ////

        recyclerView.setLayoutManager(layoutManager);
        chatMessageModelList = new ArrayList<>();
   adapter   = new ChatMessageAdapter(chatMessageModelList);
        recyclerView.setAdapter(adapter);

        adjustMessagesFromFirebase();
    }
// Firebase work goes over here

    private void adjustMessagesFromFirebase() {



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // You need to clear the arraylist each time the list is updated so you won't have duplicates
                chatMessageModelList.clear();
                /// this to show job interests coming from the other side of the chat
                database.getReference("JobInterests").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            MessageOFIntereset messageOFIntereset = data.getValue(MessageOFIntereset.class);

                            if(messageOFIntereset.getReciver().equals(userClass.getUsername()))
                                if(!messageOFIntereset.isAccepted())
                                chatMessageModelList.add(new ChatMessageModel(messageOFIntereset.getJobTitle(),messageOFIntereset.getJobID(),ChatMessageModel.LayoutThree,messageOFIntereset.getSender(),ChatActivity.this));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                /// here we get the messages
                if(isInContacts)
                for(DataSnapshot data : snapshot.getChildren()){
                    Messages messages = data.getValue(Messages.class);
                    if(messages.getSender().equals(senderUserName)&& messages.getReciever().equals(userClass.getUsername())){
                        chatMessageModelList.add(new ChatMessageModel(ChatMessageModel.LayoutOne,messages.getSender(),messages.getMessage()));
                    }
                    else if (messages.getSender().equals(userClass.getUsername()) && messages.getReciever().equals(senderUserName)){
                        chatMessageModelList.add(new ChatMessageModel(ChatMessageModel.LayoutTwo,messages.getSender(),messages.getMessage()));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if(edtChatText.getText().toString().trim().isEmpty()){}
        else{
            if(isInContacts)
            {Messages messages = new Messages(userClass.getUsername(),senderUserName,edtChatText.getText().toString());
            edtChatText.setText("");
            myRef.push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  //  chatMessageModelList.add(new ChatMessageModel(ChatMessageModel.LayoutTwo,userClass.getUsername(),messages.getMessage()));
                   // adapter.notifyDataSetChanged();
                }
            });}
            else{
                new AlertDialog.Builder(this)
                        .setTitle("تاكيد")
                        .setMessage("هذا الشخص غير مضاف لقائمة معارفك اذا كنت تود السماح له بمراسلتك اضغط نعم حتى يتم اضافته لقائمة معارفك, علما بانه لن يتمكن الطرفان من المحادثة مالم يكونا بقائمة المعارف المشتركة.")

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<String> temp= userClass.getContacts();
                                temp.add(senderUserName);

                                userClass.setContacts(temp);
                                database.getReference("Users").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot data : snapshot.getChildren()){
                                            User user = data.getValue(User.class);
                                            if(user.getUsername().equals(userClass.getUsername()))
                                            {
                                                data.getRef().setValue(userClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(ChatActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            if(user.getUsername().equals(senderUserName)){
                                                ArrayList<String> SenderTemp = user.getContacts();
                                                SenderTemp.add(userClass.getUsername());
                                                user.setContacts(SenderTemp);
                                                data.getRef().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(ChatActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
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
                        }).setNegativeButton("لا",null).show();
            }
        }
    }
}