package com.example.aasv11.Student.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aasv11.R;
import com.example.aasv11.database.attendance;
import com.example.aasv11.database.classdb;
import com.example.aasv11.database.usersdb;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.aasv11.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aasv11.R;
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
import com.example.aasv11.database.classdb;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentHome extends Fragment {

    private DatabaseReference userRef1, userRef2, classRef, absref, reference;
    private TextView salutations, class1, class2, class3, class4, class5;
    private TextView lec1, lec2, lec3, lec4, lec5;
    private TextView abs1, abs2, abs3, abs4, abs5;
    private TextView studentid;
    private String classa, classb, classc, classd, classe, thisDate;
    classdb classdb;
    usersdb users;
    attendance att;
    long maxid1 = 0;
    int id;


    public StudentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        salutations = (TextView) view.findViewById(R.id.tv_Hi);
        class1 = (TextView) view.findViewById(R.id.tv_Class1);
        class2 = (TextView) view.findViewById(R.id.tv_Class2);
        class3 = (TextView) view.findViewById(R.id.tv_Class3);
        class4 = (TextView) view.findViewById(R.id.tv_Class4);
        class5 = (TextView) view.findViewById(R.id.tv_Class5);

        lec1 = (TextView) view.findViewById(R.id.Lec1);
        lec2 = (TextView) view.findViewById(R.id.Lec2);
        lec3 = (TextView) view.findViewById(R.id.Lec3);
        lec4 = (TextView) view.findViewById(R.id.Lec4);
        lec5 = (TextView) view.findViewById(R.id.Lec5);

        abs1 = (TextView) view.findViewById(R.id.tv_Absence1);
        abs2 = (TextView) view.findViewById(R.id.tv_Absence2);
        abs3 = (TextView) view.findViewById(R.id.tv_Absence3);
        abs4 = (TextView) view.findViewById(R.id.tv_Absence4);
        abs5 = (TextView) view.findViewById(R.id.tv_Absence5);

        studentid = (TextView) view.findViewById(R.id.studentid);

        //studentid.setVisibility(View.INVISIBLE);

        classdb = new classdb();
        users = new usersdb();
        att = new attendance();

        //Absence1();
        Studentdb();
       // Absenceclass1();

        return view;
    }

    public void Studentdb() {

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

                        studentid.setText(String.valueOf(id));

                        //tvNavName.setText(String.valueOf(id));
                        String studmail = email;

                        userRef2 = FirebaseDatabase.getInstance().getReference();

                        Query search1 = userRef2.child("Student").orderByChild("id").equalTo(id);

                        search1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    usersdb users = postSnapshot.getValue(usersdb.class);

                                    String Fname = users.getFName();
                                    classa = users.getClass1();
                                    classb = users.getClass2();
                                    classc = users.getClass3();
                                    classd = users.getClass4();
                                    classe = users.getClass5();

                                    salutations.setText("Hi there " + Fname);
                                    class1.setText(classa);
                                    class2.setText(classb);
                                    class3.setText(classc);
                                    class4.setText(classd);
                                    class5.setText(classe);

                                    /*AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("SHOW");
                                    alertDialog.setMessage("Show?");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                     dialog.dismiss();


                                                }
                                            });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();*/

                                    Class1();
                                    Class2();
                                    Class3();
                                    Class4();
                                    Class5();


                                    Absenceclass1();
                                    Absenceclass2();
                                    Absenceclass3();
                                    Absenceclass4();
                                    Absenceclass5();
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


    private void Class1() {

        String clref = class1.getText().toString().trim();

        classRef = FirebaseDatabase.getInstance().getReference();

        Query searchClass = classRef.child("Classes").orderByChild("id").equalTo(clref);

        searchClass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        classdb classdb = postsnapshot.getValue(classdb.class);
                        String lecid = classdb.getLecname();
                            lec1.setText(String.valueOf(lecid));
                    }
                }else{
                    lec1.setText("Not Assigned");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lec1.setText("Error" + error);
            }
        });
    }


    private void Class2() {

        String clref = class2.getText().toString().trim();

        classRef = FirebaseDatabase.getInstance().getReference();

        Query searchClass = classRef.child("Classes").orderByChild("id").equalTo(clref);

        searchClass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        classdb classdb = postsnapshot.getValue(classdb.class);
                        String lecid = classdb.getLecname();
                        if (lecid == "") {
                            lec2.setText("Not Assigned");
                        }else{
                            lec2.setText(lecid);
                        }
                    }
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Class3() {

        String clref = class3.getText().toString().trim();

        classRef = FirebaseDatabase.getInstance().getReference();

        Query searchClass = classRef.child("Classes").orderByChild("id").equalTo(clref);

        searchClass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        classdb classdb = postsnapshot.getValue(classdb.class);
                        String lecid = classdb.getLecname();
                        if (lecid == "") {
                            lec3.setText("Not Assigned");
                        }else{
                            lec3.setText(lecid);
                        }
                    }
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Class4() {

        String clref = class4.getText().toString().trim();

        classRef = FirebaseDatabase.getInstance().getReference();

        Query searchClass = classRef.child("Classes").orderByChild("id").equalTo(clref);

        searchClass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        classdb classdb = postsnapshot.getValue(classdb.class);
                        String lecid = classdb.getLecname();
                            lec4.setText(lecid);
                    }
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Class5() {

        String clref = class5.getText().toString().trim();

        classRef = FirebaseDatabase.getInstance().getReference();

        Query searchClass = classRef.child("Classes").orderByChild("id").equalTo(clref);

        searchClass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                        classdb classdb = postsnapshot.getValue(classdb.class);
                        String lecid = classdb.getLecname();
                            lec5.setText(lecid);
                    }
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    /*private void Absence1() {
        absref = FirebaseDatabase.getInstance().getReference();

        //int a = Integer.parseInt((studentid).getText().toString().trim());

        String clas =class1.getText().toString().trim();

        Query firebaseSearchQuery2 = absref.child("Classes").child(clas).child("Students").orderByChild("stuid").equalTo(id);

        //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        firebaseSearchQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendance att = snapshot.getValue(attendance.class);
                long pres = att.getAtt();

                long txt = sesscount - pres;
                abs1.setText(String.valueOf(txt));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setmaxidAtt() {

        refid = FirebaseDatabase.getInstance().getReference().child("Attendance");

        refid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid1 = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/

    private void Absenceclass1() {

        final String id1 = class1.getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.orderByChild("name").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    absref = FirebaseDatabase.getInstance().getReference().child("Classes").child(id1).child("Students").child(String.valueOf(id));

                    //int a = Integer.parseInt((studentid).getText().toString().trim());

                    String clas =class1.getText().toString().trim();

                   // Query firebaseSearchQuery2 = absref.child("Classes").child(clas).child("Students").orderByChild("stuid").equalTo(id);

                    //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                    absref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                attendance att = snapshot.getValue(attendance.class);
                                long pres = snapshot.child("att").getValue(long.class);

                                long txt = sesscount - pres;
                                abs1.setText(String.valueOf(txt));
                            }else{
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Absenceclass2() {

        final String id1 = class2.getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.orderByChild("name").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    absref = FirebaseDatabase.getInstance().getReference().child("Classes").child(id1).child("Students").child(String.valueOf(id));

                    //int a = Integer.parseInt((studentid).getText().toString().trim());

                    String clas =class1.getText().toString().trim();

                    // Query firebaseSearchQuery2 = absref.child("Classes").child(clas).child("Students").orderByChild("stuid").equalTo(id);

                    //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                    absref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                attendance att = snapshot.getValue(attendance.class);
                                long pres = snapshot.child("att").getValue(long.class);

                                long txt = sesscount - pres;
                                abs2.setText(String.valueOf(txt));
                            }else{
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Absenceclass3() {

        final String id1 = class3.getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.orderByChild("name").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    absref = FirebaseDatabase.getInstance().getReference().child("Classes").child(id1).child("Students").child(String.valueOf(id));

                    //int a = Integer.parseInt((studentid).getText().toString().trim());

                    String clas =class3.getText().toString().trim();

                    // Query firebaseSearchQuery2 = absref.child("Classes").child(clas).child("Students").orderByChild("stuid").equalTo(id);

                    //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                    absref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                attendance att = snapshot.getValue(attendance.class);
                                long pres = snapshot.child("att").getValue(long.class);

                                long txt = sesscount - pres;
                                abs3.setText(String.valueOf(txt));
                            }else{
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Absenceclass4() {

        final String id1 = class4.getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.orderByChild("name").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    absref = FirebaseDatabase.getInstance().getReference().child("Classes").child(id1).child("Students").child(String.valueOf(id));

                    //int a = Integer.parseInt((studentid).getText().toString().trim());

                    String clas =class4.getText().toString().trim();

                    // Query firebaseSearchQuery2 = absref.child("Classes").child(clas).child("Students").orderByChild("stuid").equalTo(id);

                    //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                    absref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                attendance att = snapshot.getValue(attendance.class);
                                long pres = snapshot.child("att").getValue(long.class);

                                long txt = sesscount - pres;
                                abs4.setText(String.valueOf(txt));
                            }else{
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void Absenceclass5() {

        final String id1 = class5.getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(id1).child("Session");
        reference.orderByChild("name").equalTo(thisDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long sc = snapshot.child("count").getValue(long.class);
                    final Long sesscount = sc;
                    absref = FirebaseDatabase.getInstance().getReference().child("Classes").child(id1).child("Students").child(String.valueOf(id));

                    //int a = Integer.parseInt((studentid).getText().toString().trim());

                    //String clas =class1.getText().toString().trim();

                    // Query firebaseSearchQuery2 = absref.child("Classes").child(clas).child("Students").orderByChild("stuid").equalTo(id);

                    //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                    absref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                attendance att = snapshot.getValue(attendance.class);
                                long pres = snapshot.child("att").getValue(long.class);

                                long txt = sesscount - pres;
                                abs5.setText(String.valueOf(txt));
                            }else{
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
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
}