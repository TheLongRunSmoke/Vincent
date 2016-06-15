package com.example.vincent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by thelongrunsmoke.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private String[] mDataset;
    private String[] mActivityNames;

    private static final String LOG_TAG = "RecyclerViewAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public RecyclerViewAdapter(Context context, String[] dataset, String[] activityNames) {
        mContext = context;
        mDataset = dataset;
        mActivityNames = activityNames;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder((TextView) v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(mContext, Class.forName(mContext.getPackageName() + mActivityNames[holder.getAdapterPosition()]));
                    mContext.startActivity(intent);
                } catch (ClassNotFoundException e1) {
                    Log.d(LOG_TAG, "onClick: Class not found.");
                } catch (ArrayIndexOutOfBoundsException e2) {
                    Log.d(LOG_TAG, "onClick: Array index out of bounds.");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
