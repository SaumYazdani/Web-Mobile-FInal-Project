package com.example.web_mobilefinalproject.ui.login;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class screen2 extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        Button nextscreen = findViewById(R.id.btc);
        Bundle bundle = getIntent().getExtras();
        String sbal = bundle.getString("starting");
        final String[] initial = {bundle.getString("initial")};
        String btcnum = bundle.getString("btcamt");
        String avail = bundle.getString("available");
        TextView starting = (TextView) findViewById(R.id.startingamt);
        TextView investment = (TextView) findViewById(R.id.portvalue);
        TextView investable = (TextView) findViewById(R.id.remainingval);
        TextView pcnt = findViewById(R.id.percent);
        TextView ownedbtc = (TextView) findViewById(R.id.btcowned);
        TextView npl = findViewById(R.id.npl);
        String defaultval=avail;
        Log.i("INFORMATIONS in scr 2" , "BTC: "+ btcnum + "REMAIN:" + avail + "INIT:" + initial[0]);
        if(avail!= null){
            double availdub = Double.parseDouble(avail);
            String availfinal = String.format("%.2f", availdub);
            investable.setText("$"+availfinal);
        }
        else {
            investable.setText("$"+sbal+".00");
        }
        nextscreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    sendvals(avail, initial[0], btcnum, defaultval);
                }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//update btc price function every 1 second -> price is not live data (api rate limits) approx every 1 minute change in price
                long unixTime = System.currentTimeMillis() / 1000L;
                String uni = Long.toString(unixTime);
                String url = "https://api.lunarcrush.com/v2?data=assets&key=ibd18qa5h5dvo8m7571wj&symbol=ETH&interval=hour&start="+uni;
                String finalInitial1 = initial[0];
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(sbal!=null) {
                                        initial[0] = sbal;
                                        String finalInitial = initial[0];
                                    }
                                    else{
                                        initial[0] = initial[0];
                                    }
                                    if(finalInitial1 != null && avail != null && btcnum!= null) {
                                        double start = Double.parseDouble(finalInitial1);
                                        starting.setText("$" + finalInitial1 + ".00");
                                        String btcfinal;
                                        List<String> prices = new ArrayList<String>();
                                        JSONArray jsonArray = response.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            prices.add(jsonArray.getJSONObject(i).optString("price"));
                                            String btcprice = String.valueOf(prices.get(0));
                                            btcfinal = ('$' + btcprice);
                                            double dnum3 = Double.parseDouble(btcnum);
                                            double dnum = Double.parseDouble(btcprice);
                                            double dnum2 = Double.parseDouble(avail);
                                            double totalval = (dnum * dnum3) + dnum2;
                                            double npll = ((dnum * dnum3) + dnum2) - start;
                                            String totalval2 = Double.toString(totalval);
                                            totalval2 = String.format("%.2f", totalval);
                                            investment.setText('$' + totalval2);
                                            double pcntt = (((totalval - start) / start)*100) ;
                                            String pcntfinal = String.format("%.4f", pcntt);
                                            String nplfinal = Double.toString(npll);
                                            pcnt.setText(pcntfinal + "%");
                                            nplfinal = String.format("%.2f", npll);
                                            npl.setText("$" +nplfinal);
                                            String owned = String.format("%.6f", dnum3);
                                            ownedbtc.setText(owned);
                                        }
                                    }
                                    else{
                                        starting.setText("$"+ sbal +".00");
                                        investment.setText("$" +sbal+".00");
                                    }
                                } catch (JSONException e) {
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
                requestqueue.add(objectRequest);
                handler.postDelayed(this, 1000);
            }
        }, 1000);

    }
    public void overview(){
        Intent intent = new Intent (screen2.this,screen3.class);
        startActivity(intent);
    }

    public void sendvals(String initial, String remain, String btc, String defaultval){
        Intent intent = new Intent (screen2.this,screen3.class);
        Bundle bundle = new Bundle();
        intent.putExtra("initial", initial);
        intent.putExtra("remaining", remain);
        intent.putExtra("btcamt", btc);
        intent.putExtra("defaultval", defaultval);
        startActivity(intent);
    }
}