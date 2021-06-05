package com.example.aasv11.Admin.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aasv11.Lecturer.Lecturer;
import com.example.aasv11.database.credentialsdb;
import com.example.aasv11.database.usersdb;
import com.example.aasv11.R;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddUser extends Fragment {

    EditText f_name, m_name, l_name, ID, email, pass, cname1, cname2, cname3, cname4, cname5;
    TextView tf_name, t_mname, t_lname, t_id, t_email, t_pass, t_gen, t_type, c1, c2, c3, c4, c5;
    TextView c_c1, c_c2, c_c3, c_c4, c_c5;
    RadioButton m, f, stud, lec;
    RadioGroup gengrp, typegrp;
    FloatingActionButton save, delete, search, update, clear;
    Switch editmode;


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button yes, no;


    DatabaseReference ref, refsave, refdel, refemail;
    private FirebaseAuth mAuth;
    FirebaseDatabase fdb;
    usersdb users;
    credentialsdb cred;

    String g, c, thisDate;

    int n, n1, n2, n3, n4, n5, n6, n7;
    int s, s1, s2, s3, s4;
    int a, a1, a2, a3, a4, a5;
    int e=0;
    int up1=0, up2=0, up3=0, up4=0, up5=0, up;

    ConstraintLayout v;

    long maxidc1 = 0;
    long maxidc2 = 0;
    long maxidc3 = 0;
    long maxidc4 = 0;
    long maxidc5 = 0;

    Spinner spinnerCourse1, spinnerCourse2, spinnerCourse3, spinnerCourse4, spinnerCourse5;
    ArrayAdapter<String> adapter1, adapter2, adapter3, adapter4, adapter5;
    ArrayList<String> spinnerDataList1, spinnerDataList2, spinnerDataList3, spinnerDataList4, spinnerDataList5;

    String spinnerValue1, spinnerValue2, spinnerValue3, spinnerValue4, spinnerValue5;
    private static final String USER = "Credentials";
    private static final String TAG = "Add_User";

    String email1, pass1;

    public AddUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_add_user, container, false);

        View view = inflater.inflate(R.layout.fragment_add_user, container, false);


        save = (FloatingActionButton) view.findViewById(R.id.fbtnSave);
        delete = (FloatingActionButton) view.findViewById(R.id.fbtnDelete);
        search = (FloatingActionButton) view.findViewById(R.id.fbtnSearch);
        update = (FloatingActionButton) view.findViewById(R.id.fbtnUpdate);
        clear = (FloatingActionButton) view.findViewById(R.id.fbtnClear);

        editmode = (Switch) view.findViewById(R.id.switchEdit);

        v = (ConstraintLayout) view.findViewById(R.id.constrainLayout);

        clear.setEnabled(false);
        delete.setEnabled(false);
        update.setEnabled(false);
        save.setEnabled(false);
        search.setEnabled(false);

        Toast.makeText(getActivity(), "For search enter ID and User class", Toast.LENGTH_LONG).show();

        dialogBuilder = new AlertDialog.Builder(getActivity());

        dialog = dialogBuilder.create();

        ID = (EditText) view.findViewById(R.id.txtId);
        f_name = (EditText) view.findViewById(R.id.txtFname);
        l_name = (EditText) view.findViewById(R.id.txtLname);
        m_name = (EditText) view.findViewById(R.id.txtMname);
        email = (EditText) view.findViewById(R.id.txtEmail);
        pass = (EditText) view.findViewById(R.id.txtPass);
        cname1 = (EditText) view.findViewById(R.id.txtClass1);
        cname2 = (EditText) view.findViewById(R.id.txtClass2);
        cname3 = (EditText) view.findViewById(R.id.txtClass3);
        cname4 = (EditText) view.findViewById(R.id.txtClass4);
        cname5 = (EditText) view.findViewById(R.id.txtClass5);

        email.setEnabled(false);

        t_id = (TextView) view.findViewById(R.id.tvID);
        tf_name = (TextView) view.findViewById(R.id.tvFname);
        t_mname = (TextView) view.findViewById(R.id.tvMname);
        t_lname = (TextView) view.findViewById(R.id.tvLname);
        t_email = (TextView) view.findViewById(R.id.tvEmail);
        t_pass = (TextView) view.findViewById(R.id.tvPass);
        t_gen = (TextView) view.findViewById(R.id.tvGender);
        t_type = (TextView) view.findViewById(R.id.tvUserType);
        c1 = (TextView) view.findViewById(R.id.tvC1);
        c2 = (TextView) view.findViewById(R.id.tvC2);
        c3 = (TextView) view.findViewById(R.id.tvC3);
        c4 = (TextView) view.findViewById(R.id.tvC4);
        c5 = (TextView) view.findViewById(R.id.tvC5);

        c_c1 = (TextView) view.findViewById(R.id.tv_Course1);
        c_c2 = (TextView) view.findViewById(R.id.tv_Course2);
        c_c3 = (TextView) view.findViewById(R.id.tv_Course3);
        c_c4 = (TextView) view.findViewById(R.id.tv_Course4);
        c_c5 = (TextView) view.findViewById(R.id.tv_Course5);

        gengrp = (RadioGroup) view.findViewById(R.id.Gender);
        typegrp = (RadioGroup) view.findViewById(R.id.Type);

        m = (RadioButton) view.findViewById(R.id.rbtnM);
        f = (RadioButton) view.findViewById(R.id.rbtnF);
        stud = (RadioButton) view.findViewById(R.id.rbtnStud);
        lec = (RadioButton) view.findViewById(R.id.rbtnLec);

        spinnerCourse1 = (Spinner) view.findViewById(R.id.spC1);
        spinnerCourse2 = (Spinner) view.findViewById(R.id.spC2);
        spinnerCourse3 = (Spinner) view.findViewById(R.id.spC3);
        spinnerCourse4 = (Spinner) view.findViewById(R.id.spC4);
        spinnerCourse5 = (Spinner) view.findViewById(R.id.spC5);

        users = new usersdb();
        cred = new credentialsdb();

        spinnerset();
        hideFields();


        l_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (l_name.getText().toString().isEmpty()) {

                } else {
                    if (f_name.getText().toString().isEmpty()) {

                    } else {
                        String pos1 = f_name.getText().toString().toLowerCase().substring(0, 1);
                        email.setText(pos1 + l_name.getText().toString().toLowerCase() + "@usiu.ac.ke");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        IDcheck();


        gengrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (m.isChecked()) {
                    g = ("Male");
                } else if (f.isChecked()) {
                    g = ("Female");
                } else {
                    m.setChecked(false);
                    f.setChecked(false);
                }
            }

        });

        typegrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (stud.isChecked()) {
                    c = "Student";

                    spinnerCourse1.setVisibility(View.VISIBLE);
                    spinnerCourse2.setVisibility(View.VISIBLE);
                    spinnerCourse3.setVisibility(View.VISIBLE);
                    spinnerCourse4.setVisibility(View.VISIBLE);
                    spinnerCourse5.setVisibility(View.VISIBLE);
                    cname1.setVisibility(View.INVISIBLE);

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c5.setVisibility(View.VISIBLE);

                } else if (lec.isChecked()) {
                    c = "Lecturer";

                    s=4;

                    spinnerCourse1.setVisibility(View.INVISIBLE);
                    spinnerCourse2.setVisibility(View.INVISIBLE);
                    spinnerCourse3.setVisibility(View.INVISIBLE);
                    spinnerCourse4.setVisibility(View.INVISIBLE);
                    spinnerCourse5.setVisibility(View.INVISIBLE);
                    cname1.setVisibility(View.INVISIBLE);

                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c3.setVisibility(View.INVISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    c5.setVisibility(View.INVISIBLE);

                } else {
                    stud.setChecked(false);
                    lec.setChecked(false);
                }
            }

        });

        editmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){

                    v.setBackground(getActivity().getDrawable(R.drawable.mac_os_dark));

                    lec.setEnabled(false);
                    stud.setEnabled(false);
                    ID.setEnabled(false);

                    if (stud.isChecked()) {

                        spinnerCourse1.setVisibility(View.VISIBLE);
                        spinnerCourse2.setVisibility(View.VISIBLE);
                        spinnerCourse3.setVisibility(View.VISIBLE);
                        spinnerCourse4.setVisibility(View.VISIBLE);
                        spinnerCourse5.setVisibility(View.VISIBLE);

                        cname1.setVisibility(View.INVISIBLE);
                        cname2.setVisibility(View.INVISIBLE);
                        cname3.setVisibility(View.INVISIBLE);
                        cname4.setVisibility(View.INVISIBLE);
                        cname5.setVisibility(View.INVISIBLE);

                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c3.setVisibility(View.VISIBLE);
                        c4.setVisibility(View.VISIBLE);
                        c5.setVisibility(View.VISIBLE);

                        c_c1.setVisibility(View.VISIBLE);
                        c_c2.setVisibility(View.VISIBLE);
                        c_c3.setVisibility(View.VISIBLE);
                        c_c4.setVisibility(View.VISIBLE);
                        c_c5.setVisibility(View.VISIBLE);

                        color1();

                        clear.setEnabled(true);
                        if (n == 7) {
                            delete.setEnabled(true);
                            update.setEnabled(true);
                        }
                        save.setEnabled(false);

                    }else if (lec.isChecked()){

                        spinnerCourse1.setVisibility(View.INVISIBLE);
                        spinnerCourse2.setVisibility(View.INVISIBLE);
                        spinnerCourse3.setVisibility(View.INVISIBLE);
                        spinnerCourse4.setVisibility(View.INVISIBLE);
                        spinnerCourse5.setVisibility(View.INVISIBLE);

                        cname1.setVisibility(View.VISIBLE);
                        cname2.setVisibility(View.INVISIBLE);
                        cname3.setVisibility(View.INVISIBLE);
                        cname4.setVisibility(View.INVISIBLE);
                        cname5.setVisibility(View.INVISIBLE);

                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);
                        c3.setVisibility(View.INVISIBLE);
                        c4.setVisibility(View.INVISIBLE);
                        c5.setVisibility(View.INVISIBLE);

                        c_c1.setVisibility(View.INVISIBLE);
                        c_c2.setVisibility(View.INVISIBLE);
                        c_c3.setVisibility(View.INVISIBLE);
                        c_c4.setVisibility(View.INVISIBLE);
                        c_c5.setVisibility(View.INVISIBLE);

                        color1();

                        clear.setEnabled(true);
                        if (n == 7) {
                            delete.setEnabled(true);
                            update.setEnabled(true);
                        }
                        save.setEnabled(false);
                    }

                }else{

                    v.setBackground(getActivity().getDrawable(R.drawable.mac_os_light));

                    lec.setEnabled(true);
                    stud.setEnabled(true);
                    ID.setEnabled(true);

                    spinnerCourse1.setVisibility(View.INVISIBLE);
                    spinnerCourse2.setVisibility(View.INVISIBLE);
                    spinnerCourse3.setVisibility(View.INVISIBLE);
                    spinnerCourse4.setVisibility(View.INVISIBLE);
                    spinnerCourse5.setVisibility(View.INVISIBLE);

                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c3.setVisibility(View.INVISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    c5.setVisibility(View.INVISIBLE);

                    c_c1.setVisibility(View.INVISIBLE);
                    c_c2.setVisibility(View.INVISIBLE);
                    c_c3.setVisibility(View.INVISIBLE);
                    c_c4.setVisibility(View.INVISIBLE);
                    c_c5.setVisibility(View.INVISIBLE);

                    color1();
                    if (n==7){
                        cname1.setVisibility(View.VISIBLE);
                        cname2.setVisibility(View.VISIBLE);
                        cname3.setVisibility(View.VISIBLE);
                        cname4.setVisibility(View.VISIBLE);
                        cname5.setVisibility(View.VISIBLE);
                    }else{
                        cname1.setVisibility(View.INVISIBLE);
                        cname2.setVisibility(View.INVISIBLE);
                        cname3.setVisibility(View.INVISIBLE);
                        cname4.setVisibility(View.INVISIBLE);
                        cname5.setVisibility(View.INVISIBLE);
                    }
                    IDcheck();
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color();
                if (n == 7){
                    if(pass.getText().toString().trim().length()<6){
                        Toast.makeText(getActivity(), "Password too short! Min length is 6", Toast.LENGTH_SHORT).show();
                    }else {
                        if (stud.isChecked()) {
                            spinnerValueCount();
                            if (s == 4) {
                                saveuser();
                            } else {
                                Toast.makeText(getActivity(), "A user cannot register for the same class more than once", Toast.LENGTH_SHORT).show();
                            }
                        }else if(lec.isChecked()){
                            saveuser();
                        }
                    }
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("ERROR!");
                    alertDialog.setMessage("PLEASE FILL IN THE REQUIRED FIELDS");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color1();
                if (n == 7) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("DELETE");
                    alertDialog.setMessage("Are you sure you want to delete " + f_name.getText().toString().toUpperCase() + " from the records?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteuser();
                                    deleteCredentials();
                                    clearSelection();
                                    Toast.makeText(getActivity(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    delete.setEnabled(false);
                                    update.setEnabled(false);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("Screen data is not the same as database data!" +
                            "Make sure data that was retrieved was not tampered with!");
                }
            }

        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelection();
                hideFields();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideForSearch();
                retrieveUser();
                clear.setEnabled(true);
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
                updatePass_Email();
            }
        });
        return view;
    }


    private void saveuser() {


        if (c == "Student") {


            final int id = Integer.parseInt(ID.getText().toString().trim());
            refsave = FirebaseDatabase.getInstance().getReference().child("Student");
            ref = FirebaseDatabase.getInstance().getReference();

            refsave.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
                    }else {
                        email1 = email.getText().toString().trim();
                        refemail = FirebaseDatabase.getInstance().getReference().child("Credentials");
                        refemail.orderByChild("email").equalTo(email1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    email.setEnabled(true);
                                    Toast.makeText(getActivity(),"This email already exists, try adding a number", Toast.LENGTH_SHORT).show();
                                   // email1 = email.getText().toString().trim();
                                    //saveuserCredentials(email1, pass1);

                                } else {
                                    users.setFName(f_name.getText().toString().trim());
                                    users.setMName(m_name.getText().toString().trim());
                                    users.setLName(l_name.getText().toString().trim());
                                    email1 = email.getText().toString().trim();
                                    pass1 = pass.getText().toString().trim();

                                    users.setID(id);
                                    users.setGender(g);
                                    users.setUser_Class("Student");

                                    refsave.child(String.valueOf(id)).setValue(users);

                                    if (spinnerValue1 != null) {
                                        ref.child("Classes").child(String.valueOf(spinnerValue1)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                        ref.child("Classes").child(String.valueOf(spinnerValue1)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                        ref.child("Classes").child(String.valueOf(spinnerValue1)).child("Students").child(String.valueOf(id)).child("date").setValue("0");

                                    }
                                    if (spinnerValue2 != null) {
                                        ref.child("Classes").child(String.valueOf(spinnerValue2)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                        ref.child("Classes").child(String.valueOf(spinnerValue2)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                        ref.child("Classes").child(String.valueOf(spinnerValue2)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                    }
                                    if (spinnerValue3 != null) {
                                        ref.child("Classes").child(String.valueOf(spinnerValue3)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                        ref.child("Classes").child(String.valueOf(spinnerValue3)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                        ref.child("Classes").child(String.valueOf(spinnerValue3)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                    }
                                    if (spinnerValue4 != null) {
                                        ref.child("Classes").child(String.valueOf(spinnerValue4)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                        ref.child("Classes").child(String.valueOf(spinnerValue4)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                        ref.child("Classes").child(String.valueOf(spinnerValue4)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                    }
                                    if (spinnerValue5 != null) {
                                        ref.child("Classes").child(String.valueOf(spinnerValue5)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                        ref.child("Classes").child(String.valueOf(spinnerValue5)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                        ref.child("Classes").child(String.valueOf(spinnerValue5)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                    }

                                    Toast.makeText(getActivity(), "Student: " + f_name.getText().toString() + " added successfully", Toast.LENGTH_LONG).show();
                                    saveuserCredentials(email1, pass1);
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
        } else if (c == "Lecturer") {

            final int id = Integer.parseInt(ID.getText().toString().trim());
            refsave = FirebaseDatabase.getInstance().getReference().child("Lecturer");

            refsave.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        email1 = email.getText().toString().trim();
                        ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("Credentials").orderByChild("email").equalTo(email1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    email.setEnabled(true);
                                    Toast.makeText(getActivity(),"This email already exists, try adding a number", Toast.LENGTH_SHORT).show();
                                   // email1 = email.getText().toString().trim();
                                    //saveuserCredentials(email1, pass1);
                                }else{
                                    refsave.child(String.valueOf(id)).child("fname").setValue(f_name.getText().toString().trim());
                                    refsave.child(String.valueOf(id)).child("mname").setValue(m_name.getText().toString().trim());
                                    refsave.child(String.valueOf(id)).child("lname").setValue(l_name.getText().toString().trim());
                                    email1 = email.getText().toString().trim();
                                    pass1 = pass.getText().toString().trim();
                                    refsave.child(String.valueOf(id)).child("id").setValue(id);
                                    refsave.child(String.valueOf(id)).child("gender").setValue(g);
                                    refsave.child(String.valueOf(id)).child("user_Class").setValue("Lecturer");
                                    refsave.child(String.valueOf(id)).child("class1").setValue("");

                                    Toast.makeText(getActivity(), "Lecturer: " + f_name.getText().toString() + " added successfully", Toast.LENGTH_LONG).show();
                                    saveuserCredentials(email1, pass1);
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


    private void saveuserCredentials(String email1, String pass1){

        int id = Integer.parseInt(ID.getText().toString());

        fdb = FirebaseDatabase.getInstance();
        refsave = fdb.getReference().child(USER);

        mAuth = FirebaseAuth.getInstance();


        mAuth.createUserWithEmailAndPassword(email1, pass1)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        //display some message here
                        if (task.isSuccessful()) {
                            //display some message here
                            //Toast.makeText(getActivity(), "Successfully registered", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(getActivity(), "Registration Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        cred.setID(id);
        cred.setEmail(email.getText().toString().trim());
        computeMD5Hash(pass.toString());
        cred.setPass(pass.getText().toString().trim());
        cred.setUser_Class(c);

        refsave.child(String.valueOf(id)).setValue(cred);
    }


    private void updateDetails(){

            if (c == "Student") {


                final int id = Integer.parseInt(ID.getText().toString().trim());
                refsave = FirebaseDatabase.getInstance().getReference().child("Student");
                ref = FirebaseDatabase.getInstance().getReference();

                refsave.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("CONFIRM");
                            alertDialog.setMessage("Are you sure you want to change these details?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            refsave.child(String.valueOf(id)).child("fname").setValue(f_name.getText().toString().trim());
                                            refsave.child(String.valueOf(id)).child("mname").setValue(m_name.getText().toString().trim());
                                            refsave.child(String.valueOf(id)).child("lname").setValue(l_name.getText().toString().trim());
                                            email1 = email.getText().toString().trim();
                                            pass1 = pass.getText().toString().trim();
                                            //refsave.child(String.valueOf(id)).child("id").setValue(id);
                                            refsave.child(String.valueOf(id)).child("gender").setValue(g);
                                            refsave.child(String.valueOf(id)).child("user_Class").setValue("Student");

                                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                            alertDialog.setTitle("CHANGE CLASSES?");
                                            alertDialog.setMessage("Note the user's attendance will be reset when this is done, Do you wish to continue? \n\nPlease select NO if you have not made any changes to the classes");
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            spinnerValueCount();
                                                            if (s == 4) {

                                                                String class1= c_c1.getText().toString();
                                                            if (class1 != spinnerValue1){
                                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                                alertDialog.setTitle("CONFIRM");
                                                                alertDialog.setMessage("Swap "+class1+" with "+ spinnerValue1+ "?");
                                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                refsave.child(String.valueOf(id)).child("class1").setValue(spinnerValue1);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue1)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue1)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue1)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                                                                up1=1;
                                                                            }

                                                                        });
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                                up1=1;
                                                                            }
                                                                        });
                                                                alertDialog.show();
                                                            }
                                                            if (c_c2.getText().toString() != spinnerValue2){
                                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                                alertDialog.setTitle("CONFIRM");
                                                                alertDialog.setMessage("Swap "+c_c2.getText().toString()+" with "+ spinnerValue2+ "?");
                                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                refsave.child(String.valueOf(id)).child("class2").setValue(spinnerValue2);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue2)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue2)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue2)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                                                                up2=1;
                                                                            }

                                                                        });
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                up2=1;
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                alertDialog.show();

                                                            }
                                                            if (c_c3.getText().toString() != spinnerValue3){
                                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                                alertDialog.setTitle("CONFIRM");
                                                                alertDialog.setMessage("Swap "+c_c3.getText().toString()+" with "+ spinnerValue3+ "?");
                                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                refsave.child(String.valueOf(id)).child("class3").setValue(spinnerValue3);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue3)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue3)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue3)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                                                                up3=1;
                                                                            }

                                                                        });
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                up3=1;
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                alertDialog.show();

                                                            }
                                                            if (c_c4.getText().toString() != spinnerValue4){
                                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                                alertDialog.setTitle("CONFIRM");
                                                                alertDialog.setMessage("Swap "+c_c4.getText().toString()+" with "+ spinnerValue4+ "?");
                                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                refsave.child(String.valueOf(id)).child("class4").setValue(String.valueOf(spinnerValue4));
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue4)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue4)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue4)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                                                                up4=1;
                                                                            }

                                                                        });
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                up4=1;
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                alertDialog.show();

                                                            }
                                                            if (c_c5.getText().toString() != spinnerValue5){
                                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                                alertDialog.setTitle("CONFIRM");
                                                                alertDialog.setMessage("Swap "+c_c5.getText().toString()+" with "+ spinnerValue5+ "?");
                                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                refsave.child(String.valueOf(id)).child("class5").setValue(String.valueOf(spinnerValue5));
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue5)).child("Students").child(String.valueOf(id)).child("att").setValue(0);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue5)).child("Students").child(String.valueOf(id)).child("stuid").setValue(id);
                                                                                ref.child("Classes").child(String.valueOf(spinnerValue5)).child("Students").child(String.valueOf(id)).child("date").setValue("0");
                                                                                up5=1;
                                                                            }

                                                                        });
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                up5=1;
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                alertDialog.show();
                                                                  }

                                                            up = up1+up2+up3+up4+up5;
                                                            if (up==5) {
                                                                Toast.makeText(getActivity(), "Successfully Updated Details & Classes for " + f_name.getText().toString().toUpperCase(), Toast.LENGTH_LONG).show();
                                                                up=0;
                                                            }
                                                            }else {
                                                                Toast.makeText(getActivity(), "A student cannot be assigned to the same class more than once", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Toast.makeText(getActivity(), "Successfully Updated Details for "+f_name.getText().toString().toUpperCase(), Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                    });

                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
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
            } else if (c == "Lecturer") {

                final int id = Integer.parseInt(ID.getText().toString().trim());
                refsave = FirebaseDatabase.getInstance().getReference().child("Lecturer");

                refsave.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("CONFIRM");
                            alertDialog.setMessage("Are you sure you want to change these details?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            refsave.child(String.valueOf(id)).child("fname").setValue(f_name.getText().toString().trim());
                                            refsave.child(String.valueOf(id)).child("mname").setValue(m_name.getText().toString().trim());
                                            refsave.child(String.valueOf(id)).child("lname").setValue(l_name.getText().toString().trim());
                                            email1 = email.getText().toString().trim();
                                            pass1 = pass.getText().toString().trim();
                                            //refsave.child(String.valueOf(id)).child("id").setValue(id);
                                            refsave.child(String.valueOf(id)).child("gender").setValue(g);
                                            refsave.child(String.valueOf(id)).child("user_Class").setValue("Lecturer");
                                            refsave.child(String.valueOf(id)).child("class1").setValue("");

                                            //refsave.child(String.valueOf(id)).setValue(users);
                                            Toast.makeText(getActivity(), "Successfully Updated Details for " + f_name.getText().toString().toUpperCase(), Toast.LENGTH_LONG).show();

                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
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
    }


    private void updatePass_Email(){

    }


    private void deleteuser(){


            if (c == "Student") {

                    refdel = FirebaseDatabase.getInstance().getReference();

                    int id = Integer.parseInt(ID.getText().toString());

                    Query firebaseSearchQuery = refdel.child("Student").orderByChild("id").equalTo(id);


                    firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                usersdb users = postSnapshot.getValue(usersdb.class);

                                postSnapshot.getRef().removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();

                        }
                    });



            } else if (c == "Lecturer") {

                int id = Integer.parseInt(ID.getText().toString());

                ref = FirebaseDatabase.getInstance().getReference();

                Query firebaseSearchQuery = ref.child("Lecturer").orderByChild("id").equalTo(id);


                firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            //usersdb users = postSnapshot.getValue(usersdb.class);

                            postSnapshot.getRef().removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();

                    }
                });
            }

    }


    private void deleteCredentials(){
        int id = Integer.parseInt(ID.getText().toString());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery = ref.child("Credentials").orderByChild("id").equalTo(id);


        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                   // usersdb users = postSnapshot.getValue(usersdb.class);

                    postSnapshot.getRef().removeValue();

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void clearSelection(){
        ID.setText("");
        f_name.setText("");
        m_name.setText("");
        l_name.setText("");
        email.setText("");
        pass.setText("");
        gengrp.clearCheck();
        typegrp.clearCheck();


    }


    private void retrieveUser(){
        if (c == "Student") {

            int id = Integer.parseInt(ID.getText().toString());

            ref = FirebaseDatabase.getInstance().getReference();

            Query firebaseSearchQuery = ref.child("Student").orderByChild("id").equalTo(id);


            firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            usersdb users = postSnapshot.getValue(usersdb.class);

                            String Fname = users.getFName();
                            String Mname = users.getMName();
                            String Lname = users.getLName();
                            String Cname1 = users.getClass1();
                            String Cname2 = users.getClass2();
                            String Cname3 = users.getClass3();
                            String Cname4 = users.getClass4();
                            String Cname5 = users.getClass5();
                            g = users.getGender();

                            f_name.setText(Fname);
                            m_name.setText(Mname);
                            l_name.setText(Lname);
                            cname1.setText(Cname1);
                            cname2.setText(Cname2);
                            cname3.setText(Cname3);
                            cname4.setText(Cname4);
                            cname5.setText(Cname5);

                            c_c1.setText(Cname1);
                            c_c2.setText(Cname2);
                            c_c3.setText(Cname3);
                            c_c4.setText(Cname4);
                            c_c5.setText(Cname5);

                            if (g.equals("Male")) {
                                m.setChecked(true);
                                f.setChecked(false);

                            } else if (g.equals("Female")) {
                                m.setChecked(false);
                                f.setChecked(true);
                            }
                            retrieveCredentials();
                        }
                    }else{
                        Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (c == "Lecturer") {

            int id = Integer.parseInt(ID.getText().toString());

            ref = FirebaseDatabase.getInstance().getReference();

            Query firebaseSearchQuery = ref.child("Lecturer").orderByChild("id").equalTo(id);


            firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            usersdb users = postSnapshot.getValue(usersdb.class);

                            String Fname = users.getFName();
                            String Mname = users.getMName();
                            String Lname = users.getLName();
                            String Cname1 = users.getClass1();

                            f_name.setText(Fname);
                            m_name.setText(Mname);
                            l_name.setText(Lname);
                            cname1.setText(Cname1);

                            g = users.getGender();

                            if (g.equals("Male")) {
                                m.setChecked(true);
                                f.setChecked(false);

                            } else if (g.equals("Female")) {
                                m.setChecked(false);
                                f.setChecked(true);
                            }

                            retrieveCredentials();
                        }
                    }else{
                        Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error "+ error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void retrieveCredentials(){
        ref = FirebaseDatabase.getInstance().getReference();

        int id = Integer.parseInt(ID.getText().toString().trim());

        Query firebaseSearchQuery = ref.child("Credentials").orderByChild("id").equalTo(id);

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    usersdb users = postSnapshot.getValue(usersdb.class);

                    String Email = users.getEmail();
                    String Pass = users.getPass();

                    email.setText(Email);
                    pass.setText(Pass);
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void color(){
        if (ID.getText().toString().isEmpty()){
            ID.setError("Please fill this field");
            n1=0;
        }else {
            n1=1;
        }


        if(f_name.getText().toString().isEmpty()){
            f_name.setError("Please fill this field");
            n2=0;
        }else {
            n2=1;
        }


        if(l_name.getText().toString().isEmpty()){
            l_name.setError("Please fill this field");
            n3=0;
        }else {
            n3=1;
        }


        if(gengrp.getCheckedRadioButtonId()==-1){
            n4=0;
        }else {
            n4=1;
        }


        if(typegrp.getCheckedRadioButtonId()==-1){

            n5=0;
        }else {
            n5=1;
        }


        if(pass.getText().toString().isEmpty()){
            pass.setError("Please fill this field");
            n6=0;
        }else {
            n6=1;
        }


        if(email.getText().toString().isEmpty()){
            email.setError("Please fill this field");
            n7=0;
        }else{
            n7=1;
        }

        n = n1+n2+n3+n4+n5+n6+n7;

    }


    private void color1(){
        if (ID.getText().toString().isEmpty()){
            n1=0;
        }else {
            n1=1;
        }


        if(f_name.getText().toString().isEmpty()){
            n2=0;
        }else {
            n2=1;
        }


        if(l_name.getText().toString().isEmpty()){
            n3=0;
        }else {
            n3=1;
        }


        if(gengrp.getCheckedRadioButtonId()==-1){
            n4=0;
        }else {
            n4=1;
        }


        if(typegrp.getCheckedRadioButtonId()==-1){
            n5=0;
        }else {
            n5=1;
        }


        if(pass.getText().toString().isEmpty()){
            n6=0;
        }else {
            n6=1;
        }


        if(email.getText().toString().isEmpty()){
            n7=0;
        }else{
            n7=1;
        }

        n = n1+n2+n3+n4+n5+n6+n7;
    }


    private void spinnerset(){
        spinnerDataList1 = new ArrayList<>();
        spinnerDataList2 = new ArrayList<>();
        spinnerDataList3 = new ArrayList<>();
        spinnerDataList4 = new ArrayList<>();
        spinnerDataList5 = new ArrayList<>();



        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Classes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()){

                    spinnerValue1 = childSnapshot.child("id").getValue(String.class);
                    spinnerDataList1.add(spinnerValue1);

                    spinnerValue2 = childSnapshot.child("id").getValue(String.class);
                    spinnerDataList2.add(spinnerValue2);

                    spinnerValue3 = childSnapshot.child("id").getValue(String.class);
                    spinnerDataList3.add(spinnerValue3);

                    spinnerValue4 = childSnapshot.child("id").getValue(String.class);
                    spinnerDataList4.add(spinnerValue4);

                    spinnerValue5 = childSnapshot.child("id").getValue(String.class);
                    spinnerDataList5.add(spinnerValue5);

                }

                adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,spinnerDataList1);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);

                adapter2 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,spinnerDataList2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

                adapter3 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,spinnerDataList3);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);

                adapter4 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,spinnerDataList4);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_item);

                adapter5 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,spinnerDataList5);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_item);


                spinnerCourse1.setAdapter(adapter1);
                spinnerCourse2.setAdapter(adapter2);
                spinnerCourse3.setAdapter(adapter3);
                spinnerCourse4.setAdapter(adapter4);
                spinnerCourse5.setAdapter(adapter5);

                spinnerSelection();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateUI(FirebaseUser currentUser){
        String keyId = ref.push().getKey();
        ref.child(keyId).setValue(users);
    }


    private void hideFields(){
        spinnerCourse1.setVisibility(View.INVISIBLE);
        spinnerCourse2.setVisibility(View.INVISIBLE);
        spinnerCourse3.setVisibility(View.INVISIBLE);
        spinnerCourse4.setVisibility(View.INVISIBLE);
        spinnerCourse5.setVisibility(View.INVISIBLE);

        c1.setVisibility(View.INVISIBLE);
        c2.setVisibility(View.INVISIBLE);
        c3.setVisibility(View.INVISIBLE);
        c4.setVisibility(View.INVISIBLE);
        c5.setVisibility(View.INVISIBLE);

        c_c1.setVisibility(View.INVISIBLE);
        c_c2.setVisibility(View.INVISIBLE);
        c_c3.setVisibility(View.INVISIBLE);
        c_c4.setVisibility(View.INVISIBLE);
        c_c5.setVisibility(View.INVISIBLE);

        cname1.setVisibility(View.INVISIBLE);
        cname2.setVisibility(View.INVISIBLE);
        cname3.setVisibility(View.INVISIBLE);
        cname4.setVisibility(View.INVISIBLE);
        cname5.setVisibility(View.INVISIBLE);

        cname1.setEnabled(false);
        cname2.setEnabled(false);
        cname3.setEnabled(false);
        cname4.setEnabled(false);
        cname5.setEnabled(false);
    }


    private void spinnerValueCount() {

        int num = spinnerCourse1.getAdapter().getCount();

        if (num >= 5) {

            if (spinnerValue1 == spinnerValue2 || spinnerValue1 == spinnerValue3 || spinnerValue1 == spinnerValue4 || spinnerValue1 == spinnerValue5) {
                s1 = 0;
            } else {
                s1 = 1;
            }

            if (spinnerValue2 == spinnerValue3 || spinnerValue2 == spinnerValue4 || spinnerValue2 == spinnerValue5) {
                s2 = 0;
            } else {
                s2 = 1;
            }

            if (spinnerValue3 == spinnerValue4 || spinnerValue3 == spinnerValue5) {
                s3 = 0;
            } else {
                s3 = 1;
            }

            if (spinnerValue4 == spinnerValue5) {
                s4 = 0;
            } else {
                s4 = 1;
            }


            users.setClass1(spinnerValue1);
            users.setClass2(spinnerValue2);
            users.setClass3(spinnerValue3);
            users.setClass4(spinnerValue4);
            users.setClass5(spinnerValue5);

        } else if (num == 4) {

            if (spinnerValue1 == spinnerValue2 || spinnerValue1 == spinnerValue3 || spinnerValue1 == spinnerValue4) {
                s1 = 0;
            } else {
                s1 = 1;
            }

            if (spinnerValue2 == spinnerValue3 || spinnerValue2 == spinnerValue4) {
                s2 = 0;
            } else {
                s2 = 1;
            }

            if (spinnerValue3 == spinnerValue4) {
                s3 = 0;
            } else {
                s3 = 1;
            }

            s4 = 1;


            users.setClass1(spinnerValue1);
            users.setClass2(spinnerValue2);
            users.setClass3(spinnerValue3);
            users.setClass4(spinnerValue4);
            users.setClass5("");


        }else if (num == 3){

            if (spinnerValue1 == spinnerValue2 || spinnerValue1 == spinnerValue3) {
                s1 = 0;
            } else {
                s1 = 1;
            }

            if (spinnerValue2 == spinnerValue3) {
                s2 = 0;
            } else {
                s2 = 1;
            }

            s3 = 1;
            s4 = 1;


            users.setClass1(spinnerValue1);
            users.setClass2(spinnerValue2);
            users.setClass3(spinnerValue3);
            users.setClass4("");
            users.setClass5("");

        }else if (num == 2){

            if (spinnerValue1 == spinnerValue2) {
                s1 = 0;
            } else {
                s1 = 1;
            }

            s2 = 1;
            s3 = 1;
            s4 = 1;


            users.setClass1(spinnerValue1);
            users.setClass2(spinnerValue2);
            users.setClass3("");
            users.setClass4("");
            users.setClass5("");

        }else if (num == 1){
            users.setClass1(spinnerValue1);
            users.setClass2("");
            users.setClass3("");
            users.setClass4("");
            users.setClass5("");

            s1 = 1;
            s2 = 1;
            s3 = 1;
            s4 = 1;

        }else{
            users.setClass1("");
            users.setClass2("");
            users.setClass3("");
            users.setClass4("");
            users.setClass5("");

            s1 = 1;
            s2 = 1;
            s3 = 1;
            s4 = 1;
        }

        s = s1+ s2 +s3 + s4;

    }


    private void spinnerSelection(){
        spinnerCourse1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> child, View view, int position, long id) {
                spinnerValue1 = String.valueOf(spinnerCourse1.getSelectedItem());
                a1=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a1=1;
            }
        });

        spinnerCourse2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> child, View view, int position, long id) {
                spinnerValue2 = String.valueOf(spinnerCourse2.getSelectedItem());
                a2=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a2=1;
            }
        });

        spinnerCourse3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> child, View view, int position, long id) {
                spinnerValue3 = String.valueOf(spinnerCourse3.getSelectedItem());
                a3=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a3=1;
            }
        });

        spinnerCourse4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> child, View view, int position, long id) {
                spinnerValue4 = String.valueOf(spinnerCourse4.getSelectedItem());
                a4=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a4=1;
            }
        });

        spinnerCourse5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> child, View view, int position, long id) {
                spinnerValue5 = String.valueOf(spinnerCourse5.getSelectedItem());
                a5=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a5=1;
            }
        });

        a=a1+a2+a3+a4+a5;
    }


    private void hideForSearch() {
        if (stud.isChecked()) {
            spinnerCourse1.setVisibility(View.INVISIBLE);
            spinnerCourse2.setVisibility(View.INVISIBLE);
            spinnerCourse3.setVisibility(View.INVISIBLE);
            spinnerCourse4.setVisibility(View.INVISIBLE);
            spinnerCourse5.setVisibility(View.INVISIBLE);

            cname1.setVisibility(View.VISIBLE);
            cname2.setVisibility(View.VISIBLE);
            cname3.setVisibility(View.VISIBLE);
            cname4.setVisibility(View.VISIBLE);
            cname5.setVisibility(View.VISIBLE);

        } else if (lec.isChecked()){
            spinnerCourse1.setVisibility(View.INVISIBLE);
            spinnerCourse2.setVisibility(View.INVISIBLE);
            spinnerCourse3.setVisibility(View.INVISIBLE);
            spinnerCourse4.setVisibility(View.INVISIBLE);
            spinnerCourse5.setVisibility(View.INVISIBLE);

            c1.setVisibility(View.VISIBLE);

            cname1.setVisibility(View.VISIBLE);
            cname2.setVisibility(View.INVISIBLE);
            cname3.setVisibility(View.INVISIBLE);
            cname4.setVisibility(View.INVISIBLE);
            cname5.setVisibility(View.INVISIBLE);
        }
    }


    private void IDcheck(){
        ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ID.getText().toString().isEmpty()) {
                    // clear.setEnabled(false);
                    delete.setEnabled(false);
                    update.setEnabled(false);
                    save.setEnabled(false);
                    search.setEnabled(false);

                } else {
                    search.setEnabled(true);
                    save.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getDate(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);
    }

    private void setmaxidStudClassSave1(){

        final int id = Integer.parseInt(ID.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery1 = ref.child("Classes").child(String.valueOf(spinnerValue1)).orderByChild("Students").equalTo(id);

        firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxidc1=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setmaxidStudClassSave2(){

        final int id = Integer.parseInt(ID.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery1 = ref.child("Classes").child(String.valueOf(spinnerValue2)).orderByChild("Students").equalTo(id);

        firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxidc2=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setmaxidStudClassSave3(){

        final int id = Integer.parseInt(ID.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery1 = ref.child("Classes").child(String.valueOf(spinnerValue3)).orderByChild("Students").equalTo(id);

        firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxidc3=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setmaxidStudClassSave4(){

        final int id = Integer.parseInt(ID.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery1 = ref.child("Classes").child(String.valueOf(spinnerValue4)).orderByChild("Students").equalTo(id);

        firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxidc4=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setmaxidStudClassSave5(){

        final int id = Integer.parseInt(ID.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery1 = ref.child("Classes").child(String.valueOf(spinnerValue5)).orderByChild("Students").equalTo(id);

        firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxidc5=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void computeMD5Hash(String password){

        try{

            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++){
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
            pass.setText(MD5Hash);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

}



