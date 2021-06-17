package com.example.sha4lny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link srchForEmpFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class srchForEmpFrag extends Fragment {
Button btnPostAJob,btnShowPostedJobs,btnShowInterestedJobs;
    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";
    //

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public srchForEmpFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment srchForEmpFrag.
     */

    // TODO: Rename and change types and number of parameters
   public static srchForEmpFrag newInstance(String param1, String param2) {
        srchForEmpFrag fragment = new srchForEmpFrag();
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
        // Inflate the layout for this fragment
         View rootView= inflater.inflate(R.layout.fragment_srch_for_emp, container, false);
         btnPostAJob = rootView.findViewById(R.id.btnPostAJob);
         btnShowInterestedJobs=rootView.findViewById(R.id.btnCheckInterestJobs);
         btnShowPostedJobs=rootView.findViewById(R.id.btnCheckPostedJobs);
        /// shared preferences
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////
         btnShowInterestedJobs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 editor.putInt("searchType",1).commit();
                 Intent intent= new Intent(getActivity(),searchResultsActivity.class);
                 startActivity(intent);
             }
         });
         btnShowPostedJobs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 editor.putInt("searchType",2).commit();
                 Intent intent= new Intent(getActivity(),searchResultsActivity.class);
                 startActivity(intent);
             }
         });
         btnPostAJob.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 editor.putString("jobApp",null).commit();
                 Intent intent = new Intent(getActivity(),lookingForEmp.class);
                 startActivity(intent);
             }
         });
        return rootView;
    }
}