package com.example.sha4lny.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sha4lny.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

public class ChatMessageAdapter  extends RecyclerView.Adapter {
    private List<ChatMessageModel> chatMessageModelList;
    public static final int LayoutOne = 0;
    public static final int LayoutTwo = 1;
    public static final int LayoutThree = 2;
    public ChatMessageAdapter(List<ChatMessageModel> chatMessageModelList) {
        this.chatMessageModelList = chatMessageModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case LayoutOne:
                View layoutOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_message,parent,false);
                return new LayoutOneViewHolder(layoutOne);

            case LayoutTwo:
                View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciever_message,parent,false);
                return new LayoutTwoViewHolder(layoutTwo);
            case LayoutThree :
                View layoutThree = LayoutInflater.from(parent.getContext()).inflate(R.layout.interested,parent,false);
                return new LayoutThreeViewHolder(layoutThree);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (chatMessageModelList.get(position).getViewType()){
            case LayoutOne:
                String senderName = chatMessageModelList.get(position).getSender();
                String message = chatMessageModelList.get(position).getMessage();
                ((LayoutOneViewHolder)holder).setViews(message);
                break;
            case LayoutTwo:
                String rec = chatMessageModelList.get(position).getSender();
                String rmessage = chatMessageModelList.get(position).getMessage();
                ((LayoutTwoViewHolder)holder).setViews(rmessage);

                break;
            case LayoutThree:
                String send = chatMessageModelList.get(position).getSender();
                String inMessage = chatMessageModelList.get(position).getInterestedJobTitle();
                ((LayoutThreeViewHolder)holder).setViews(inMessage,send,chatMessageModelList.get(position).getInterestedJobID(),chatMessageModelList.get(position).getCurrentAct());
                break;
            default:return;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatMessageModelList.get(position).getViewType()){
            case 0:
                return LayoutOne;

            case 1:
                return LayoutTwo;

            case 2:
                return LayoutThree;

            default:
                return -1;

        }
    }
    class LayoutOneViewHolder
            extends RecyclerView.ViewHolder {

        private TextView message;
        private LinearLayout linearLayout;

        public LayoutOneViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Find the Views

           message=itemView.findViewById(R.id.txtSenderMessage);
            linearLayout
                    = itemView.findViewById(R.id.linearlayout);
        }

        // method to set the views that will
        // be used further in onBindViewHolder method.
        private void setViews(String message)
        {

            this.message.setText(message);
        }
    }

    class LayoutTwoViewHolder
            extends RecyclerView.ViewHolder {

      private TextView message;
        private LinearLayout linearLayout;

        public LayoutTwoViewHolder(@NonNull View itemView)
        {
            super(itemView);

            message=itemView.findViewById(R.id.txtMessage);
            linearLayout
                    = itemView.findViewById(R.id.linearlayout);
        }

        private void setViews(
                              String rMessage)
        {

            message.setText(rMessage);
        }
    }
    class LayoutThreeViewHolder

            extends RecyclerView.ViewHolder {
        // Shared prefereces for sessions
        SharedPreferences sharedpreferences;
        SharedPreferences.Editor editor;
        public static final String MyPREFERENCES = "MyPrefs";
        //

        private Button message;
        private LinearLayout linearLayout;

        public LayoutThreeViewHolder(@NonNull View itemView)
        {
            super(itemView);

            /// shared preferences


            /////
            message=itemView.findViewById(R.id.txtInterestMessage);
            linearLayout
                    = itemView.findViewById(R.id.linearlayout);
        }

        private void setViews(
                String rMessage , String sender, String jobID, Context myAc)
        {
            sharedpreferences = myAc.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            message.setText(rMessage);
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /// Firebase connection
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference  myRef = database.getReference("Jobs");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot data : snapshot.getChildren()){
                                if(data.getValue(Job.class).getJobID().equals(jobID)){
                                    Gson gson = new Gson();
                                    String json = gson.toJson(data.getValue(Job.class));
                                    editor.putString("jobApp",json).commit();

                                    break;
                                }
                            }
                            editor.putBoolean("fromChat",true);
                            editor.putString("interestedPerson",sender);

                            editor.commit();
                            Intent intent = new Intent(myAc,com.example.sha4lny.jobApplication.class);
                                 myAc.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    ///

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageModelList.size();
    }
}
