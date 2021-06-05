package com.example.aasv11.Lecturer.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aasv11.Lecturer.Lecturer;
import com.example.aasv11.R;
import com.example.aasv11.database.attendance;
import com.example.aasv11.database.usersdb;
import com.google.android.material.navigation.NavigationView;
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

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LecHome extends Fragment {

    TextView salutations, lecid, teach, stnum, clnum;
    TextView c1, c2, c3, c4, c5;
    private DatabaseReference userRef1, userRef2, refid, ref, reference, absref;
    ArrayAdapter<String> adapter;
    Spinner students;
    long ids;
    String nambar;
    String values, thisDate;
    int id;
    int a=0;
    int b=0;
    int c=0;
    int d=0;
    int e=0;
    int cc;
    int x;

    usersdb users;
    String Class1;

    public LecHome() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lec_home, container, false);

        users = new usersdb();


        salutations = (TextView)  view.findViewById(R.id.txtHi);
        lecid = (TextView)  view.findViewById(R.id.lecid);
        teach = (TextView)  view.findViewById(R.id.tv_teach);
        stnum = (TextView)  view.findViewById(R.id.stunum);
        clnum = (TextView)  view.findViewById(R.id.txtClassNum);
        c1 = (TextView)  view.findViewById(R.id.one);
        c2 = (TextView)  view.findViewById(R.id.two);
        c3 = (TextView)  view.findViewById(R.id.three);
        c4 = (TextView)  view.findViewById(R.id.four);
        c5 = (TextView)  view.findViewById(R.id.five);

        students = (Spinner) view.findViewById(R.id.spinstudents);

        c1.setVisibility(View.INVISIBLE);
        c2.setVisibility(View.INVISIBLE);
        c3.setVisibility(View.INVISIBLE);
        c4.setVisibility(View.INVISIBLE);
        c5.setVisibility(View.INVISIBLE);


        Auth();

        students.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nambar = String.valueOf(students.getSelectedItem());
                if (nambar.equals("Pick a student")){

                }else{
                    getAttendance();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    public void Auth(){

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

            userRef1 = FirebaseDatabase.getInstance().getReference();

            Query search = userRef1.child("Credentials").orderByChild("email").equalTo(email);

            search.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        usersdb users = postSnapshot.getValue(usersdb.class);

                        id = users.getID();

                        lecid.setText(String.valueOf(id));

                        //tvNavName.setText(String.valueOf(id));
                        String studmail = email;

                        userRef2 = FirebaseDatabase.getInstance().getReference();

                        Query search1 = userRef2.child("Lecturer").orderByChild("id").equalTo(id);

                        search1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    usersdb users = postSnapshot.getValue(usersdb.class);

                                    String Fname = users.getFName();
                                    Class1 = users.getClass1();

                                    salutations.setText("Hi there " + Fname +"!");
                                    teach.setText("You have been assigned to teach "+Class1);
                                    studnum();
                                    getStudents();
                                    sessioncount();
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


    private void studnum(){
        refid = FirebaseDatabase.getInstance().getReference();

        String cid = Class1;

        Query firebaseSearchQuery1 = refid.child("Student").orderByChild("class1").equalTo(cid);
        Query firebaseSearchQuery2 = refid.child("Student").orderByChild("class2").equalTo(cid);
        Query firebaseSearchQuery3 = refid.child("Student").orderByChild("class3").equalTo(cid);
        Query firebaseSearchQuery4 = refid.child("Student").orderByChild("class4").equalTo(cid);
        Query firebaseSearchQuery5 = refid.child("Student").orderByChild("class5").equalTo(cid);


        firebaseSearchQuery1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        usersdb users = postSnapshot.getValue(usersdb.class);
                        final String classname = users.getClass1();
                        int id = users.getID();

                        if (classname.isEmpty()) {

                        }else{
                            a = a + 1;
                            c1.setText(String.valueOf(a));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseSearchQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        usersdb users = postSnapshot.getValue(usersdb.class);
                        final String classname = users.getClass2();
                        int id = users.getID();

                        if (classname.isEmpty()) {

                        }else{
                           b =b+1;
                           c2.setText(String.valueOf(b));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseSearchQuery3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        usersdb users = postSnapshot.getValue(usersdb.class);
                        final String classname = users.getClass3();
                        int id = users.getID();

                        if (classname.isEmpty()) {

                        }else{
                            c = c+1;
                            c3.setText(String.valueOf(c));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseSearchQuery4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        usersdb users = postSnapshot.getValue(usersdb.class);
                        final String classname = users.getClass4();
                        int id = users.getID();

                        if (classname.isEmpty()) {

                        }else{
                            d = d + 1;
                            c4.setText(String.valueOf(d));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseSearchQuery5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        usersdb users = postSnapshot.getValue(usersdb.class);
                        final String classname = users.getClass5();
                        int id = users.getID();

                        if (classname.isEmpty()) {

                        }else{
                            e = e+1;
                            c5.setText(String.valueOf(e));
                        }

                    }
                }
                setnum();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void setnum(){

        x=a+b+c+d+e;

        String z = String.valueOf(x);

        stnum.setText("You have "+z+" students enrolled in your class");
    }


    private void getStudents()  {

        final List<String> studClass = new ArrayList<>();
        studClass.add(0,"Pick a student");

        ref = FirebaseDatabase.getInstance().getReference().child("Classes").child(Class1);

        ref.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    ids = childSnapshot.child("stuid").getValue(long.class);
                    values = String.valueOf(ids);
                    studClass.add(values);
                }
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, studClass);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                students.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getAttendance(){

        final String id1 = Class1;

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.orderByChild("name").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    absref = FirebaseDatabase.getInstance().getReference().child("Classes").child(id1).child("Students").child(nambar);

                       absref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                attendance att = snapshot.getValue(attendance.class);
                                long pres = snapshot.child("att").getValue(long.class);

                                long txt = sesscount - pres;
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("ATTENDANCE FOR "+ nambar);
                                alertDialog.setMessage("Number of Absences is " + txt);
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                  dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }else{
                                Toast.makeText(getActivity(),"Hakuna", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("You have not taught any class yet! Attendance can only be checked after classes have started");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getDate(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);
    }


    private void sessioncount() {

        final String id1 = Class1;

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    clnum.setText(String.valueOf("You have taught " + sesscount +" sessions"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}