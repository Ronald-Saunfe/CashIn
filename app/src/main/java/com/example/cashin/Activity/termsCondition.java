package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.cashin.Fragment.SignUpFragment.JSON;

public class termsCondition extends AppCompatActivity implements OkHTTPhandler.onresponseLinstener {

    private TextView txtTM;
    private Request request;
    private ProgressBar pgTM;
    private int Rertycount=0;
    private ImageButton btnBackTerms;
    HashMap<String,String> payload = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        OkHTTPhandler.setOnResponseListener(this);

        txtTM = findViewById(R.id.txtTM);
        pgTM = findViewById(R.id.pgTM);
        btnBackTerms= findViewById(R.id.btnBackTerms);
        btnBackTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSignedUrl();

    }
    //get the signed url
    private void getSignedUrl(){
        payload.put("filename","terms_conditions.txt");
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/get_signed_url",
                0,
                pgTM,
                this,
                null);
    }

    //download content from a url
    private void DownloadTM(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                    Headers responseHeaders = response.headers();
//                    for (int i = 0; i < responseHeaders.size(); i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//                    System.out.println(response.body().string());

                InputStream in = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result, line = reader.readLine();
                result = line;
                while((line = reader.readLine()) != null) {
                    result += line;
                }
                final String results = result;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        pgTM.setVisibility(View.GONE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            txtTM.setText(Html.fromHtml(results, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            txtTM.setText(Html.fromHtml(results));
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0){
            try{
                String code = response.getString("code");

                final String Cod = code;
                if (Cod.equals("200")){
                    DownloadTM(response.getString("reponse"));
                }
            }
            catch (JSONException d){}
        }

    }
}
