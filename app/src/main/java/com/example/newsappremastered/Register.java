package com.example.newsappremastered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Register extends AppCompatActivity {

    EditText etUsername, etPassword, etConfPassword;
    Button btnRegister;
    TextView tvLoginHere;

    //create dbref obj to access firebase realtime db
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://newsappremaster-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfPassword = findViewById(R.id.et_confPassword);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginHere = findViewById(R.id.tv_loginHere);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data from edittext to string var
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String confPassword = etConfPassword.getText().toString();

                //check if user fill all fields
                if(username.isEmpty() || password.isEmpty() || confPassword.isEmpty()){
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                //check if password and confPass is same
                else if(!password.equals(confPassword)){
                    Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                }

                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if username haven't registered before
                            if(snapshot.hasChild(username)){
                                Toast.makeText(Register.this, "Username is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //send data to firebase realtime db
                                databaseReference.child("users").child(username).child("password").setValue(password);

                                Toast.makeText(Register.this, "User registered succesfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open login activity
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

}