package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.BottomSheetPI;
import com.example.cashin.GlobalVariable;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.example.cashin.purchaseInvestmentAdapter;
import com.example.cashin.purchaseInvestmentModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class purchaseInvestment extends AppCompatActivity implements purchaseInvestmentAdapter.peopleClickListener,
        OkHTTPhandler.onresponseLinstener {

    private RecyclerView recycleVMI;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<purchaseInvestmentModel> myDataset = new ArrayList<>();
    private int Rertycount =0;
    private int paginationPage =0;
    private String last_doc_email="";
    private boolean reachedLastFetch = false;
    private ProgressBar pbPI;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    private int PAGE_SIZE=0;
    private int initSize;

    private boolean slock=false;

    private EditText EtxtseachPI;

    private RecyclerView recyclevwPI;
    private LinearLayout recyclevwPISearch;
    HashMap<String,String> payload = new HashMap<String,String>();

    private String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_investment);

        pbPI = findViewById(R.id.pbPI);
        OkHTTPhandler.setOnResponseListener(this);
        recycleVMI = findViewById(R.id.recyclevwPI);

        EtxtseachPI = findViewById(R.id.EtxtseachPI);

        recyclevwPISearch = findViewById(R.id.recyclevwPISearch);
        recyclevwPI= findViewById(R.id.recyclevwPI);
        //set layout fade animation
        LayoutTransition itemLayoutTransition1 = new LayoutTransition();
        itemLayoutTransition1.setInterpolator(LayoutTransition.DISAPPEARING, new AccelerateInterpolator());
        itemLayoutTransition1.setInterpolator(LayoutTransition.APPEARING, new AccelerateInterpolator());
        //itemLayoutTransition1.setDuration(500); //3/4*time of transition

        ViewGroup av1 = findViewById(R.id.ccPI);
        av1.setLayoutTransition(itemLayoutTransition1);

        recycleVMI.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recycleVMI.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new purchaseInvestmentAdapter(myDataset, this);
        SlideInUpAnimator siua = new SlideInUpAnimator();
        siua.setInterpolator(new AnticipateOvershootInterpolator());
        recycleVMI.setItemAnimator(siua);
        recycleVMI.setAdapter(mAdapter);

        recycleVMI.addOnScrollListener(recyclerViewOnScrollListener);

        EtxtseachPI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                txt = EtxtseachPI.getText().toString();
                //avoid overiding current search using slock
                if (slock==false){
                    if (txt.length() <=0 ){
                        recyclevwPISearch.setVisibility(View.GONE);
                        recyclevwPI.setVisibility(View.VISIBLE);
                    }
                    else{
                        recyclevwPISearch.setVisibility(View.VISIBLE);
                        recyclevwPI.setVisibility(View.GONE);
                        searchPeople();
                    }
                }
            }
        });
        fetchPeople();
    }

    @Override
    public void onPersonClick(int position) {
        GlobalVariable.selectedPIEmail = myDataset.get(position).getEmail();
        GlobalVariable.selectedPIUsername = myDataset.get(position).getUsername();

        BottomSheetPI addPhotoBottomDialogFragment = BottomSheetPI.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "tag");

    }

    //load the people in a pagination fatshion
    private void searchPeople(){
        slock = true;
        payload.put("SearchEmail", EtxtseachPI.getText().toString());
        OkHTTPhandler.makepayload(null,
                "https://us-central1-cashin-270220.cloudfunctions.net/searchPeople",
                0,
                 pbPI,
                this,
                null);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition =((LinearLayoutManager)recycleVMI.getLayoutManager()).findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    if (reachedLastFetch==false){
                        fetchPeople();
                    }
                }
            }
        }
    };

    //load the people in a pagination fatshion
    private void fetchPeople(){
        OkHTTPhandler.makepayload(null,
                "https://us-central1-cashin-270220.cloudfunctions.net/fetchMembersPaginated",
                0,
                pbPI,
                this,
                null);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0){
            try {
                String code = response.getString("code");

                final String Cod = code;
                if (Cod.equals("200")){
                    String availableInv = response.getString("response");
                    //check for last record in db
                    if (availableInv.length()>2 ){
                        reachedLastFetch = false;
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
                            JSONObject jsonObjInv = new JSONObject(val);
                            String username = (jsonObjInv.getString("Username"));
                            String email = (jsonObjInv.getString("Email"));
                            myDataset.add(new purchaseInvestmentModel(username  ,email,""));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemRangeInserted(initSize,  mAdapter.getItemCount()-1);
                                last_doc_email = myDataset.get(mAdapter.getItemCount()-1).getEmail();
                                System.out.println(">>>>>>=====================last email==========="+last_doc_email);
                            }
                        });
                    }
                    else{
                        reachedLastFetch=true;
                    }
                }
            }
            catch (JSONException s){

            }
        }
        else if (id==1){
            try {
                String code = response.getString("code");

                final String Cod = code;
                if (Cod.equals("200")){
                    String availableInv = response.getString("response");
                    availableInv = availableInv.replace("[", "");
                    availableInv = availableInv.replace("]", "");
                    System.out.println("======<<>>>"+availableInv);
                    final List<String> strng = new ArrayList<String>(Arrays.asList(availableInv.split("\\},")));
                    recyclevwPISearch.removeAllViews();
                    for (String val:strng) {
                        int valLength = val.length();
                        if(!val.substring(valLength-1, valLength).contentEquals("}")) {
                            val +="}";
                        }
                        System.out.println(">>>>>>=====================response==========="+val);
                        JSONObject jsonObjInv = new JSONObject(val);
                        final String username = (jsonObjInv.getString("Username"));
                        final String email = (jsonObjInv.getString("Email"));
                        LayoutInflater inflater1 = LayoutInflater.from(getApplicationContext());
                        final View view = inflater1.inflate(R.layout.peoplewidget, null);
                        TextView txtU = view.findViewById(R.id.txtPIusername);
                        txtU.setText(username);
                        TextView txtE = view.findViewById(R.id.txtPIemail);
                        txtE.setText(email);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclevwPISearch.addView(view);
                            }
                        });
                    }
                    slock=false;
                }
            }
            catch (JSONException s){

            }
        }
    }
}
