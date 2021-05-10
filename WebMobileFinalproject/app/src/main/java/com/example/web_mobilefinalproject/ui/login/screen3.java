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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class screen3 extends AppCompatActivity {
    private final String TAG="screen3";
    private LineChart mchart;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);
        Bundle bundle = getIntent().getExtras(); //getting all values from previous slide
        String btcamtt = bundle.getString("btcamt");
        String balance = bundle.getString("remaining");
        String init = bundle.getString("initial");
        String remaining = bundle.getString("defaultval");
        TextView bal2 = (TextView) findViewById(R.id.balance); //bal2 textview= investable balance
        TextView sellamt = findViewById(R.id.sellamt); // sellamt textview = sellamt
        TextView buyamt = findViewById(R.id.buyamt); //buyamt textview = buyamt
        TextView btcbal = findViewById(R.id.balance2); // btcball textview = balance2
        TextView tboxsell = findViewById(R.id.sellamt); //tbox sell textview = sellamt
        final Integer[] incrementer = {0}; //setting incrementer for chart
        Button returnoverview = findViewById(R.id.returnoverview); //setting returnoverview button to returnoverview
        Button buy = findViewById(R.id.buy); //buy button = buy
        Button sell = findViewById(R.id.sell); //sell button = sell
        mchart=(LineChart)findViewById(R.id.charty); //declaring chart
        //Double remainstr;
        String initial = balance; //setting initial = to remaining balance
        final Double[] btcamt = {0.00000}; //setting btc amt to 0
        final Double[] initialbtc = {0.00000}; //setting initial btc amt to 0

        if (init != null) { //if initial is not null, set remaining balance to initial
            double re = Double.parseDouble(remaining);
            initial = balance;
            balance = remaining;
            String remain2 = String.format("%.2f", re);
            bal2.setText("$" + remain2);
        } else { //otherwise, initial = balance, set remaining balance to initial
            initial = balance;
            double re2 = Double.parseDouble(initial);
            String remain3 = String.format("%.2f", re2);
            bal2.setText("$" + remain3);
        }
        if (btcamtt != null) { // if btcamtt is not null, btc3 is set to btcamtt, btc balance textview  updated
            double btc2 = Double.parseDouble(btcamtt);
            btc2 = btc2 + btcamt[0];
            String btc3 = String.format("%.6f", btc2);
            btcbal.setText(btc3);
        }

        Log.i("INFORMATIONS screen 3", " BTC: " + btcamtt + " REMAIN: " + balance + " INITIAL: " + initial); //displaying information in log
        final Double[] numbal = {Double.parseDouble(balance)}; //double numbal = balance

        String finalInitial = initial; // setting final initial = initial
        returnoverview.setOnClickListener(new View.OnClickListener() { //click listener for returning to account overview
            public void onClick(View v) {
                String tboxamt = (btcbal.getText().toString()); //setting tbox amt to == btcbal
                if (!TextUtils.isEmpty(btcbal.getText().toString())) { // if btcbal is empty, parse through tbox and send numbal, tboxamt, and final initial back to screen 2
                    double btc2 = Double.parseDouble(tboxamt);
                    String btc3 = String.format("%.6f", btc2);
                    Log.i("SC 3 tboxxx", String.valueOf(tboxamt));
                    String finalbal = String.valueOf(numbal[0]);
                    valreturns(finalbal, btc3, finalInitial);
                } else { // otherwise, btcbal is set to 0 and return btcbal of 0, numbal, and final initial
                    String btc3 = "0";
                    String finalbal = String.valueOf(numbal[0]);
                    valreturns(finalbal, btc3, finalInitial);
                }
                //String finalbal = String.valueOf(numbal[0]);
            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this); //request queue for get request
        final Handler handler = new Handler(); //handler to refresh screen
        handler.postDelayed(new Runnable() { //update btc price function every 1 second -> price is not live data (api rate limits) approx every 1 minute per change in price
            @Override
            public void run() {
                long unixTime = System.currentTimeMillis() / 1000L; //getting current time in unix
                String uni = Long.toString(unixTime); // setting unix time to string
                String url = "https://api.lunarcrush.com/v2?data=assets&key=ibd18qa5h5dvo8m7571wj&symbol=BTC&interval=hour&start="+uni; //get request url + unix time to get live (1 minute delayed) data
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try { //json get request try block
                                    String btcfinal; //declaring btcfinal var

                                    //=-=-=-parsing btc data and setting parsed value to = btcfinal
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

                                        ///=-- seting chart
                                        ArrayList<Entry> yValues = new ArrayList<>();
                                        yValues.add(new Entry(incrementer[0],Float.parseFloat(btcfinal))); //array list set to include incrementer, btcfinal price pair
                                        LineDataSet set1 = new LineDataSet(yValues,"BitCoin Price");
                                        //set1.setFillAlpha(110);
                                        ArrayList<ILineDataSet> datasets=new ArrayList<>();
                                        datasets.add(set1);
                                        LineData data=new LineData(datasets);
                                        mchart.setData(data); //setting chart data
                                        incrementer[0] +=1; //incrementing
                                        mchart.notifyDataSetChanged(); //notifying chart data has changed and refreshing
                                        mchart.invalidate();

                                        sell.setOnClickListener(new View.OnClickListener() { //click listener for sell button
                                            public void onClick(View V) {
                                                Context context = getApplicationContext(); //getting context for toast
                                                int duration = Toast.LENGTH_SHORT;
                                                if (!TextUtils.isEmpty(tboxsell.getText().toString())) { //if tboxsell is not empty
                                                    if (!TextUtils.isEmpty(btcbal.getText().toString()) && Double.parseDouble(btcbal.getText().toString()) > 0) { // if btcbal is not empty, and btcbal > 0
                                                        double btc2 = Double.parseDouble(btcbal.getText().toString()); // btc2 = btcbal parsed
                                                        String btc3 = String.format("%.6f", btc2); //btc 3 set to btc2
                                                        double tboxamt = Double.parseDouble(tboxsell.getText().toString()); // tboxamt = tboxsell
                                                        if (btc2 >= 0 && tboxamt <= btc2 && tboxamt != 0) { // if btc2 > 0 and tboxamt <- btc2 and tboxamt != 0
                                                            String tboxstr = String.format("%.6f", tboxamt); //tboxstr = tboxamt
                                                            CharSequence text3 = "Succsessfully Sold " + tboxstr + " BTC"; // success message revealed
                                                            Toast failedval2 = Toast.makeText(context, text3, duration);
                                                            failedval2.show();
                                                            Log.i("Succsessful Sale", "Error checker -=- BTC entered SOLD");
                                                            btc2 = btc2 - tboxamt; //btc2 set to btc2 - tboxamt
                                                            numbal[0] = tboxamt * btcpricee + numbal[0]; //balance updated to tboxamt * btcprice + prebious balance
                                                            String finalbtc = String.format("%.6f", btc2); //final btc = btc2
                                                            btcbal.setText(finalbtc); //btcbalance set to final btc
                                                            String finalbal = String.format("%.2f", numbal[0]); //remaining balance set to numbal
                                                            bal2.setText('$' + finalbal); //setting remaing balance text

                                                        }
                                                        else if (tboxamt != 0){ //amount of btc entered is larger than amount of btc owned error message displayed
                                                            String finalbtc = String.valueOf(btcamt[0]);
                                                            finalbtc = String.format("%.6f", btcamt);
                                                            CharSequence text3 = "Only " + btcbal.getText().toString() + " BTC available to sell"; // success message revealed
                                                            Toast failedval2 = Toast.makeText(context, text3, duration);
                                                            failedval2.show();
                                                            Log.i("Unsuccsessful Sale", "Error checker -=- Not Enough BTC in posession ");
                                                        }
                                                        else { // amount entered is 0, error message displayed
                                                            String finalbtc = String.valueOf(btcamt[0]);
                                                            finalbtc = String.format("%.6f", btcamt);
                                                            CharSequence text3 = "Please enter an amount greater than 0! " + btcbal.getText().toString() + " BTC available to sell"; // 0 entered by user
                                                            Toast failedval2 = Toast.makeText(context, text3, duration);
                                                            failedval2.show();
                                                            Log.i("Unsuccsessful Sale", "Error checker -=-  Not Enough BTC in posession ");
                                                        }
                                                    }
                                                    else { // amount entered is 0, error message displayed different case from above (different loop)
                                                        String finalbtc = String.valueOf(btcamt[0]);
                                                        finalbtc = String.format("%.6f", btcamt);
                                                        CharSequence text3 = "Please enter an amount greater than 0! " + btcbal.getText().toString() + " BTC available to sell"; // 0 entered by user
                                                        Toast failedval2 = Toast.makeText(context, text3, duration);
                                                        failedval2.show();
                                                        Log.i("Unsuccsessful Sale", "Error checker -=-  Not Enough BTC in posession ");
                                                    }
                                                }
                                                else { //else for tbox being empty, error message displayed
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
                                                    Context context = getApplicationContext(); //getting context for toast
                                                    int duration = Toast.LENGTH_SHORT;
                                                    if (!TextUtils.isEmpty(buyamt.getText().toString())) { // checking if user entered a value in buy text box
                                                        String sellamtval = buyamt.getText().toString(); // setting sellamt val to string value of user entered value
                                                        double amtt = Double.parseDouble(sellamtval); // setting amtt to = double of user entered value
                                                        String balancio = String.valueOf(amtt); // string balancio = to string value of user entered value
                                                        if (amtt <= numbal[0] && amtt != 0) { // if user entered value is less than or = to current balance and not = to 0
                                                            String numpurchase = String.format("%.2f", amtt); //numpurchase = amtt user entered
                                                            CharSequence text2 = "Succsessfully Purchased $" + numpurchase + " Worth of BTC"; // success message revealed
                                                            Toast failedval2 = Toast.makeText(context, text2, duration);
                                                            failedval2.show();
                                                            Double remaining = numbal[0] - amtt; // setting remaining balance = to balance - amt purchased
                                                            String remainstr = String.format("%.2f", remaining);
                                                            // String remainstr = String.valueOf(remaining); // remainstr = remainder as string
                                                            bal2.setText('$' + remainstr); // setting bal2 (display for remaining amt of money in text view) to = remainstr
                                                            Log.i("Successfully purchased", "Error checker " + remainstr);
                                                            double btcdub = Double.parseDouble(btcprice); //btcdub = btc price
                                                            btcamt[0] = amtt / btcdub + initialbtc[0]; //btc amt set to USD amt user entered / btcprice + initialbtcamt
                                                            numbal[0] = numbal[0] - amtt; // remaining balance = remaining balance - amtt in usd user eneted
                                                            initialbtc[0] = initialbtc[0] + btcamt[0];
                                                            String finalbtc = String.format("%.6f", btcamt[0]); // final btc set to btcamt
                                                            Log.i("Successfully purchased", "Error checker " + finalbtc); //log success
                                                            if (btcamtt != null) { //if btcamtt imported from screen 2 not null, set btcbal to = btcamtt + btcamt
                                                                double btc2 = Double.parseDouble(btcamtt);
                                                                btc2 = btc2 + btcamt[0];
                                                                finalbtc = String.format("%.6f", btc2);
                                                                btcbal.setText(finalbtc);
                                                            }

                                                            else { //otherwise btcbalance = amt of btc user just pruchased
                                                                btcbal.setText(finalbtc);
                                                            }

                                                        }
                                                        if (amtt == 0) { //user entered 0, display error message
                                                            CharSequence text2 = "Please enter a value greater than 0!";
                                                            Toast failedval2 = Toast.makeText(context, text2, duration);
                                                            failedval2.show();
                                                            Log.i("Zero Entered", "Error checker" + balancio);
                                                        }
                                                         else if (amtt > numbal[0]) { //user entered amount larger than they can afford, display error message
                                                            CharSequence text2 = "You don't have enough available balance to buy that much BTC with! Please sell some BTC or enter a lower amount then retry.";
                                                            Toast failedval2 = Toast.makeText(context, text2, duration);
                                                            failedval2.show();
                                                            Log.i("Not Enough Money", "Error checker");
                                                        }

                                                    }
                                                    else { //user did not enter an amount, display error message
                                                        CharSequence text2 = "Please enter an amount in USD you would like to buy BTC with";
                                                        Toast failedval2 = Toast.makeText(context, text2, duration);
                                                        failedval2.show();
                                                        Log.i("No Amount Entered", "Error checker");
                                                    }
                                                }
                                                else {//user doesn't have any money to buy btc with, display error message
                                                    Context context = getApplicationContext();
                                                    CharSequence text2 = "You don't have any available balance to buy with! Please sell some BTC then retry.";
                                                    int duration = Toast.LENGTH_SHORT;
                                                    Toast failedval2 = Toast.makeText(context, text2, duration);
                                                    failedval2.show();
                                                    Log.i("OUT OF MONEY bal check", "Error checker");
                                                }

                                            }
                                        });

                                    };

                                } catch (JSONException e) { //unexpected error
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
                requestqueue.add(objectRequest); //get request queue adding object request
                handler.postDelayed(this, 1000); //handler delay 1 second refresh
            }

        }, 1000);

    }
    public void valreturns (String available, String btcamt, String init){ //method that returns available balance, btcamt,initial balance to screen 2
        Intent intent = new Intent(screen3.this, screen2.class);
        Bundle bundle = new Bundle();
        intent.putExtra("btcamt", btcamt);
        intent.putExtra("initial", init);
        intent.putExtra("available", available);
        startActivity(intent);
    }

}