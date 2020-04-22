package com.example.cashin;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MyViewHolder>  {
    private List<messageModel> mDataset = new ArrayList<>() ;
    private List<MyViewHolder> attachedViews = new ArrayList<>() ;

    public messageAdapter(List<messageModel> myDataset)
    {
        mDataset = myDataset;
    }

    @Override
    public messageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messageview, parent, false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtmessage.setText(mDataset.get(position).getMessage());
        holder.txtdate.setText(mDataset.get(position).getDate());
    }


    // Provide a reference to the views for each data item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtmessage, txtdate;
        public CircleImageView cidp;

        public MyViewHolder(CardView v) {
            super(v);
            txtmessage = v.findViewById(R.id.txtmsgm);
            txtdate = v.findViewById(R.id.txtmsgdate);
            cidp = v.findViewById(R.id.crclemsg);
        }

        // Provide a suitable constructor (depends on the kind of dataset)


        // Create new views (invoked by the layout manager)
    }

}