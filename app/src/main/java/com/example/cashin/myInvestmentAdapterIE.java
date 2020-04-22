package com.example.cashin;

import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class myInvestmentAdapterIE extends RecyclerView.Adapter<myInvestmentAdapterIE.MyViewHolder> {
    private List<myInvestmentsModel> mDataset = new ArrayList<>() ;
    private List<MyViewHolder> attachedViews = new ArrayList<>() ;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtprice, txtremaining, txtdatebought,myInvProftmade;
        public Button btnrefund,btnShare;

        public MyViewHolder(CardView v) {
            super(v);
            txtprice = v.findViewById(R.id.myInvPrice);
            txtremaining = v.findViewById(R.id.myInvQuantity);
            txtdatebought = v.findViewById(R.id.myinvDateBought);
            myInvProftmade = v.findViewById(R.id.myInvProftmade);
            btnrefund = v.findViewById(R.id.btnRefund);
            btnShare = v.findViewById(R.id.btnShare);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public myInvestmentAdapterIE(List<myInvestmentsModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public myInvestmentAdapterIE.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myinvestment, parent, false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String htmltxt = "<font size=\"0.2\">USD</font> "+mDataset.get(position).getPrice();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtprice.setText(Html.fromHtml(htmltxt, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.txtprice.setText(Html.fromHtml(htmltxt));
        }

        holder.txtdatebought.setText("Bought on "+mDataset.get(position).getDatebought());
        holder.myInvProftmade.setText("Profit made "+mDataset.get(position).getProfitmade());
        holder.txtremaining.setText("Remaining "+mDataset.get(position).getQuantity());
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello ,\n download cash in from playstore and earn money from investment. Search for "+GlobalVariable.GlobEmail+" in purchase investment");
                sendIntent.setType("text/plain");
                v.getContext().startActivity(sendIntent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}