package com.example.emptytest.testglovo.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emptytest.testglovo.R;
import com.example.emptytest.testglovo.data.models.CitiesList;
import com.example.emptytest.testglovo.data.models.City;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.CountriesViewHolder> {

    private CitiesList mData;
    private Context mContext;
    private static ClickListener mListener;


    public DataAdapter(Context context, ClickListener listener)
    {
        this.mContext = context;
        mListener = listener;
    }
    public void setData(CitiesList mData) {
        this.mData = mData;
    }
    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        if(viewType==0)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.country_item, viewGroup,false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sity_item, viewGroup,false);

        return new CountriesViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, int i) {
        holder.tvName.setText(mData.getListOfElements().get(i).getName());
        holder.mCity = mData.getListOfElements().get(i).getCity();
    }

    @Override
    public int getItemViewType(int position){
        return mData.getListOfElements().get(position).getCity()!=null?1:0;
    }
    @Override
    public int getItemCount() {

        if(mData==null)
            return 0;

        return mData.getListOfElements().size();
    }

    public static class CountriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvName=null;
        private City mCity = null;
        public CountriesViewHolder(Context context, View itemView)
        {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            View item = itemView.findViewById(R.id.cityItem);
            if(item!=null){
                item.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(mCity);
        }
    }


    public interface ClickListener{
        public void onItemClick(City city);
    }
}
