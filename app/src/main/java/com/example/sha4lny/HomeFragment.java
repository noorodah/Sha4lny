package com.example.sha4lny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha4lny.classes.Job;
import com.example.sha4lny.classes.MessageAdapter;
import com.example.sha4lny.classes.MessageModel;
import com.example.sha4lny.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements  MessageAdapter.onChatClickListener{
    // DATABASE REFERENCE
    DatabaseReference myRef;

    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    User user;
    //
    ArrayList<String >contacts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView messageRV;

    // Arraylist for storing data
    private ArrayList<MessageModel> messageModelArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }
public  HomeFragment(String name,String Age, String Loc, String Job){


}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        /// shared preferences
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////
        Gson gson= new Gson();
        String json = sharedpreferences.getString("jobApp","");
        String userJson = sharedpreferences.getString("userClass","");
        user = gson.fromJson(userJson, User.class);

        /// Adjusting views
/// Firebase connection
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sha4lny-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("MessageRequests");
        ///
        messageRV = rootView.findViewById(R.id.messagesRV);
        messageModelArrayList = new ArrayList<>();
        /// To add contacts to chat list
        MessageAdapter messageAdapter = new MessageAdapter(getActivity(),messageModelArrayList,HomeFragment.this::onChatClick);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        contacts = user.getContacts();
    contacts =  removeDuplicates(contacts);
        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    for(int i =0 ; i < contacts.size();i++){
                        if(user.getUsername().equals(contacts.get(i)))
                            messageModelArrayList.add(new MessageModel(user.getUsername(),user.getName(),"ابدأ محادثة"));
                    }
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageRV.setAdapter(messageAdapter);
        messageRV.setLayoutManager(linearLayoutManager);
myRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot data : snapshot.getChildren()) {
            boolean exists = false;
            MessageModel messageModel = data.getValue(MessageModel.class);
            if (messageModel.getReciver().equals(user.getUsername()))
            {

                for (int i =0 ;i<messageModelArrayList.size();i++){
                    if(messageModelArrayList.get(i).getUserName().equals(messageModel.getUserName()))
                    {exists=true; break;}
                }
                if(!exists)
                {
                    boolean existsInContacts = false;
                    for(int i =0;i<contacts.size();i++){
                        if (contacts.get(i).equals(messageModel.getUserName()))
                            existsInContacts = true;
                    }
                    if(!existsInContacts)
                        messageModelArrayList.add(messageModel);
                }
            }


        }
        MessageAdapter messageAdapter = new MessageAdapter(getActivity(),messageModelArrayList,HomeFragment.this::onChatClick);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        messageRV.setAdapter(messageAdapter);
        messageRV.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        // Inflate the layout for this fragment
        return rootView;

    }


    @Override
    public void onChatClick(int position) {
        editor.putString("senderUserName",messageModelArrayList.get(position).getUserName());
        editor.putString("senderName",messageModelArrayList.get(position).getFullName());
        editor.commit();

        Intent intent = new Intent(getActivity(), ChatActivity.class);
       startActivity(intent);
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
}