package com.example.cashin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cashin.Activity.peopleInvestmentCheckout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BottomSheetPI extends BottomSheetDialogFragment {
    private ProgressBar pbBS;
    private LinearLayout lnLPIB;

    private TextView txtPIusernameB, txtPIemailB;

    private int Rertycount=0;

    public static BottomSheetPI newInstance() {
        return new BottomSheetPI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheetpi1, container, false);
        // get the views and attach the listener
        pbBS = view.findViewById(R.id.pbBS);
        lnLPIB = view.findViewById(R.id.lnLPIB);

        txtPIusernameB = view.findViewById(R.id.txtPIusernameB);
        txtPIemailB = view.findViewById(R.id.txtPIemailB);

        txtPIusernameB.setText(GlobalVariable.selectedPIUsername);
        txtPIemailB.setText(GlobalVariable.selectedPIEmail);


        //load investment
        LoadInvestment();
        return view;
    }

    private void LoadInvestment(){
        //make an asynchronous request
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbBS.setVisibility(View.VISIBLE);
            }
        });
        final JSONObject jsonObject=new JSONObject();
        final JSONObject jsonObject1=new JSONObject();
        boolean chkerr=false;
        try {
            jsonObject.put("email",GlobalVariable.selectedPIEmail);//GlobalVariable.GlobEmail
            jsonObject1.put("message",jsonObject);
            System.out.println("----------------------"+jsonObject1);
            chkerr = false;
        } catch (JSONException e) {
            System.out.println("building json error to response=====================================GT 1"+e.getMessage());
            chkerr = true;
            e.printStackTrace();
        }
        //ensure the json is build correctly
        if (chkerr ==false) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject1));
            //build the url
            final Request request = new Request.Builder()
                    .url("https://us-central1-cashin-270220.cloudfunctions.net/loadmyInvestments")
                    .post(body)
                    .build();

            //callback to handle response and error of the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    System.out.println("Failure on response====================================="+e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbBS.setVisibility(View.GONE);
                        }
                    });
                    String errMessage = ResponseError.GetResponse(e.getMessage());
                    if (Rertycount<=3){
                        if (errMessage.equals("Connection failed")){
                            LoadInvestment();
                        }
                        else if(errMessage.equals("Slow internet connection")){
                            LoadInvestment();
                        }
                        else if(errMessage.equals("timeout")){
                            LoadInvestment();
                        }
                        else if(errMessage.equals("client timeout")){
                            LoadInvestment();
                        }
                        else if(errMessage.equals("Try Again")){
                            LoadInvestment();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.myinvPurchased),errMessage,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            LoadInvestment();
                                        }
                                    }).setActionTextColor(Color.parseColor("#003c8f"));

                            snackbar.show();
                        }
                    }
                    else{
                        if (errMessage.equals("Connection failed")){
                            errMessage="Connection failed";
                        }
                        else if(errMessage.equals("Slow internet connection")){
                            errMessage="Slow internet connection";
                        }
                        else if(errMessage.equals("timeout")){
                            errMessage="Timeout";
                        }
                        else if(errMessage.equals("client timeout")){
                            errMessage="Timeout";
                        }
                        else if(errMessage.equals("Try Again")){
                            errMessage="Try again";
                        }
                        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.myinvPurchased).findViewById(R.id.rootProfl),errMessage,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LoadInvestment();
                                    }
                                }).setActionTextColor(Color.parseColor("#003c8f"));

                        snackbar.show();
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbBS.setVisibility(View.GONE);
                        }
                    });

                    final String myResponse = response.body().string();
                    try {
                        System.out.println(myResponse.getClass().getName()+"==================="+myResponse);
                        //convertion of response to json to fetch value
                        JSONObject jsonObj = new JSONObject(myResponse);
                        String code = jsonObj.getString("code");

                        final String Cod = code;
                        if (Cod.equals("200")){
                            String availableInv = jsonObj.getString("response");
                            //check for last record in db
                            if (availableInv.length()>2 ){
                                availableInv = availableInv.replace("[", "");
                                availableInv = availableInv.replace("]", "");
                                System.out.println("======<<>>>"+availableInv);
                                final List<String> strng = new ArrayList<String>(Arrays.asList(availableInv.split("\\},")));

                                for (String val:strng) {
                                    int valLength = val.length();
                                    if(!val.substring(valLength-1, valLength).contentEquals("}")) {
                                        val +="}";
                                    }
                                    System.out.println(">>>>>>=====================response==========="+val);
                                    JSONObject jsonObjInv = new JSONObject(val);
                                    final String price = (jsonObjInv.getString("investmentPrice"));
                                    String purchased = (jsonObjInv.getString("purchased_status"));
                                    final String inv_acc_no= (jsonObjInv.getString("investment_acc_no"));

                                    LayoutInflater inflater1 = LayoutInflater.from(getContext());
                                    final View view = inflater1.inflate(R.layout.people_investment ,null);
                                    TextView txt = view.findViewById(R.id.txtppIPrice);
                                    txt.setText(price);
                                    MaterialButton btn = view.findViewById(R.id.btnPpI);
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            GlobalVariable.GlobSelectedInvestmentCommission = 50;
                                            GlobalVariable.GlobSelectedInvestmentPrice = Float.valueOf(price);
                                            GlobalVariable.GlobSelectedInvestmentQuantity= 20;
                                            GlobalVariable.GlobSelectedBoughtAccessionNo = inv_acc_no;
                                            startActivity(new Intent(getContext(), peopleInvestmentCheckout.class));
                                        }
                                    });
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            lnLPIB.addView(view);
                                        }
                                    });
                                }
                            }
                            else{
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView txt= new TextView(getContext());
                                        txt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                                        txt.setText("No Investment");
                                        lnLPIB.addView(txt);
                                    }
                                });
                            }
                        }
                    }
                    catch (JSONException err){
                        System.out.println("building json after repsonse====================================="+err.getMessage());
                        final JSONException err1 = err;
                        //ude the main ui thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),err1.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }
    }
}