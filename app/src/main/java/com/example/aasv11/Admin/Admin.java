package com.example.aasv11.Admin;

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

import com.example.aasv11.R;
import com.google.android.material.navigation.NavigationView;

public class Admin extends AppCompatActivity {
    TextView tvNavName, tvNavEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final DrawerLayout drawerLayout = findViewById(R.id.AdmindrawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.admin_navigationView);

        NavController navController = Navigation.findNavController(this, R.id.admin_navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);


        final TextView textTitle = findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                textTitle.setText(destination.getLabel());

            }
        });

        View headerLayout = navigationView.inflateHeaderView(R.layout.layout_navigation_header);

        tvNavName = (TextView)  headerLayout.findViewById(R.id.txtUName);
        tvNavEmail = (TextView) headerLayout.findViewById(R.id.txtMail);

        tvNavEmail.setText("Rani Keinth");
        tvNavName.setText("0754688993");

    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLogout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout(){
        AlertDialog alertDialog = new AlertDialog.Builder(Admin.this).create();
        alertDialog.setTitle("LOGOUT");
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Admin.this.finish();
                        startActivity(new Intent(Admin.this, Login.class));
                        Toast.makeText(Admin.this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Admin.this.finish();
                        startActivity(new Intent(Admin.this, Admin.class));
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }*/

}