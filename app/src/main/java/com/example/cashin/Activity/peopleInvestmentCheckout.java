package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.cashin.Fragment.SignUpFragment.JSON;

public class peopleInvestmentCheckout extends AppCompatActivity implements OkHTTPhandler.onresponseLinstener{

    private TextView txtStepsPPI;
    private MaterialButton btnGOIPPI;
    private  int Rertycount;
    private ProgressBar pbppI;
    private BraintreeFragment mBraintreeFragment;
    private AppCompatActivity activity;
    HashMap<String,String> payload = new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_investment_checkout);
        OkHTTPhandler.setOnResponseListener(this);
        txtStepsPPI = findViewById(R.id.txtStepsPPI);
        pbppI = findViewById(R.id.pbppI);
        btnGOIPPI = findViewById(R.id.btnGOIPPI);

        activity=this;

        String range ="Between USD "+(GlobalVariable.GlobSelectedInvestmentPrice/2)+ " - "
                +GlobalVariable.GlobSelectedInvestmentPrice *
                (GlobalVariable.GlobSelectedInvestmentCommission/100)*
                GlobalVariable.GlobSelectedInvestmentQuantity;
        String htmltxt = "<ul style=\"list-style-type:square;\">\n" +
                "  <li> To earn between "+ range +"<font color=\"grey\"> + extas</font>.</li><br>\n" +
                "  <li> Market your investment and ensure someone has invested in your investment through purchasing it.</li><br>\n" +
                "  <li> You earn 1/2 of the amount you invested once someone has purchased your investment.</li><br>\n" +
                "  <li> The investment experies once it is purchased atleast 20 times.</li><br>\n" +
                "  <li> The extras are determined by the efforts of the the ones who have purchased your investment. You will gain 1/4 of the amount you investment once someone purchase their investment</li><br>\n" +
                "  <li> <b>NOTE:</b>Your investment is refundable only if someone has not purchased it or you have purchased someones investment.</li>\n<br>" +
                "  ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtStepsPPI.setText(Html.fromHtml(htmltxt, Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtStepsPPI.setText(Html.fromHtml(htmltxt));
        }

        btnGOIPPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetToken();
            }
        });
    }

    private void GetToken(){
        payload.put("email_buyer_of_investment",GlobalVariable.GlobEmail);//GlobalVariable.GlobEmail
        payload.put("email_seller_of_investment",GlobalVariable.selectedPIEmail);
        payload.put("Price",String.valueOf(GlobalVariable.GlobSelectedInvestmentPrice));
        payload.put("bought_investment_acc_no", GlobalVariable.GlobSelectedBoughtAccessionNo);
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/generateBrainTreeToken",
                0,
                pbppI,
                this,
                null);
    }

    //purchase investment
    private void MakePurchase(String acc_no, String nonces){
        payload.put("email_buyer_of_investment",GlobalVariable.GlobEmail);//GlobalVariable.GlobEmail
        payload.put("investment_acc_no", acc_no);
        payload.put("email_seller_of_investment", GlobalVariable.selectedPIEmail);
        payload.put("payment_method_nonce", nonces.toString());
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/purchaseInvestment",
                1,
                pbppI,
                this,
                null);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if(id==0){
            try{
                String code = response.getString("code");

                final String Cod = code;
                final String acc_no = response.getString("acc_no");
                final String mAuthorization = response.getString("response");
                if (Cod.equals("200")){
                    try {
                        mBraintreeFragment = BraintreeFragment.newInstance(activity, mAuthorization);
                        // mBraintreeFragment is ready to use!
                        PayPalRequest PPrequest = new PayPalRequest(String.valueOf(GlobalVariable.GlobSelectedInvestmentPrice))
                                .currencyCode("USD")
                                .intent(PayPalRequest.INTENT_AUTHORIZE);
                        PayPal.requestOneTimePayment(mBraintreeFragment, PPrequest);
                        Snackbar.make(findViewById(R.id.constPPIC),"Opening paypal from browser...",Snackbar.LENGTH_LONG).show();
                        //receive nonnce and send it to the server
                        mBraintreeFragment.addListener(new PaymentMethodNonceCreatedListener() {
                            @Override
                            public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
                                String nonce = paymentMethodNonce.getNonce();
                                System.out.println(nonce+"=======>>>>Start the purchase===="+acc_no);
                                MakePurchase(acc_no, nonce);
                                Snackbar.make(findViewById(R.id.constPPIC),"Almost there... dont exit",Snackbar.LENGTH_LONG).show();
                            }
                        });
                        //listen to the cancel of the flow
                        mBraintreeFragment.addListener(new BraintreeCancelListener() {
                            @Override
                            public void onCancel(int requestCode) {
                                Snackbar.make(findViewById(R.id.constPPIC),"",Snackbar.LENGTH_LONG).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbppI.setVisibility(View.GONE);
                                        btnGOIPPI.setText("GO ON INVEST");
                                        btnGOIPPI.setEnabled(true);
                                    }
                                });
                            }
                        });
                        //listen to errors like network e.g
                        mBraintreeFragment.addListener(new BraintreeErrorListener() {
                            @Override
                            public void onError(Exception error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbppI.setVisibility(View.GONE);
                                        btnGOIPPI.setText("GO ON INVEST");
                                        btnGOIPPI.setEnabled(true);
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
                                            Snackbar.make(findViewById(R.id.constPPIC),expirationMonthError.getMessage(),Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else{
                                    Snackbar.make(findViewById(R.id.constPPIC),"Error occurred please retry",Snackbar.LENGTH_LONG).show();
                                    System.out.println("error============"+error.getMessage());
                                }
                                System.out.println("error=====aaah!======="+error.getMessage());
                            }
                        });

                    }
                    catch (InvalidArgumentException e) {
                        // There was an issue with your authorization string.
                    }
                }

            }
            catch (JSONException e){ }
        }
        else if(id==1){
            try{
                String code = response.getString("code");
                String resp = response.getString("response");
                final String Cod = code;
                final String mAuthorization = response.getString("response");
                if (Cod.equals("200")){
                    Snackbar.make(findViewById(R.id.constPPIC), "Successfully purchased", Snackbar.LENGTH_LONG).show();
                }
                else if (Cod.equals("100")){
                    Snackbar.make(findViewById(R.id.constPPIC), resp, Snackbar.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbppI.setVisibility(View.GONE);
                            btnGOIPPI.setText("GO ON INVEST");
                            btnGOIPPI.setEnabled(true);
                        }
                    });
                }
            }
            catch (JSONException D){

            }
        }
        }

}

