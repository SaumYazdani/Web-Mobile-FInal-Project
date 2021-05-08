package com.example.web_mobilefinalproject.ui.login;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.web_mobilefinalproject.R;

public class screen1 extends AppCompatActivity {
    EditText startingamt;

    String amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        Button nextscreen = findViewById(R.id.overview);
        nextscreen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startingamt = findViewById(R.id.startingbal);
                if(!TextUtils.isEmpty(startingamt.getText().toString())) {
                    amt = startingamt.getText().toString();
                    double amtt = Double.parseDouble(amt);
                    if (amtt < 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "Starting USD Amount must be at least 1";
                        int duration = Toast.LENGTH_LONG;
                        Toast failedval = Toast.makeText(context, text, duration);
                        failedval.show();
                    } else if (amtt == 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "Starting USD amount must be greater than 0!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else {
                        sendstarting();
                        overview();
                    }

                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter the amount in USD you would like to start investing with!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

    }
    public void overview(){
        Intent intent = new Intent (screen1.this,screen2.class);
        startActivity(intent);
    }
    public void sendstarting(){
        String starting = amt;
        Intent intent = new Intent (screen1.this,screen2.class);
        Bundle bundle = new Bundle();
        intent.putExtra("starting", starting);
        startActivity(intent);
    }
}