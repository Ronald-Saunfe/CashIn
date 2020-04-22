package com.example.cashin;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InvestmentAdapterIE extends RecyclerView.Adapter<InvestmentAdapterIE.MyViewHolder> {
    private List<InvestmentsModel> mDataset = new ArrayList<>() ;
    private List<MyViewHolder> attachedViews = new ArrayList<>() ;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button btn;
        public CheckBox chkIE;

        public MyViewHolder(CardView v) {
            super(v);
            btn = v.findViewById(R.id.rootbtnIE);
            chkIE = v.findViewById(R.id.chkIE);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InvestmentAdapterIE(List<InvestmentsModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InvestmentAdapterIE.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.investmentviews, parent, false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        //ensure no reduduncy
        boolean redudant=false;
        boolean fredudant = false;
        for (MyViewHolder vw :attachedViews){
            if (vw.getLayoutPosition()==holder.getLayoutPosition()){
                redudant = true;
            }
            if (vw.getLayoutPosition()==0){
                fredudant = true;
            }

        }

        //prevent the focus of the first view
        if (fredudant==false){
            if (holder.getLayoutPosition()==0){
                holder.chkIE.setVisibility(View.VISIBLE);
                holder.chkIE.toggle();
                holder.btn.setTextColor(Color.parseColor("#003c8f"));
                GlobalVariable.GlobInvestmentPrice =mDataset.get(holder.getLayoutPosition()).getPrice();
            }
        }

        if (redudant==false){
            attachedViews.add(holder);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.btn.setText("Between USD "+String.valueOf(mDataset.get(position).getPrice()/2)+ " - "
                +String.valueOf( mDataset.get(position).getPrice()*
                (mDataset.get(position).getCommission()/100)*
                        mDataset.get(position).getQuantity()
        )
        );

        final MyViewHolder holders = holder;
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear the previous states
                for (MyViewHolder vw :attachedViews){
                    vw.btn.setTextColor(Color.parseColor("#7e7e7e"));
                    if (holders.chkIE.isChecked() ) {
                        holders.chkIE.toggle();
                    }
                    vw.chkIE.setVisibility(View.GONE);
                }

                GlobalVariable.GlobInvestmentPrice =mDataset.get(holders.getLayoutPosition()).getPrice();
                GlobalVariable.GlobInvestmentCommission =mDataset.get(holders.getLayoutPosition()).getCommission();
                GlobalVariable.GlobInvestmentQuantity =mDataset.get(holders.getLayoutPosition()).getQuantity();
                holders.chkIE.setVisibility(View.VISIBLE);
                holders.chkIE.toggle();
                holders.btn.setTextColor(Color.parseColor("#003c8f"));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}