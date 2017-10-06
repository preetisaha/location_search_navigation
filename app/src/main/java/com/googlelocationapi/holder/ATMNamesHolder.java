package com.googlelocationapi.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.googlelocationapi.R;

public class ATMNamesHolder extends RecyclerView.ViewHolder {

    public CuboidButton cuboidButton;

    public ATMNamesHolder(View view) {
        super(view);
        cuboidButton = (CuboidButton)view.findViewById(R.id.atmNames);
    }

    public TextView getName() {
        return cuboidButton;
    }

    public void setName(TextView name) {
        this.cuboidButton = cuboidButton;
    }
}
