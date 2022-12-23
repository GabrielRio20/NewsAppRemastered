package com.example.newsappremastered;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class DetailUser extends AppCompatActivity {

    SharedPreferences getData;
    EditText etTanggal;
    TextView tvUsia;
    Spinner spKategori;
    Button btnNews, btnLogout;
    TextView welcome;

    int usia;
    public static final String MESSAGE_EXTRA = "MESSAGE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        welcome = findViewById(R.id.nama);

        getData = getSharedPreferences("login_data", MODE_PRIVATE);
        if(getData.contains("username")){
            welcome.setText("Welcome, " + getData.getString("username", null));
        }
        else{
            startActivity(new Intent(DetailUser.this, Login.class));
            finish();
        }

        etTanggal = findViewById(R.id.et_tanggal);
        tvUsia = findViewById(R.id.tv_usia);
        spKategori = findViewById(R.id.sp_kategori);
        btnNews = findViewById(R.id.btn_news);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
                Login.setLogout();
                finish();
            }
        });

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cariBerita(v);
            }
        });

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        etTanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker();
                }
            }
        });

        //array spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //jika pada spinner terdapat value, maka set adapter sesuai yg di define di atas
        if(spKategori != null){
            spKategori.setAdapter(adapter);
        }
    }

    //method tanggal
    public void showDatePicker(){
        DialogFragment dateFragment = new com.example.newsappremastered.DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "date-picker");
    }

    public void processedDatePickerResult(int day, int month, int year){
        String day_string = Integer.toString(day);
        String month_string = Integer.toString(month);
        String year_string = Integer.toString(year);
        String dateMessage = day_string + "/" + month_string + "/" + year_string;
        etTanggal.setText(dateMessage);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        usia = (currentYear - year);
        String strUsia = Integer.toString(usia);
        tvUsia.setText(strUsia);
    }

    public void cariBerita(View v){
        Intent intent = new Intent(DetailUser.this, NewsList.class);
        String message = spKategori.getSelectedItem().toString();
        int message2 = usia;
        intent.putExtra(MESSAGE_EXTRA, message);
        intent.putExtra("usiaUser", message2);
        startActivityForResult(intent, 1);
    }
}