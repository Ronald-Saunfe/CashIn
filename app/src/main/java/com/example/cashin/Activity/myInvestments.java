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
import com.example.cashin.InvestmentAdapterIE;
import com.example.cashin.InvestmentsModel;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
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

public class myInvestments extends AppCompatActivity implements OkHTTPhandler.onresponseLinstener{

    private ProgressBar pbmyInv;

    private RecyclerView recycleVMI;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<myInvestmentsModel> myDataset = new ArrayList<>();
    private int Rertycount =0;
    HashMap<String,String> payload = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_investments);

        pbmyInv = findViewById(R.id.pbmyInv);
        recycleVMI = findViewById(R.id.recycleVMI);
        OkHTTPhandler.setOnResponseListener(this);

        recycleVMI.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleVMI.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new myInvestmentAdapterIE(myDataset);
        recycleVMI.setAdapter(mAdapter);

        FetchMyInvestments();
    }

    private void FetchMyInvestments(){
        payload.put("email",GlobalVariable.GlobEmail);
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/loadmyInvestments",
                0,
                pbmyInv,
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
                    String availableInv = response.getString("response");
                    availableInv = availableInv.replace("[", "");
                    availableInv = availableInv.replace("]", "");
                    System.out.println("======<<>>>"+availableInv);
                    final List<String> strng = new ArrayList<String>(Arrays.asList(availableInv.split("\\},")));

                    final int getItmCount = mAdapter.getItemCount();
                    myDataset.clear();
                    for (String val:strng) {
                        int valLength = val.length();
                        if(!val.substring(valLength-1, valLength).contentEquals("}")) {
                            val +="}";
                        }
                        System.out.println(">>>>>>=====================response==========="+val);
                        JSONObject jsonObjInv = new JSONObject(val);
                        float price = Float.valueOf(jsonObjInv.getString("investmentPrice"));
                        String date_time_invested = (jsonObjInv.getString("date_time_invested"));
                        myDataset.add(new myInvestmentsModel(price  ,date_time_invested,20,0));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            catch (JSONException d){

            }
        }
        else if (id==1){

        }
    }
}
