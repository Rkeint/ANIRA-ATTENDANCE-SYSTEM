package com.example.aasv11.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aasv11.Admin.Admin;
import com.example.aasv11.Lecturer.Lecturer;
import com.example.aasv11.SplashScreens.SplashScreenAdmin;
import com.example.aasv11.Student.Student;
import com.example.aasv11.database.usersdb;
import com.example.aasv11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatabaseReference ref;
    private Button enter;
    EditText user, pass;
    LogDet confirm;
    usersdb users;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        fAuth = FirebaseAuth.getInstance();
        user = findViewById(R.id.txtUsername);
        pass = findViewById(R.id.txtPass);
        enter = findViewById(R.id.btnLogin);
        loading = new ProgressDialog(this);


        confirm = new LogDet();
        users = new usersdb();


        enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = user.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    user.setError("Email required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    pass.setError("Password required");
                    return;
                }



                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.setMessage("Logging In");
                            loading.show();
                            checkUserType();
                        } else {
                            Toast.makeText(Login.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }



    private void checkUserType(){
        String email = user.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        ref = FirebaseDatabase.getInstance().getReference();

        Query loginQuerymail = ref.child("Credentials").orderByChild("email").equalTo(email);

        loginQuerymail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    usersdb users = postSnapshot.getValue(usersdb.class);

                    String pass = users.getPass();
                    String type = users.getUser_Class();
                    String inpPass = password;

                    if (type.equals("Admin")) {
                        Toast.makeText(Login.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Admin.class));
                        loading.dismiss();
                        finish();

                    } else if (type.equals("Lecturer")) {
                        Toast.makeText(Login.this, "Welcome Lecturer", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Lecturer.class));
                        loading.dismiss();
                        finish();

                    } else if (type.equals("Student")) {
                        Toast.makeText(Login.this, "Welcome Student", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Student.class));
                        loading.dismiss();
                        finish();
                    } else {
                        loading.dismiss();
                        Toast.makeText(Login.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}


