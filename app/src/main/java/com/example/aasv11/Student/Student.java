package com.example.aasv11.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

public class Student extends AppCompatActivity {

    TextView tvNavName, tvNavEmail;
    private DatabaseReference userRef1, userRef2;
    private static final String USER = "Credentials";
    usersdb users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final DrawerLayout drawerLayout = findViewById(R.id.StudentDrawerLayout);

/*        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });*/

        NavigationView navigationView = findViewById(R.id.student_navigationView);

        NavController navController = Navigation.findNavController(this, R.id.student_navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);

        View headerLayout = navigationView.inflateHeaderView(R.layout.layout_navigation_header);

        tvNavName = (TextView)  headerLayout.findViewById(R.id.txtUName);
        tvNavEmail = (TextView) headerLayout.findViewById(R.id.txtMail);

        users = new usersdb();

        Auth();
        //credRef();

       // final TextView textTitle = findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                textTitle.setText(destination.getLabel());

            }
        });
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

                        int id = users.getID();

                        //tvNavName.setText(String.valueOf(id));
                        tvNavEmail.setText(email);

                        userRef2 = FirebaseDatabase.getInstance().getReference();

                        Query search1 = userRef2.child("Student").orderByChild("id").equalTo(id);

                        search1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                    usersdb users = postSnapshot.getValue(usersdb.class);

                                    String Fname = users.getFName();
                                    String LName = users.getLName();

                                    tvNavName.setText(Fname + " " + LName);
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

    public void credRef(){

        String id1 = tvNavName.getText().toString().trim();

        userRef2 = FirebaseDatabase.getInstance().getReference();

        Query search1 = userRef2.child("Student").orderByChild("id").equalTo(String.valueOf(id1));

        search1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    usersdb users = postSnapshot.getValue(usersdb.class);

                    String Fname = users.getFName();

                    tvNavName.setText(Fname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}