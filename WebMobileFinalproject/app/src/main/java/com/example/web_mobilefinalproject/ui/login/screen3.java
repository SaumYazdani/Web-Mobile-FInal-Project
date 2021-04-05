package com.example.web_mobilefinalproject.ui.login;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.web_mobilefinalproject.R;

public class screen3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);
        Button returnoverview = findViewById(R.id.returnoverview);
        returnoverview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                overview();
            }
        });
    }
    public void overview(){
        Intent intent = new Intent (screen3.this,screen2.class);
        startActivity(intent);
    }
}