package com.example.web_mobilefinalproject.ui.login;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.web_mobilefinalproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class screen3 extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);
        Bundle bundle = getIntent().getExtras();
        String btcamtt = bundle.getString("btcamt");
        String balance = bundle.getString("remaining");
        String init = bundle.getString("initial");
        String remaining = bundle.getString("defaultval");
        TextView bal2 = (TextView) findViewById(R.id.balance);
        TextView sellamt = findViewById(R.id.sellamt);
        TextView buyamt = findViewById(R.id.buyamt);
        TextView btcbal = findViewById(R.id.balance2);
        TextView tboxsell = findViewById(R.id.sellamt);
        Button returnoverview = findViewById(R.id.returnoverview);
        Button buy = findViewById(R.id.buy);
        Button sell = findViewById(R.id.sell);
        String buyamtval;
        Double remainstr;
        String initial = balance;
        final Double[] btcamt = {0.00000};
        final Double[] initialbtc = {0.00000};
        if (init != null) {
            double re = Double.parseDouble(remaining);
            initial = balance;
            balance = remaining;
            String remain2 = String.format("%.2f", re);
            bal2.setText("$" + remain2);
        } else {
            initial = balance;
            double re2 = Double.parseDouble(initial);
            String remain3 = String.format("%.2f", re2);
            bal2.setText("$" + remain3);
        }
        if (btcamtt != null) {
            double btc2 = Double.parseDouble(btcamtt);
            btc2 = btc2 + btcamt[0];
            String btc3 = String.format("%.6f", btc2);
            btcbal.setText(btc3);
        }


        Log.i("INFORMATIONS screen 3", " BTC: " + btcamtt + " REMAIN: " + balance + " INITIAL: " + initial);
        final Double[] numbal = {Double.parseDouble(balance)};


        String finalInitial = initial;
        returnoverview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String tboxamt = (btcbal.getText().toString());
                if (!TextUtils.isEmpty(btcbal.getText().toString())) {
                    double btc2 = Double.parseDouble(tboxamt);
                    String btc3 = String.format("%.6f", btc2);
                    Log.i("SC 3 tboxxx", String.valueOf(tboxamt));
                    String finalbal = String.valueOf(numbal[0]);
                    valreturns(finalbal, btc3, finalInitial);
                } else {
                    String btc3 = "0";
                    String finalbal = String.valueOf(numbal[0]);
                    valreturns(finalbal, btc3, finalInitial);
                }
                String finalbal = String.valueOf(numbal[0]);

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() { //update btc price function every 1 second -> price is not live data (api rate limits) approx every 1 minute change in price
            @Override
            public void run() {
                long unixTime = System.currentTimeMillis() / 1000L;
                String uni = Long.toString(unixTime);
                String url = "https://api.lunarcrush.com/v2?data=assets&key=ibd18qa5h5dvo8m7571wj&symbol=ETH&interval=minute&start="+uni;
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String btcfinal;
                                    List<String> prices = new ArrayList<String>();
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        prices.add(jsonArray.getJSONObject(i).optString("price"));
                                        String btcprice = String.valueOf(prices.get(0));
                                        Double btcpricee = Double.parseDouble(btcprice);
                                        btcfinal = String.format("%.2f", btcpricee);
                                        // btcfinal = ('$' + btcprice);
                                        Log.e("BTC PRICE", String.valueOf(btcfinal));
                                        TextView btcprecio = (TextView) findViewById(R.id.btcprice2);
                                        btcprecio.setText("$" + btcfinal);
                                        double btcprice2 = Double.parseDouble(btcprice);
                                        sell.setOnClickListener(new View.OnClickListener() { //click listener for sell button
                                            public void onClick(View V) {
                                                Context context = getApplicationContext();
                                                int duration = Toast.LENGTH_SHORT;
                                                if (!TextUtils.isEmpty(tboxsell.getText().toString())) {
                                                    if (!TextUtils.isEmpty(btcbal.getText().toString()) && Double.parseDouble(btcbal.getText().toString()) > 0) {
                                                        double btc2 = Double.parseDouble(btcbal.getText().toString());
                                                        String btc3 = String.format("%.6f", btc2);
                                                        double tboxamt = Double.parseDouble(tboxsell.getText().toString());
                                                        if (btc2 >= 0 && tboxamt <= btc2 && tboxamt != 0) {
                                                            CharSequence text3 = "Succsessfully Sold BTC"; // success message revealed
                                                            Toast failedval2 = Toast.makeText(context, text3, duration);
                                                            failedval2.show();
                                                            Log.i("HUGEEE Succsessful Sale", "Error checker -=- BTC entered SOLD");
                                                            btc2 = btc2 - tboxamt;
                                                            numbal[0] = tboxamt * btcpricee + numbal[0];
                                                            String finalbtc = String.format("%.6f", btc2);
                                                            btcbal.setText(finalbtc);
                                                            String finalbal = String.format("%.2f", numbal[0]);
                                                            bal2.setText('$' + finalbal);

                                                        }
                                                    } else {
                                                        String finalbtc = String.valueOf(btcamt[0]);
                                                        finalbtc = String.format("%.6f", btcamt);
                                                        CharSequence text3 = finalbtc + " BTC available to sell"; // success message revealed
                                                        Toast failedval2 = Toast.makeText(context, text3, duration);
                                                        failedval2.show();
                                                        Log.i("Unsuccsessful Sale", "Error checker -=- 0 BTC in posession ");
                                                    }
                                                } else {
                                                    CharSequence text4 = "Please enter the amount of BTC you would like to sell"; // success message revealed
                                                    Toast failedval2 = Toast.makeText(context, text4, duration);
                                                    failedval2.show();
                                                    Log.i("Unsuccessful sell", "Error checker -=- No sale amt entered");
                                                }
                                            }
                                        });
                                        buy.setOnClickListener(new View.OnClickListener() { //click listener for buy button

                                            public void onClick(View V) {

                                                if (numbal[0] > 0) { //checking if balance greater than 0
                                                    Context context = getApplicationContext();
                                                    int duration = Toast.LENGTH_SHORT;
                                                    if (!TextUtils.isEmpty(buyamt.getText().toString())) { // checking if user entered a value in buy text box
                                                        String sellamtval = buyamt.getText().toString(); // setting sellamt val to string value of user entered value
                                                        double amtt = Double.parseDouble(sellamtval); // setting amtt to = double of user entered value
                                                        String balancio = String.valueOf(amtt); // string balancio = to string value of user entered value
                                                        if (amtt <= numbal[0] && amtt != 0) { // if user entered value is less than or = to current balance and not = to 0
                                                            String numpurchase = String.format("%.2f", amtt);
                                                            CharSequence text2 = "Succsessfully Purchased $" + numpurchase + " Worth of BTC"; // success message revealed
                                                            Toast failedval2 = Toast.makeText(context, text2, duration);
                                                            failedval2.show();
                                                            Double remaining = numbal[0] - amtt; // setting remaining = to balance - amt purchased
                                                            String remainstr = String.format("%.2f", remaining);
                                                            // String remainstr = String.valueOf(remaining); // remainstr = remainder as string
                                                            bal2.setText('$' + remainstr); // setting bal2 (display for remaining amt of money in text view) to = remainstr
                                                            Log.i("Successfully purchased", "Error checker " + remainstr);
                                                            double btcdub = Double.parseDouble(btcprice);
                                                            btcamt[0] = amtt / btcdub + initialbtc[0];
                                                            numbal[0] = numbal[0] - amtt;
                                                            String finalbtc = String.valueOf(btcamt[0]);
                                                            initialbtc[0] = initialbtc[0] + btcamt[0];
                                                            finalbtc = String.format("%.6f", btcamt);

                                                            Log.i("Successfully purchased", "Error checker " + finalbtc);
                                                            if (btcamtt != null) {
                                                                double btc2 = Double.parseDouble(btcamtt);
                                                                btc2 = btc2 + btcamt[0];
                                                                finalbtc = String.format("%.6f", btc2);
                                                                btcbal.setText(finalbtc);
                                                            } else {
                                                                btcbal.setText(finalbtc);
                                                            }

                                                        }
                                                        if (amtt == 0) {
                                                            CharSequence text2 = "Please enter a value greater than 0!";
                                                            Toast failedval2 = Toast.makeText(context, text2, duration);
                                                            failedval2.show();
                                                            Log.i("Zero Entered", "Error checker" + balancio);
                                                        } else if (amtt > numbal[0]) {
                                                            CharSequence text2 = "You don't have enough available balance to buy that much BTC with! Please sell some BTC or enter a lower amount then retry.";
                                                            Toast failedval2 = Toast.makeText(context, text2, duration);
                                                            failedval2.show();
                                                            Log.i("Not Enough Money", "Error checker");
                                                        }
                                                    } else {
                                                        CharSequence text2 = "Please enter an amount in USD you would like to buy BTC with";
                                                        Toast failedval2 = Toast.makeText(context, text2, duration);
                                                        failedval2.show();
                                                        Log.i("No Amount Entered", "Error checker");
                                                    }
                                                } else {
                                                    Context context = getApplicationContext();
                                                    CharSequence text2 = "You don't have any available balance to buy with! Please sell some BTC then retry.";
                                                    int duration = Toast.LENGTH_SHORT;
                                                    Toast failedval2 = Toast.makeText(context, text2, duration);
                                                    failedval2.show();
                                                    Log.i("OUT OF MONEY bal check", "Error checker");
                                                }

                                            }
                                        });

                                    }
                                    ;
                                } catch (JSONException e) {
                                    Log.e("ERRRORRR", "unexpected JSON exception", e);

                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("RESPONSE", error.toString());
                            }
                        }
                );
                requestqueue.add(objectRequest);
                handler.postDelayed(this, 1000);
            }

        }, 1000);

    }
    public void valreturns (String available, String btcamt, String init){
        Log.i("avail", available);
        Intent intent = new Intent(screen3.this, screen2.class);
        Bundle bundle = new Bundle();
        intent.putExtra("btcamt", btcamt);
        Log.i("DOEDO", btcamt);
        intent.putExtra("initial", init);
        intent.putExtra("available", available);
        startActivity(intent);
    }
}
