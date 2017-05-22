package com.ourbuaa.buaahelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by alan_yang on 2017/5/22.
 */

public class SuggestionHolder extends RecyclerView.ViewHolder {
    public TextView getTitle() {
        return title;
    }

    public TextView getDate() {
        return date;
    }

    private TextView title,date;
    public SuggestionHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.txt_label_item);
        title = (TextView) itemView.findViewById(R.id.txt_date_time);
    }
}
