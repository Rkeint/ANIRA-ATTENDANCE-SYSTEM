package com.example.aasv11.Student.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.service.autofill.FillEventHistory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aasv11.R;
import com.example.aasv11.database.attendance;
import com.example.aasv11.database.usersdb;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkRegister extends Fragment {

    private EditText code;
    private Spinner classes;
    private Button mark;
    DatabaseReference ref, ref2, ref1;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatList;
    String classa, classb, classc, classd, classe;
    String className, thisDate;
    int abs;
    attendance att;
    int id;
    long maxid = 0;

    public MarkRegister() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mark_register, container, false);

        code = (EditText) view.findViewById(R.id.txtCode);
        classes = (Spinner) view.findViewById(R.id.spinnerClass);
        mark = (Button) view.findViewById(R.id.btnMark);

        att = new attendance();

        getDate();
        getClasses();
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // setmaxid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markPresent();
            }
        });

        return view;
    }

    private void spinnerSet(){

        List<String> studClass = new ArrayList<>();
        studClass.add(0,"Pick a class");

        studClass.add(classa);
        studClass.add(classb);
        studClass.add(classc);
        studClass.add(classd);
        studClass.add(classe);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, studClass);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classes.setAdapter(adapter);

        classes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Pick a class")){

                }else{
                    className = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getActivity(), className,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void markPresent(){

        ref = FirebaseDatabase.getInstance().getReference();

        final int id1 = Integer.parseInt(code.getText().toString().trim());


        final Query firebaseSearchQuery = ref.child("Attendance").child(String.valueOf(className)).orderByChild("code").equalTo(id1);

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    ref1 = FirebaseDatabase.getInstance().getReference();

                    Query firebaseSearchQuery1 = ref1.child("Classes").child(String.valueOf(className)).child("Students").orderByChild("stuid").equalTo(id);

                    firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                            if (snapshot.exists()) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    attendance att = postSnapshot.getValue(attendance.class);

                                    final int pres = att.getAtt();
                                    Query firebaseSearchQuery2 = ref1.child("Classes").child(String.valueOf(className)).child("Students").orderByChild("date").equalTo(thisDate);

                                    firebaseSearchQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                alertDialog.setTitle("NOTIFICATION");
                                                alertDialog.setMessage("YOU HAVE ALREADY BEEN MARKED PRESENT FOR "+className+" ON "+ thisDate);
                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                              dialog.dismiss();
                                                            }
                                                        });
                                                alertDialog.show();
                                            } else {
                                                ref.child("Classes").child(String.valueOf(className)).child("Students").child(String.valueOf(id)).child("att").setValue(pres + 1);
                                                ref.child("Classes").child(String.valueOf(className)).child("Students").child(String.valueOf(id)).child("date").setValue(thisDate);
                                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                                alertDialog.setTitle("SUCCESS");
                                                alertDialog.setMessage("YOU HAVE SUCCESSFULLY BEEN MARKED PRESENT FOR "+className.toUpperCase()+" ON "+ thisDate);
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
                            }else{
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }else{
                   Toast.makeText(getActivity(),"wrong code", Toast.LENGTH_SHORT).show();
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "ERROR: "+error, Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void getDate(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);

    }


    public void getClasses(){

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

                        id = users.getID();

                        //tvNavName.setText(String.valueOf(id));
                        String studmail = email;

                        ref2 = FirebaseDatabase.getInstance().getReference();

                        Query search1 = ref2.child("Student").orderByChild("id").equalTo(id);

                        search1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    usersdb users = postSnapshot.getValue(usersdb.class);

                                     classa = users.getClass1();
                                     classb = users.getClass2();
                                     classc = users.getClass3();
                                     classd = users.getClass4();
                                     classe = users.getClass5();

                                     spinnerSet();
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


    private void setmaxid(){


        final int id1 = Integer.parseInt(code.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery1 = ref.child("Attendance").child(String.valueOf(className)).orderByChild("code").equalTo(id1);

        firebaseSearchQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxid=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}