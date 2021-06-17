package com.example.sha4lny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link srchForJobFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class srchForJobFrag extends Fragment {
Button btnSearch;
Spinner spnrJobType,spnrCity;
    // Shared prefereces for sessions
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public srchForJobFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment srchForJobFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static srchForJobFrag newInstance(String param1, String param2) {
        srchForJobFrag fragment = new srchForJobFrag();
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


        /// shared preferences
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /////

        // Inflate the layout for this fragment
        View rootView=  inflater.inflate(R.layout.fragment_srch_for_job, container, false);
        btnSearch = rootView.findViewById(R.id.btnSrchForJob);
        spnrCity = rootView.findViewById(R.id.spnSrchCity);

        spnrJobType = rootView.findViewById(R.id.spnrSrchJobs);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),R.array.jobs_array,R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrJobType.setAdapter(adapter);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity(),R.array.cities_array,R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnrCity.setAdapter(adapter1);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jobType = spnrJobType.getSelectedItem().toString();
                editor.putInt("searchType",0).commit();
                editor.putString("jobType",jobType);

                editor.putString("jobCity",spnrCity.getSelectedItem().toString());
                editor.commit();
                Intent intent = new Intent(getActivity(),searchResultsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}