package com.example.cashin.Fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.cashin.GlobalVariable;
import com.example.cashin.InvestmentsModel;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class invetEarnFrag_2 extends Fragment implements OkHTTPhandler.onresponseLinstener {

    //backbutton on toolbar
    private ImageButton btnBackInvestEarn2;
    private TextView txtSteps;
    private Button btnGOIEf;
    private ProgressBar pbIE1;
    private int Rertycount=0;
    private BraintreeFragment mBraintreeFragment;
    HashMap<String,String> payload = new HashMap<String,String>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OkHTTPhandler.setOnResponseListener(this);
        //backbutton on toolbar
        btnBackInvestEarn2 = view.findViewById(R.id.btnBackInvestEarn2);

        //backbutton on toolbar
        pbIE1 = view.findViewById(R.id.pbIE1);

        //GO ON INVEST BUTTON
        btnGOIEf = view.findViewById(R.id.btnGOIEf);

        //textview for steps
        txtSteps = view.findViewById(R.id.txtSteps);

        //return to main menu invest and earn
        btnBackInvestEarn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                investEarnFrag_1 frag = new investEarnFrag_1();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayoutInvestEarn, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        String range ="Between USD "+(GlobalVariable.GlobInvestmentPrice/2)+ " - "
                +GlobalVariable.GlobInvestmentPrice *
                (GlobalVariable.GlobInvestmentCommission/100)*
                GlobalVariable.GlobInvestmentQuantity;
        String htmltxt = "<ul style=\"list-style-type:square;\">\n" +
                "  <li> To earn between "+ range +"<font color=\"grey\"> + extas</font>.</li><br>\n" +
                "  <li> Market your investment and ensure someone has invested in your investment through purchasing it.</li><br>\n" +
                "  <li> You earn 1/2 of the amount you invested once someone has purchased your investment.</li><br>\n" +
                "  <li> The investment experies once it is purchased atleast 20 times.</li><br>\n" +
                "  <li> The extras are determined by the efforts of the the ones who have purchased your investment. You will gain 1/4 of the amount you investment once someone purchase their investment</li><br>\n" +
                "  <li> <b>NOTE:</b>Your investment is refundable only if someone has not purchased it or you have purchased someones investment.</li>\n<br>" +
                "  ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtSteps.setText(Html.fromHtml(htmltxt, Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtSteps.setText(Html.fromHtml(htmltxt));
        }

        btnGOIEf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the client token from server
                GetToken();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invet_earn_frag_2, container, false);
    }

    private void GetToken(){
        payload.put("email_buyer_of_investment",GlobalVariable.GlobEmail);
        payload.put("email_seller_of_investment","Store");
        payload.put("Price", String.valueOf(GlobalVariable.GlobInvestmentPrice));
        payload.put("investmentPrice",String.valueOf(GlobalVariable.GlobInvestmentPrice));
        payload.put("bought_investment_acc_no", "null");
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/Login",
                0,
                pbIE1,
                null,
                this);
    }

    //purchase investment
    private void MakePurchase(String acc_no, String nonces){
        payload.put("investment_acc_no", acc_no);
        payload.put("email_seller_of_investment","Store");
        payload.put("payment_method_nonce", nonces.toString());
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/Login",
                1,
                pbIE1,
                null,
                this);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0){
            try{
                String code = response.getString("code");

                final String Cod = code;
                final String acc_no = response.getString("acc_no");
                final String mAuthorization = response.getString("response");
                if (Cod.equals("200")){
                    try {
                        mBraintreeFragment = BraintreeFragment.newInstance(getActivity(), mAuthorization);
                        // mBraintreeFragment is ready to use!
                        PayPalRequest PPrequest = new PayPalRequest(String.valueOf(GlobalVariable.GlobInvestmentPrice))
                                .currencyCode("USD")
                                .intent(PayPalRequest.INTENT_AUTHORIZE);
                        PayPal.requestOneTimePayment(mBraintreeFragment, PPrequest);
                        Snackbar.make(getView(),"Opening paypal from browser...",Snackbar.LENGTH_LONG).show();
                        //receive nonnce and send it to the server
                        mBraintreeFragment.addListener(new PaymentMethodNonceCreatedListener() {
                            @Override
                            public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
                                String nonce = paymentMethodNonce.getNonce();
                                System.out.println(nonce+"=======>>>>Start the purchase===="+acc_no);
                                MakePurchase(acc_no, nonce);
                                Snackbar.make(getView(),"Almost there... dont exit",Snackbar.LENGTH_LONG).show();
                            }
                        });
                        //listen to the cancel of the flow
                        mBraintreeFragment.addListener(new BraintreeCancelListener() {
                            @Override
                            public void onCancel(int requestCode) {
                                Snackbar.make(getView(),"",Snackbar.LENGTH_LONG).show();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbIE1.setVisibility(View.GONE);
                                        btnGOIEf.setText("GO ON INVEST");
                                        btnGOIEf.setEnabled(true);
                                    }
                                });
                            }
                        });
                        //listen to errors like network e.g
                        mBraintreeFragment.addListener(new BraintreeErrorListener() {
                            @Override
                            public void onError(Exception error) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbIE1.setVisibility(View.GONE);
                                        btnGOIEf.setText("GO ON INVEST");
                                        btnGOIEf.setEnabled(true);
                                    }
                                });

                                if (error instanceof ErrorWithResponse) {
                                    ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
                                    BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
                                    if (cardErrors != null) {
                                        // There is an issue with the credit card.
                                        BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                                        if (expirationMonthError != null) {
                                            // There is an issue with the expiration month.
                                            Snackbar.make(getView(),expirationMonthError.getMessage(),Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else{
                                    Snackbar.make(getView(),"Error occurred please retry",Snackbar.LENGTH_LONG).show();
                                    System.out.println("error============"+error.getMessage());
                                }
                            }
                        });

                    } catch (InvalidArgumentException e) {
                        // There was an issue with your authorization string.
                    }
                }
            }
            catch (JSONException d){

            }
        }
        else if (id==1){
            try{
                String code = response.getString("code");
                String resp = response.getString("response");
                final String Cod = code;
                final String mAuthorization = response.getString("response");
                if (Cod.equals("200")){
                    Snackbar.make(getView(), "Successfully purchased", Snackbar.LENGTH_LONG).show();
                }
                else if (Cod.equals("100")){
                    Snackbar.make(getView(), resp, Snackbar.LENGTH_LONG).show();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbIE1.setVisibility(View.GONE);
                            btnGOIEf.setText("GO ON INVEST");
                            btnGOIEf.setEnabled(true);
                        }
                    });
                }
            }
            catch (JSONException R){

            }

        }
    }
}
