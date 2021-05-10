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
        Button nextscreen = findViewById(R.id.overview); // setting next screen button
        nextscreen.setOnClickListener(new View.OnClickListener(){ //seting onclick listener for next screen
            public void onClick(View v) {
                startingamt = findViewById(R.id.startingbal); // setting startingamt variable to text view
                if(!TextUtils.isEmpty(startingamt.getText().toString())) { // if starting amt is not empty, parse the result and turn it into double
                    amt = startingamt.getText().toString();
                    double amtt = Double.parseDouble(amt);
                    if (amtt < 0) { //if amtt (startingamt bal in double form) is greater than zero show error message
                        Context context = getApplicationContext();
                        CharSequence text = "Starting USD Amount must be at least 1";
                        int duration = Toast.LENGTH_LONG;
                        Toast failedval = Toast.makeText(context, text, duration);
                        failedval.show();
                    } else if (amtt == 0) { //if amtt (startingamt bal in double form) is equal to zero show error message
                        Context context = getApplicationContext();
                        CharSequence text = "Starting USD amount must be greater than 0!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else { // Otherwise, start next activity and send starting bal value to next screen
                        sendstarting();
                        overview();
                    }

                }
                else{ // if user is fails to enter a value but hits next screen, show error message
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter the amount in USD you would like to start investing with!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

    }
    public void overview(){ // method will go to next screen
        Intent intent = new Intent (screen1.this,screen2.class);
        startActivity(intent);
    }
    public void sendstarting(){ // method will send starting amt to next screen
        String starting = amt;
        Intent intent = new Intent (screen1.this,screen2.class);
        Bundle bundle = new Bundle();
        intent.putExtra("starting", starting);
        startActivity(intent);
    }
}