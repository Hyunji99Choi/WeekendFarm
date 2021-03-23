package com.example.edrkr.subpage;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edrkr.R;
import com.google.android.material.textfield.TextInputLayout;

public class subpage_userIdnetChange extends AppCompatActivity {

    TextInputLayout namelayout;
    EditText name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage_userident_change);



    }
}
