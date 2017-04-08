package com.example.lemniscate.lockapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.Items;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class AppViewProvider extends ItemViewProvider<Appbean, AppViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected AppViewProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.app, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull Appbean appbean) {
        holder.iv.setImageDrawable(appbean.getSrc());
        holder.tv.setText(appbean.getName());
        holder.cb.setChecked(appbean.isCheck);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        CheckBox cb;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.cb = (CheckBox) itemView.findViewById(R.id.cb);
            this.iv = (ImageView) itemView.findViewById(R.id.iv);
            this.tv = (TextView) itemView.findViewById(R.id.tv);
//            this.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (cb.isChecked()) {
//                        spUtils.setAppName(itemView.getContext(),tv.getText().toString());
//                    }else {
//                        spUtils.removeAppName(itemView.getContext(),tv.getText().toString());
//                    }
//                }
//            });
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cb.isChecked()) {
                        spUtils.removeAppName(itemView.getContext(), tv.getText().toString());
                    } else {
                        spUtils.setAppName(itemView.getContext(), tv.getText().toString());
                    }
                    EventBus.getDefault().post(new AppbeanMessageEvent(getPosition(),!cb.isChecked()));
                    cb.setChecked(!cb.isChecked());
                }
            });
        }
    }
}
