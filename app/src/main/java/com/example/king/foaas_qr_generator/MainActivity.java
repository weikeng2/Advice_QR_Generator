package com.example.king.foaas_qr_generator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * main Screen for app.
 */
public class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "FOAAS_QR_Generator:Main";
    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    /** URL FUCKER */
    private static String url = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";
    private static String aurl = "https://api.adviceslip.com/advice";
    private static String advice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);


        /* The QR Button */
        final Button startQRCall = findViewById(R.id.startQRCall);
        startQRCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)
            {
                Log.d(TAG, "Start QR button clicked");
                startAdviceAPICall();
                advice.replaceAll(" ", "%20");

                url += advice;
                startAPICall();

                // Displays the QRCode with the msg selected
                ImageView code = findViewById(R.id.QrDisplay);
                Picasso.with(code.getContext()).load(url).into(code);
            }
        });

    }



    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "QR Code Generated");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void startAdviceAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, aurl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "Advice gotten");
                            advice = response.toString();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
