package com.ourbuaa.buaahelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

/**
 * Created by alan_yang on 2017/5/22.
 */

public class CustomSuggestionsAdapter extends SuggestionsAdapter<itemForSeachBar,SuggestionHolder> {
    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public void onBindSuggestionHolder(itemForSeachBar suggestion, SuggestionHolder holder, int position) {
             holder.getTitle().setText(suggestion.getTitle());
             holder.getDate().setText(suggestion.getTime().toString());
    }

    @Override
    public int getSingleViewHeight() {
        return 60;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.common_item_layout, parent, false);
        return new SuggestionHolder(view);
    }
}
