package com.example.web_mobilefinalproject.ui.login;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class screen2 extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        Button nextscreen = findViewById(R.id.btc); // this button goes to btc overview
        Bundle bundle = getIntent().getExtras(); //getting values passed back and forth from screen 1 and 3
        String sbal = bundle.getString("starting");
        final String[] initial = {bundle.getString("initial")};
        String btcnum = bundle.getString("btcamt");
        String avail = bundle.getString("available");
        TextView starting = (TextView) findViewById(R.id.startingamt); // textview variable for initial balance
        TextView investment = (TextView) findViewById(R.id.portvalue); // textview variable for protfolio value
        TextView investable = (TextView) findViewById(R.id.remainingval); // textview variable for available balance
        TextView pcnt = findViewById(R.id.percent); // textview variable for percent return
        TextView ownedbtc = (TextView) findViewById(R.id.btcowned); // text view variable for amt of btc owned
        TextView npl = findViewById(R.id.npl); // textview variable for net profit / loss
        String defaultval=avail; // setting default val == to available

        Log.i("INFORMATIONS in scr 2" , "BTC: "+ btcnum + "REMAIN:" + avail + "INIT:" + initial[0]); // logging information in screen 2

        if(avail!= null){ // if avail is not null, set available balance text view to == avail
            double availdub = Double.parseDouble(avail);
            String availfinal = String.format("%.2f", availdub);
            investable.setText("$"+availfinal);
        }
        else { // otherwise, our available balance is our starting balance
            investable.setText("$"+sbal+".00");
        }
        nextscreen.setOnClickListener(new View.OnClickListener() { //click listener for btc screen, passing available balance, initial balance, btc owned amt, and default val
            public void onClick(View v) {
                    sendvals(avail, initial[0], btcnum, defaultval);
                }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this); //making request queue for get request
        final Handler handler = new Handler(); // new handler for updating screen
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//update btc price function every 1 second -> price is not live data (api rate limits) approx every 1 minute per change in price
                long unixTime = System.currentTimeMillis() / 1000L; //getting current time in unix
                String uni = Long.toString(unixTime); // setting unix time to string
                String url = "https://api.lunarcrush.com/v2?data=assets&key=ibd18qa5h5dvo8m7571wj&symbol=BTC&interval=hour&start="+uni; //get request url + unix time to get live (1 minute delayed) data
                String finalInitial1 = initial[0];
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try { //json get request try block
                                    if(sbal!=null) {// if starting balance is not null, initial amt is set to starting balance
                                        initial[0] = sbal;
                                        String finalInitial = initial[0];
                                    }
                                    else{ // otherwise initial is initial
                                        initial[0] = initial[0];
                                    }
                                    if(finalInitial1 != null && avail != null && btcnum!= null) { // if starting balance is not null and available is not null and btc amt is not null
                                        double start = Double.parseDouble(finalInitial1); // turn starting balance to double
                                        starting.setText("$" + finalInitial1 + ".00"); // setting text for starting amount
                                        String btcfinal;

                                        //=-=-=-parsing btc data and setting parsed value to = btcfinal
                                        List<String> prices = new ArrayList<String>();
                                        List<String> date= new ArrayList<String>();
                                        JSONArray jsonArray = response.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            prices.add(jsonArray.getJSONObject(i).optString("price"));
                                            date.add(jsonArray.getJSONObject(i).optString("date"));
                                            String btcprice = String.valueOf(prices.get(0));
                                            btcfinal = ('$' + btcprice);

                                            double dnum3 = Double.parseDouble(btcnum); //setting double to equal number of btc owned
                                            double dnum = Double.parseDouble(btcprice); // setting double to equal price of btc
                                            double dnum2 = Double.parseDouble(avail); // setting double to investable balance
                                            double totalval = (dnum * dnum3) + dnum2; //totalval = price of btc * number of btc owned + available investable amt
                                            double npll = ((dnum * dnum3) + dnum2) - start; // net profit / loss set to btc amt * btc price + investable balance - starting balance
                                            String totalval2 = String.format("%.2f", totalval); //setting total val to string
                                            investment.setText('$' + totalval2); //setting portfolio value text view
                                            double pcntt = (((totalval - start) / start)*100); // calculating percent return
                                            String pcntfinal = String.format("%.4f", pcntt); // setting percent return to string
                                            String nplfinal = String.format("%.2f", npll); // setting net profit loss to string
                                            pcnt.setText(pcntfinal + "%"); // setting percent return textview to pcntfinal string
                                            npl.setText("$" + nplfinal); // setting net profit loss text view to nplfinal string
                                            String owned = String.format("%.6f", dnum3); // setting owned (btc) to string of dnum3
                                            ownedbtc.setText(owned); //setting owned string to btc amt text view

                                        }
                                    }
                                    else{ // starting balance is null and available is null and btc amt is null
                                        starting.setText("$"+ sbal +".00"); //set starting balance text view to sbal (initial balance)
                                        investment.setText("$" +sbal+".00"); // set portfolio value to sbal (initial balance)
                                    }

                                } catch (JSONException e) { // get request error handler
                                    Log.e("ERRRORRR", "unexpected JSON exception", e);

                                }
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Log.e("RESPONSE", error.toString());
                            }
                        }
                );
                requestqueue.add(objectRequest); //get request queue adding object request
                handler.postDelayed(this, 1000); //handler delay 1 second refresh
            }
        }, 1000);

    }
    public void overview(){ //method goes to screen 3
        Intent intent = new Intent (screen2.this,screen3.class);
        startActivity(intent);
    }

    public void sendvals(String initial, String remain, String btc, String defaultval){ // method that sends initial investment, remaining balance, btc amt, defaultval to next screen
        Intent intent = new Intent (screen2.this,screen3.class);
        Bundle bundle = new Bundle();
        intent.putExtra("initial", initial);
        intent.putExtra("remaining", remain);
        intent.putExtra("btcamt", btc);
        intent.putExtra("defaultval", defaultval);
        startActivity(intent);
    }
}