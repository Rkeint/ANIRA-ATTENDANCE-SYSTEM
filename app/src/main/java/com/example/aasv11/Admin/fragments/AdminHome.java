package com.example.aasv11.Admin.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aasv11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHome extends Fragment {

    TextView numS, numL, numC;
    DatabaseReference ref;

    public AdminHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        numC = (TextView) view.findViewById(R.id.CourseNum);
        numL = (TextView) view.findViewById(R.id.LecNum);
        numS = (TextView) view.findViewById(R.id.StudNum);

        StudentCount();
        LecturerCount();
        CourseCount();

        return view;
    }

    private void StudentCount(){
        ref = FirebaseDatabase.getInstance().getReference().child("Student");

        ref .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    long n = snapshot.getChildrenCount();
                    numS.setText(String.valueOf(n));
                }else{
                    numS.setText(String.valueOf("0"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void LecturerCount(){
        ref = FirebaseDatabase.getInstance().getReference().child("Lecturer");

        ref .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    long n = snapshot.getChildrenCount();
                    numL.setText(String.valueOf(n));
                }else{
                    numL.setText(String.valueOf("0"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void CourseCount(){
        ref = FirebaseDatabase.getInstance().getReference().child("Classes");

        ref .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    long n = snapshot.getChildrenCount();
                    numC.setText(String.valueOf(n));
                }else{
                    numC.setText(String.valueOf("0"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}