package com.ebay.example.ggd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * View holder for simple item
 */
public final class ItemViewHolder extends RecyclerView.ViewHolder
{
    private final TextView textView;
    private ItemViewModel viewModel;

    public ItemViewHolder(View itemView)
    {
        super(itemView);

        textView = (TextView)itemView.findViewById(R.id.textview);
    }

    public void bind(ItemViewModel viewModel)
    {
        this.viewModel = viewModel;

        textView.setText(viewModel.textValue);
    }

    public ItemViewModel getBoundViewModel()
    {
        return viewModel;
    }
}
