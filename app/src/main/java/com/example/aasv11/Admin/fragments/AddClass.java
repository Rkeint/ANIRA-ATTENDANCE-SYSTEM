package com.example.aasv11.Admin.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.aasv11.database.usersdb;
import com.example.aasv11.R;
import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.aasv11.database.classdb;

import java.util.ArrayList;

import static com.example.aasv11.R.id.fbtnSave;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddClass extends Fragment {

    EditText ID, Cname, Lname;
    TextView tvid, tvcname;
    FloatingActionButton save, update, delete, clear, search;
    RecyclerView recyclerview;
    Spinner spinnerLec;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    DatabaseReference ref, ref2, refid;
    ValueEventListener listener;
    String spinnerValue;
    int lecID;
    int n, n1, n2;
    int a = 1;

    private AlertDialog.Builder dialogBuilder;

    classdb classdb;
    usersdb users;

    public AddClass() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        save = (FloatingActionButton) view.findViewById(fbtnSave);
        delete = (FloatingActionButton) view.findViewById(R.id.fbtnDelete);
        search = (FloatingActionButton) view.findViewById(R.id.fbtnSearch);
        update = (FloatingActionButton) view.findViewById(R.id.fbtnUpdate);
        clear = (FloatingActionButton) view.findViewById(R.id.fbtnClear);

        tvid = (TextView) view.findViewById(R.id.t_ID);
        tvcname = (TextView) view.findViewById(R.id.t_Name);

        spinnerLec = (Spinner) view.findViewById(R.id.spinLec);

        classdb = new classdb();


        ID = (EditText) view.findViewById(R.id.txtID);
        Cname = (EditText) view.findViewById(R.id.txtName);
        Lname = (EditText)  view.findViewById(R.id.txtLecName);


        Lname.setVisibility(View.INVISIBLE);
        Lname.setEnabled(false);


        spinnerset();

        users = new usersdb();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color();
                if (n==2){
                    if(a==0){
                        spinnerLec.setVisibility(View.VISIBLE);
                        Lname.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Please choose a Lec from the spinner", Toast.LENGTH_LONG).show();
                        a=1;
                    }else{
                        saveclasses();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please fill in the required fields (Marked in Red)", Toast.LENGTH_LONG).show();
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("DELETE");
                alertDialog.setMessage("Are you sure you want to delete this class from the database? (please not this step cannot be undone)");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteclasses();
                                Toast.makeText(getActivity(),"Successfully deleted from the database", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelection();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideForSearch();
                searchClass();
            }
        });
        return view;
    }


    private void spinnerset() {

        spinnerDataList = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Lecturer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    lecID = childSnapshot.child("id").getValue(int.class);
                    spinnerValue = childSnapshot.child("fname").getValue(String.class);
                    spinnerDataList.add(spinnerValue);

                }
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerDataList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerLec.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerLec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> child, View view, int position, long id) {
                spinnerValue = String.valueOf(spinnerLec.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Toast.makeText(getActivity(),"No Lecturer selected", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveclasses() {

        ref = FirebaseDatabase.getInstance().getReference().child("Lecturer");
        ref2 = FirebaseDatabase.getInstance().getReference().child("Classes");
        refid = FirebaseDatabase.getInstance().getReference();

        classdb.setId(ID.getText().toString().trim());
        classdb.setCoursename(Cname.getText().toString().trim());
        classdb.setLecname(spinnerValue);

        final String id = (ID.getText().toString().trim());

        ref2.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("ERROR SAVING");
                    alertDialog.setMessage("A class with ID: "+id+" already exists");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();

                }else{

                    Query firebaseSearchQuery = refid.child("Lecturer").orderByChild("id").equalTo(lecID);

                    firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    usersdb users = postSnapshot.getValue(usersdb.class);
                                    final String classname = users.getClass1();

                                    if (classname.equals("")){
                                        ref.child(String.valueOf(lecID)).child("class1").setValue(id);
                                        ref2.child(id).setValue(classdb);
                                        Toast.makeText(getActivity(), "Course: " + Cname.getText().toString() + " added successfully", Toast.LENGTH_LONG).show();
                                    }else{
                                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("OVERRIDE?");
                                        alertDialog.setMessage("Dr. "+spinnerValue+" is already assigned to "+classname+". Would you like to swap the class?");
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        ref2.child(classname).child("lecname").setValue("");
                                                        ref.child(String.valueOf(lecID)).child("class1").setValue(id);
                                                        ref2.child(id).setValue(classdb);
                                                        Toast.makeText(getActivity(), "Course: " + Cname.getText().toString() + " added successfully", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                }
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


    private void deleteclasses(){

        ref = FirebaseDatabase.getInstance().getReference();

        final String id = (ID.getText().toString().trim());

        Query firebaseSearchQuery = ref.child("Classes").orderByChild("id").equalTo(id);


        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (final DataSnapshot postSnapshot1 : snapshot.getChildren()) {

                    postSnapshot1.getRef().removeValue();
                    deletefromlec();
                    deletefromstud();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void searchClass(){

        ref2 = FirebaseDatabase.getInstance().getReference();

        final String id = (ID.getText().toString().trim());

        Query firebaseSearchQuery = ref.child("Classes").orderByChild("id").equalTo(id);
        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot postsnapshot : snapshot.getChildren()){

                        classdb classdb = postsnapshot.getValue(com.example.aasv11.database.classdb.class);

                        String ClassID = classdb.getId();
                        String ClassName = classdb.getCoursename();
                        String LecName = classdb.getLecname();

                        ID.setText(ClassID);
                        Cname.setText(ClassName);
                        Lname.setText(LecName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void clearSelection(){
        ID.setText("");
        Cname.setText("");
        Lname.setText("");
        spinnerLec.setVisibility(View.VISIBLE);
        Lname.setVisibility(View.INVISIBLE);
        a = 1;
    }


    private void color(){
        if (ID.getText().toString().isEmpty()){
            ID.setError("Please fill this field");
            n1=0;
        }else {
            n1=1;
        }


        if(Cname.getText().toString().isEmpty()){
            Cname.setError("Please fill this field");
            n2=0;
        }else {
            n2=1;
        }

        n=n1+n2;
    }


    private void hideForSearch(){
        spinnerLec.setVisibility(View.INVISIBLE);
        Lname.setVisibility(View.VISIBLE);
        a = 0;
    }


    private void deletefromlec(){
        refid = FirebaseDatabase.getInstance().getReference();

        Query firebaseSearchQuery = refid.child("Lecturer").orderByChild("id").equalTo(lecID);

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        usersdb users = postSnapshot.getValue(usersdb.class);
                        final String classname = users.getClass1();

                        if (classname.isEmpty()) {

                        }else{
                            ref.child("Lecturer").child(String.valueOf(lecID)).child("class1").setValue("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deletefromstud(){
        refid = FirebaseDatabase.getInstance().getReference();

        final String clid = (ID.getText().toString().trim());
        Query firebaseSearchQuery1 = refid.child("Student").orderByChild("class1").equalTo(clid);
        Query firebaseSearchQuery2 = refid.child("Student").orderByChild("class2").equalTo(clid);
        Query firebaseSearchQuery3 = refid.child("Student").orderByChild("class3").equalTo(clid);
        Query firebaseSearchQuery4 = refid.child("Student").orderByChild("class4").equalTo(clid);
        Query firebaseSearchQuery5 = refid.child("Student").orderByChild("class5").equalTo(clid);


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
                            ref.child("Student").child(String.valueOf(id)).child("class1").setValue("");
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
                            ref.child("Student").child(String.valueOf(id)).child("class2").setValue("");
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
                            ref.child("Student").child(String.valueOf(id)).child("class3").setValue("");
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
                            ref.child("Student").child(String.valueOf(id)).child("class4").setValue("");
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
                            ref.child("Student").child(String.valueOf(id)).child("class5").setValue("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
