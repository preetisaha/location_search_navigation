package com.googlelocationapi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googlelocationapi.holder.ATMNamesHolder;
import com.googlelocationapi.R;

import java.util.List;

public class NamesAdapter extends RecyclerView.Adapter<ATMNamesHolder>{


    private List<String> atmNameList;

    public NamesAdapter(List<String> atmNameList) {
        this.atmNameList = atmNameList;
    }
    @Override
    public ATMNamesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.atm_names_adapter, parent, false);
        return new ATMNamesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ATMNamesHolder holder, int position) {
        String atm = atmNameList.get(position);
        holder.cuboidButton.setText(atm);
    }

    @Override
    public int getItemCount() {
        return atmNameList.size();
    }
}
