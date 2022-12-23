package com.example.newsappremastered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvRegHere;

    private Boolean setLogin;

    private NotificationManager mnotificationManager;
    private final static String CHANNEL_ID = "primary-channel";
    private int NOTIFICATION_ID = 0;

    public static SharedPreferences mSharedPref;
    private final String sharedPrefFile = "com.example.sharedpreferenceapp";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";

    //create dbref obj to access firebase realtime db
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://newsappremaster-default-rtdb.firebaseio.com/");

    //crate sharedPref var, to save login data
    SharedPreferences.Editor setData;

    //sharedPref to read data
    SharedPreferences getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegHere = findViewById(R.id.tv_registerHere);
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        setLogin = mSharedPref.getBoolean(LOGIN_STATUS, false);

        mnotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "app notif", NotificationManager.IMPORTANCE_HIGH);
            mnotificationManager.createNotificationChannel(notificationChannel);
        }

        //declare sharedPref with name and private mode, so can read and write data
        //to save data, add edit

//        getData = getSharedPreferences("login_data", MODE_PRIVATE);

        final Intent intent = new Intent(this, DetailUser.class);
        if (setLogin){
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Please enter your username and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if username is exist in database
                            if(snapshot.hasChild(username)){

                                //username is exist in database
                                //get pw of user from database n match with user entered pw
                                final String getPassword = snapshot.child(username).child("password").getValue(String.class);

                                if(getPassword.equals(password)){
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                    //set login data
                                    setData = getApplicationContext().getSharedPreferences("login_data", MODE_PRIVATE).edit();
                                    setData.putString("username", username);
                                    setData.putString("password", password);
                                    setData.apply();

                                    //open main activity
                                    startActivity(new Intent(Login.this, DetailUser.class));
                                    saveLogin();
                                    finish();
                                    sendNotification();
                                }
                                else{
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login.this, "Username not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        tvRegHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open register activity
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, Login.class);
        PendingIntent notificationPendingIntent = PendingIntent
                .getActivity(this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Login Success")
                        .setContentText("Welcome to News App")
                        .setSmallIcon(R.drawable.ic_baseline_desktop_mac_24).setContentIntent(notificationPendingIntent);
        return notifyBuilder;
    }

    private void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mnotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private void saveLogin(){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(LOGIN_STATUS, true);
        editor.apply();
    }

    public static void setLogout(){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(LOGIN_STATUS, false);
        editor.apply();
    }
}