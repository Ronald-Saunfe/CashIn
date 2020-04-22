package com.example.cashin.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.cashin.GlobalVariable;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.example.cashin.myrefereeAdapter;
import com.example.cashin.myrefereeModel;
import com.example.cashin.purchaseInvestmentAdapter;
import com.example.cashin.purchaseInvestmentModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myReferee extends AppCompatActivity implements myrefereeAdapter.peopleClickListener{

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<myrefereeModel> myDataset = new ArrayList<>();
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

    private RecyclerView recyclevwmess;
    private LinearLayout recyclevwmessSearch;

    private String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_referee);

        pbPI = findViewById(R.id.pbmss);

        EtxtseachPI = findViewById(R.id.Etxtseachmyreferee);

        recyclevwmessSearch = findViewById(R.id.recyclevwmessSearch);
        recyclevwmess= findViewById(R.id.recyclevwmess);
        //set layout fade animation
        LayoutTransition itemLayoutTransition1 = new LayoutTransition();
        itemLayoutTransition1.setInterpolator(LayoutTransition.DISAPPEARING, new AccelerateInterpolator());
        itemLayoutTransition1.setInterpolator(LayoutTransition.APPEARING, new AccelerateInterpolator());
        //itemLayoutTransition1.setDuration(500); //3/4*time of transition

        ViewGroup av1 = findViewById(R.id.ccPI1);
        av1.setLayoutTransition(itemLayoutTransition1);

        recyclevwmess.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclevwmess.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new myrefereeAdapter(myDataset, this);
        SlideInUpAnimator siua = new SlideInUpAnimator();
        siua.setInterpolator(new AnticipateOvershootInterpolator());
        recyclevwmess.setItemAnimator(siua);
        recyclevwmess.setAdapter(mAdapter);

        recyclevwmess.addOnScrollListener(recyclerViewOnScrollListener);

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
                        recyclevwmessSearch.setVisibility(View.GONE);
                        recyclevwmess.setVisibility(View.VISIBLE);
                    }
                    else{
                        recyclevwmessSearch.setVisibility(View.VISIBLE);
                        recyclevwmess.setVisibility(View.GONE);
                        searchPeople();
                    }
                }
            }
        });
        fetchPeople();
    }

    @Override
    public void onPersonClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to set "+myDataset.get(position).getUsername()+" as your referee").setTitle("Confirm");

        final int pos = position;
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        GlobalVariable.GlobmyRefereeEmail = myDataset.get(pos).getEmail();
                        setReferee();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setReferee(){
        final JSONObject jsonObject=new JSONObject();
        final JSONObject jsonObject1=new JSONObject();
        boolean chkerr=false;
        try {
            System.out.println(">>>>>>=====================last email init==========="+last_doc_email);
            jsonObject.put("email", GlobalVariable.GlobEmail);//GlobalVariable.GlobEmail
            jsonObject1.put("email_referee",GlobalVariable.GlobmyRefereeEmail);
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
                    .url("https://us-central1-cashin-270220.cloudfunctions.net/setReferee")
                    .post(body)
                    .build();

            //callback to handle response and error of the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    System.out.println("Failure on response====================================="+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbPI.setVisibility(View.GONE);
                        }
                    });
                    String errMessage = ResponseError.GetResponse(e.getMessage());
                    if (Rertycount<=3){
                        if (errMessage.equals("Connection failed")){
                            setReferee();
                        }
                        else if(errMessage.equals("Slow internet connection")){
                            setReferee();
                        }
                        else if(errMessage.equals("timeout")){
                            setReferee();
                        }
                        else if(errMessage.equals("client timeout")){
                            setReferee();
                        }
                        else if(errMessage.equals("Try Again")){
                            setReferee();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.myinvPurchased),errMessage,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            setReferee();
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
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myinvPurchased).findViewById(R.id.rootProfl),errMessage,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        setReferee();
                                    }
                                }).setActionTextColor(Color.parseColor("#003c8f"));

                        snackbar.show();
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbPI.setVisibility(View.GONE);
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       Toast.makeText(getApplicationContext(), GlobalVariable.GlobmyRefereeEmail+" set as Referee",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                            }
                        }
                    }
                    catch (JSONException err){
                        System.out.println("building json after repsonse====================================="+err.getMessage());
                        final JSONException err1 = err;
                        //ude the main ui thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),err1.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }
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
            int firstVisibleItemPosition =((LinearLayoutManager)recyclevwmess.getLayoutManager()).findFirstVisibleItemPosition();

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
    private void searchPeople(){
        slock = true;
        //make an asynchronous request
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbPI.setVisibility(View.VISIBLE);
            }
        });
        final JSONObject jsonObject=new JSONObject();
        final JSONObject jsonObject1=new JSONObject();
        boolean chkerr=false;
        try {
            System.out.println("edit text====================================="+EtxtseachPI.getText());
            jsonObject.put("SearchEmail", EtxtseachPI.getText());
            jsonObject1.put("message",jsonObject);
            System.out.println("----------------------"+jsonObject1);
            chkerr = false;
        } catch (JSONException e) {
            System.out.println("building json error to response=====================================GT 1"+e.getMessage());
            chkerr = true;
            e.printStackTrace();
            slock=false;
        }
        //ensure the json is build correctly
        if (chkerr ==false) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject1));
            //build the url
            final Request request = new Request.Builder()
                    .url("https://us-central1-cashin-270220.cloudfunctions.net/searchPeople")
                    .post(body)
                    .build();

            //callback to handle response and error of the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    System.out.println("Failure on response====================================="+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbPI.setVisibility(View.GONE);
                        }
                    });
                    slock=false;
                    String errMessage = ResponseError.GetResponse(e.getMessage());
                    if (Rertycount<=3){
                        if (errMessage.equals("Connection failed")){
                            searchPeople();
                        }
                        else if(errMessage.equals("Slow internet connection")){
                            searchPeople();
                        }
                        else if(errMessage.equals("timeout")){
                            searchPeople();
                        }
                        else if(errMessage.equals("client timeout")){
                            searchPeople();
                        }
                        else if(errMessage.equals("Try Again")){
                            searchPeople();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.myinvPurchased),errMessage,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            searchPeople();
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
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myinvPurchased).findViewById(R.id.rootProfl),errMessage,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        searchPeople();
                                    }
                                }).setActionTextColor(Color.parseColor("#003c8f"));

                        snackbar.show();
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbPI.setVisibility(View.GONE);
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
                            availableInv = availableInv.replace("[", "");
                            availableInv = availableInv.replace("]", "");
                            System.out.println("======<<>>>"+availableInv);
                            final List<String> strng = new ArrayList<String>(Arrays.asList(availableInv.split("\\},")));
                            recyclevwmessSearch.removeAllViews();
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
                                        recyclevwmessSearch.addView(view);
                                    }
                                });
                            }
                            slock=false;
                        }
                    }
                    catch (JSONException err){
                        System.out.println("building json after repsonse====================================="+err.getMessage());
                        final JSONException err1 = err;
                        //ude the main ui thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),err1.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                        slock=false;
                    }
                }
            });
        }
    }
    //load the people in a pagination fatshion
    private void fetchPeople(){
        //make an asynchronous request
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbPI.setVisibility(View.VISIBLE);
            }
        });
        final JSONObject jsonObject=new JSONObject();
        final JSONObject jsonObject1=new JSONObject();
        boolean chkerr=false;
        try {
            System.out.println(">>>>>>=====================last email init==========="+last_doc_email);
            jsonObject.put("last_doc_email",last_doc_email);//GlobalVariable.GlobEmail
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
                    .url("https://us-central1-cashin-270220.cloudfunctions.net/fetchMembersPaginated")
                    .post(body)
                    .build();

            //callback to handle response and error of the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    System.out.println("Failure on response====================================="+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbPI.setVisibility(View.GONE);
                        }
                    });
                    String errMessage = ResponseError.GetResponse(e.getMessage());
                    if (Rertycount<=3){
                        if (errMessage.equals("Connection failed")){
                            fetchPeople();
                        }
                        else if(errMessage.equals("Slow internet connection")){
                            fetchPeople();
                        }
                        else if(errMessage.equals("timeout")){
                            fetchPeople();
                        }
                        else if(errMessage.equals("client timeout")){
                            fetchPeople();
                        }
                        else if(errMessage.equals("Try Again")){
                            fetchPeople();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.myinvPurchased),errMessage,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            fetchPeople();
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
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myinvPurchased).findViewById(R.id.rootProfl),errMessage,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        fetchPeople();
                                    }
                                }).setActionTextColor(Color.parseColor("#003c8f"));

                        snackbar.show();
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbPI.setVisibility(View.GONE);
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
                                    myDataset.add(new myrefereeModel(username  ,email));
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
                    catch (JSONException err){
                        System.out.println("building json after repsonse====================================="+err.getMessage());
                        final JSONException err1 = err;
                        //ude the main ui thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),err1.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
