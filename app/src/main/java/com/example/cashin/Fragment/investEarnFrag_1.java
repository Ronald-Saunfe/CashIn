package com.example.cashin.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cashin.Activity.InvestEarn;
import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.GlobalVariable;
import com.example.cashin.InvestmentAdapterIE;
import com.example.cashin.InvestmentsModel;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
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


public class investEarnFrag_1 extends Fragment implements OkHTTPhandler.onresponseLinstener{
    //navigation btn for next and skip
    private Button btnNextIEf;
    private Button btnSkipIEf;
    //back button in the toolbar
    private ImageButton btnBackInvestEarn1;
    private ProgressBar pbIE;
    private int Rertycount=0;
    private View Fview;

    private RecyclerView recycleVIE;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<InvestmentsModel> myDataset = new ArrayList<>();


    HashMap<String,String> payload = new HashMap<String,String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invest_earn_frag_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //navigation btn for next and skip
        btnNextIEf = view.findViewById(R.id.btnNextIEf);
        btnSkipIEf = view.findViewById(R.id.btnSkipIEf);
        pbIE = view.findViewById(R.id.pbIE);
        btnBackInvestEarn1 = view.findViewById(R.id.btnBackInvestEarn1);
        Fview = view;
        OkHTTPhandler.setOnResponseListener(this);

        //onclick for next button
        //call fragment invest and earn2
        btnNextIEf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invetEarnFrag_2 frag = new invetEarnFrag_2();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayoutInvestEarn, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                System.out.println("========global var==="+GlobalVariable.GlobInvestmentPrice);
            }
        });

        //onclick for skip button
        btnSkipIEf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                investEarnFrag_1_1 frag = new investEarnFrag_1_1();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayoutInvestEarn, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //return to main menu activiy
        btnBackInvestEarn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Navigation_Main.class);
                startActivity(intent);
            }
        });

        //recycle list of investment
        recycleVIE =view.findViewById(R.id.recycleVIE);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycleVIE.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recycleVIE.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new InvestmentAdapterIE(myDataset);
        recycleVIE.setAdapter(mAdapter);
        FetchAvailableBaskets();
    }

    //fetch from server available baskets
    private void FetchAvailableBaskets(){
        OkHTTPhandler.makepayload(null,
                "https://us-central1-cashin-270220.cloudfunctions.net/getAvailableInvestments",
                0,
                pbIE,
                null,
                this);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0){
            try{
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
                        float price = Float.valueOf(jsonObjInv.getString("Price"));
                        float comission = Float.valueOf(jsonObjInv.getString("Commission"));
                        float quantity = Float.valueOf(jsonObjInv.getString("Quantity"));
                        myDataset.add(new InvestmentsModel(price,comission, quantity));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            btnSkipIEf.setEnabled(true);
                            btnNextIEf.setEnabled(true);
                        }
                    });

                }
            }
            catch (JSONException e){

            }
        }
    }
}
