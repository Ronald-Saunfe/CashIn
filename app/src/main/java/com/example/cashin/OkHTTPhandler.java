package com.example.cashin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHTTPhandler {
    final static private JSONObject jsonObject=new JSONObject();
    final static private JSONObject jsonObject1=new JSONObject();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String postUrl;
    private static int Rertycount;
    private static AppCompatActivity activty=null;
    private static Fragment fragment;
    private static ProgressBar progressbar;
    private static View view;
    private static int id;
    static OkHttpClient client = new OkHttpClient();

    private static onresponseLinstener listener;

    public static void makepayload(HashMap<String,String> payload, String url,int idd, ProgressBar pb, AppCompatActivity act, Fragment frag){
        if (payload!=null){
            try{
                for(Map.Entry m:payload.entrySet()) {
                    jsonObject.put(m.getKey().toString(), m.getValue().toString());
                }
            }
            catch (JSONException e){
            }
        }
        else {
            try{
                jsonObject.put("" ,"");
            }
            catch (JSONException e){
            }
        }


        postUrl = url;
        Rertycount=0;

        if (act!=null){
            activty = act;
        }
        else{
            fragment = frag;
        }

        id =idd;

        progressbar = pb;
        System.out.println("==============payload built===================");
        Request();
    }

    private static void Request(){
        if (activty!=null){
            activty.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.VISIBLE);
                }
            });
        }
        else{
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.VISIBLE);
                }
            });
        }

        //create the request string
        System.out.println("==============request made===================");
        boolean chkerr=false;
        try {
            jsonObject1.put("message",jsonObject);
            System.out.println("----------------------"+jsonObject1);
            chkerr = false;
        }
        catch (JSONException e)
        {
            System.out.println("building json error to response====================================="+e.getMessage());
            chkerr = true;
            e.printStackTrace();
        }
        //ensure the json is build correctly
        if (chkerr ==false){
            System.out.println("json built successfully");
            //make an asynchronous request
            RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject1));
            //build the url
            final Request request = new Request.Builder()
                    .url(postUrl)
                    .post(body)
                    .build();

            //callback to handle response and error of the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    System.out.println("Failure on response====================================="+e.getMessage());
                    String errMessage = ResponseError.GetResponse(e.getMessage());
                    Rertycount ++;
                    if (activty!=null){
                        activty.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setVisibility(View.GONE);
                            }
                        });
                    }
                    if (Rertycount<=4){
                        if (errMessage.equals("Connection failed")){
                            Request();
                        }
                        else if(errMessage.equals("Slow internet connection")){
                            Request();
                        }
                        else if(errMessage.equals("timeout")){
                            Request();
                        }
                        else if(errMessage.equals("client timeout")){
                            Request();
                        }
                        else if(errMessage.equals("Try Again")){
                            Request();
                        }
                        else{
                            if (activty!=null){
                                view = activty.findViewById(android.R.id.content).getRootView();
                            }
                            else{
                                view = fragment.getView().getRootView();
                            }
                            Snackbar snackbar = Snackbar.make(view,errMessage,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Request();
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
                        if (activty!=null){
                            view = activty.findViewById(android.R.id.content).getRootView();
                        }
                        else{
                            view = fragment.getView().getRootView();
                        }
                        Snackbar snackbar = Snackbar.make(view,errMessage,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Request();
                                    }
                                }).setActionTextColor(Color.parseColor("#003c8f"));

                        snackbar.show();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (activty!=null){
                        activty.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setVisibility(View.GONE);
                            }
                        });
                    }
                    final String myResponse = response.body().string();

                    try {
                        //convertion of response to json to fetch value
                        JSONObject jsonObj = new JSONObject(myResponse);
                        System.out.println("==============response==================="+jsonObj);
                        listener.onResponse(jsonObj, id);

                    }
                    catch (JSONException err){
                        System.out.println("building json after repsonse====================================="+err.getMessage());
                        final JSONException err1 = err;
                        //ude the main ui thread
                        if (activty!=null){
                            activty.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activty.getApplicationContext(), err1.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(fragment.getContext(), err1.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }

                }
            });
        }
    }


    public interface onresponseLinstener{
        void onResponse(JSONObject response,int id);
    }

    public static void setOnResponseListener(onresponseLinstener listen){
        OkHTTPhandler.listener = listen;
    }
}
