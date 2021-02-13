package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstAuthActivity extends AppCompatActivity {
    Intent intent;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        if(SaveSharedPreference.getUserID(context).length() == 0){
            intent = new Intent(context,TeamSelect.class);
            startActivity(intent);
            this.finish();
        }
        else{
            intent = new Intent(context,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
