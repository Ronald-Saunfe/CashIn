package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cashin.GlobalVariable;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.example.cashin.messageAdapter;
import com.example.cashin.messageModel;
import com.example.cashin.myInvestmentAdapterIE;
import com.example.cashin.myInvestmentsModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Messages extends AppCompatActivity implements OkHTTPhandler.onresponseLinstener{

    private RecyclerView recyclemss;
    private ProgressBar pbMss;
    private int initSize;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<messageModel> myDataset = new ArrayList<>();
    private int Rertycount =0;
    HashMap<String,String> payload = new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        OkHTTPhandler.setOnResponseListener(this);

        recyclemss = findViewById(R.id.recyclemss);
        pbMss = findViewById(R.id.pbMss);

        recyclemss.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclemss.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new messageAdapter(myDataset);
        recyclemss.setAdapter(mAdapter);

        loadMessages();
    }

    private void loadMessages(){
        payload.put("email", GlobalVariable.GlobEmail);
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/getMessage",
                0,
                pbMss,
                this,
                null);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if(id==0){
            try {
                String code = response.getString("code");

                final String Cod = code;
                if (Cod.equals("200")){
                    myDataset.clear();
                    String availableInv = response.getString("response1");
                    availableInv = availableInv.replace("[", "");
                    availableInv = availableInv.replace("]", "");
                    System.out.println("======<<>>>"+availableInv);
                    final List<String> strng = new ArrayList<String>(Arrays.asList(availableInv.split("\\},")));

                    initSize = mAdapter.getItemCount();
                    for (String val:strng) {
                        int valLength = val.length();
                        if(!val.substring(valLength-1, valLength).contentEquals("}")) {
                            val +="}";
                        }
                        System.out.println(">>>>>>=====================response==========="+val);
                        JSONObject res = new JSONObject(val);
                        String message = res.getString("email_buyer_of_investment")+" has puchased your investment price $"
                                +res.getString("investmentPrice")+". You have made profit of $"+res.getString("amount_transacted_seller1");
                        String date = (res.getString("date_time_transacted"));
                        myDataset.add(new messageModel(date  ,message, ""));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemRangeInserted(initSize,  mAdapter.getItemCount()-1);
                        }
                    });

                    String availableInv1 = response.getString("response1");
                    availableInv1 = availableInv1.replace("[", "");
                    availableInv1 = availableInv1.replace("]", "");
                    System.out.println("======<<>>>"+availableInv1);
                    final List<String> strng1 = new ArrayList<String>(Arrays.asList(availableInv1.split("\\},")));

                    initSize = mAdapter.getItemCount();
                    for (String val:strng1) {
                        int valLength = val.length();
                        if(!val.substring(valLength-1, valLength).contentEquals("}")) {
                            val +="}";
                        }
                        System.out.println(">>>>>>=====================response==========="+val);
                        JSONObject jsonObjInv = new JSONObject(val);
                        String message = jsonObjInv.getString("email_buyer_of_investment")+" has puchased your investment price $"
                                +jsonObjInv.getString("investmentPrice")+". You have made profit of $"+jsonObjInv.getString("amount_transacted_seller1");
                        String date = (jsonObjInv.getString("date_time_transacted"));
                        myDataset.add(new messageModel(date  ,message, ""));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemRangeInserted(initSize,  mAdapter.getItemCount()-1);
                        }
                    });
                }
            }
            catch (JSONException r){

            }
        }
    }
}
