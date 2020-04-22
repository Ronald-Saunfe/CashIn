package com.example.cashin;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class purchaseInvestmentAdapter extends RecyclerView.Adapter<purchaseInvestmentAdapter.MyViewHolder> {
    private List<purchaseInvestmentModel> mDataset = new ArrayList<>() ;
    private List<MyViewHolder> attachedViews = new ArrayList<>() ;
    private peopleClickListener peopleclicklistener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView txtPIusername, txtPIemail;
        public CircleImageView cidp;
        peopleClickListener peopleclicklistener;

        public MyViewHolder(CardView v, peopleClickListener peopleclicklistener) {
            super(v);
            txtPIusername = v.findViewById(R.id.txtPIusername);
            txtPIemail = v.findViewById(R.id.txtPIemail);
            cidp = v.findViewById(R.id.cidp);

            this.peopleclicklistener = peopleclicklistener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            peopleclicklistener.onPersonClick(getAdapterPosition());
        }
    }

    public interface peopleClickListener {
        void onPersonClick(int position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public purchaseInvestmentAdapter(List<purchaseInvestmentModel> myDataset, peopleClickListener peopleclicklistener) {
        mDataset = myDataset;
        this.peopleclicklistener = peopleclicklistener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public purchaseInvestmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.peoplewidget, parent, false);

        MyViewHolder vh = new MyViewHolder(v, peopleclicklistener);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtPIusername.setText(mDataset.get(position).getUsername());
        holder.txtPIemail.setText(mDataset.get(position).getEmail());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}