package com.example.aasv11.Lecturer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aasv11.Login.Login;
import com.example.aasv11.R;
import com.example.aasv11.database.attendance;
import com.example.aasv11.database.usersdb;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.aasv11.R.id.btnCodeGen;
import static com.example.aasv11.R.id.fbtnSave;
import static com.example.aasv11.R.id.tv_AGC;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallRegister extends Fragment {

    Button gen;
    TextView tvGen, tvCname;
    attendance att;
    usersdb users;
    DatabaseReference ref ,ref1, refid;
    int number;
    long sesscount;
    long maxid1 = 0;
    String thisDate, id;
    List<String> DataList;
    ArrayAdapter<String> adapter;

    public CallRegister() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_register, container, false);

        att = new attendance();
        users = new usersdb();

        gen = (Button) view.findViewById(R.id.btnCodeGen);

        tvGen = (TextView) view.findViewById(R.id.tv_AGC);
        tvCname = (TextView) view.findViewById(R.id.tv_Cname);

        getClassId();

        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
                pushData();
                gen.setEnabled(false);
            }
        });

        return view;
    }


    private void codeGen() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        number = rnd.nextInt(999999);
        tvGen.setText(String.valueOf(number));
        // this will convert any number sequence into 6 character.
    }

    private void getDate(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);

    }

    private void pushData(){
        ref = FirebaseDatabase.getInstance().getReference().child("Attendance");
        att.setCode(String.valueOf(number));
        String name = tvCname.getText().toString().trim();

        ref.child(name).orderByChild("date").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("ONLY ONE CODE CAN BE GENERATED ON A GIVEN DAY");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    codeGen();
                    id = tvCname.getText().toString().trim();
                    ref.child(id).child("Session").child("code").setValue(number);
                    ref.child(id).child("Session").child("date").setValue(thisDate);
                    ref.child(id).child("Session").child("count").setValue(sesscount);
                    ref.child(id).child("Session").child("name").setValue(String.valueOf(id));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getClassId(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            final String email = user.getEmail();
            // Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            ref = FirebaseDatabase.getInstance().getReference();

            Query search = ref.child("Credentials").orderByChild("email").equalTo(email);

            search.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        usersdb users = postSnapshot.getValue(usersdb.class);

                        final int[] id = {users.getID()};

                        ref1 = FirebaseDatabase.getInstance().getReference();

                        Query search1 = ref1.child("Lecturer").orderByChild("id").equalTo(id[0]);

                        search1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    usersdb users = postSnapshot.getValue(usersdb.class);

                                    String cid = users.getClass1();
                                    tvCname.setText(cid);
                                    sessionCount();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void sessionCount(){

        id = tvCname.getText().toString().trim();

        refid = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id).child("Session");
        refid.orderByChild("date").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    sesscount = sc + 1;
                } else {
                    sesscount = 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
