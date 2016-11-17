package com.ebay.example.ggd;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final int GRID_SPAN_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Collection of items to be displayed in a grid
        final ItemAdapter itemAdapter = new ItemAdapter(this,
            Arrays.asList(
                new ItemViewModel(1, 3, "Item 1"),
                new ItemViewModel(1, 3, "Item 2"),
                new ItemViewModel(1, 3, "Item 3"),
                new ItemViewModel(1, 3, "Item 4"),
                new ItemViewModel(1, 3, "Item 5"),
                new ItemViewModel(1, 3, "Item 6"),
                new ItemViewModel(1, 3, "Item 7"),
                new ItemViewModel(1, 3, "Item 8"),
                new ItemViewModel(1, 3, "Item 9"),
                new ItemViewModel(1, 3, "Item 10"),
                new ItemViewModel(2, 3, "Item 11"),
                new ItemViewModel(2, 3, "Item 12"),
                new ItemViewModel(2, 3, "Item 13"),
                new ItemViewModel(2, 3, "Item 14"),
                new ItemViewModel(2, 3, "Item 15"),
                new ItemViewModel(2, 3, "Item 16"),
                new ItemViewModel(2, 3, "Item 17"),
                new ItemViewModel(2, 3, "Item 18"),
                new ItemViewModel(2, 3, "Item 19"),
                new ItemViewModel(2, 3, "Item 20")
            )
        );

        final Resources resources = getResources();
        final int backgroundElevation = resources.getDimensionPixelSize(R.dimen.background_elevation);

        final View overlayContainer1 = findViewById(R.id.overlay_container_1);
        ViewCompat.setElevation(overlayContainer1, backgroundElevation);
        final View overlayContainer2 = findViewById(R.id.overlay_container_2);
        ViewCompat.setElevation(overlayContainer2, backgroundElevation);

        final Drawable backgroundDrawable = ResourcesCompat.getDrawable(resources,
                R.drawable.group_background, getTheme());
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_main);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(itemAdapter.new SpanSizeLookup());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(itemAdapter);
        if (backgroundDrawable != null)
            recyclerView.addItemDecoration(new GroupedGridDecoration(backgroundDrawable));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.setElevation(0f);
    }

    /**
     * Simple ItemViewModel adapter that emits ItemViewHolders.
     */
    public static final class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>
    {
        private final List<ItemViewModel> viewModels;
        private final LayoutInflater layoutInflater;

        ItemAdapter(@NonNull Context context, @NonNull List<ItemViewModel> viewModels)
        {
            this.viewModels = viewModels;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            final View rootView = layoutInflater.inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position)
        {
            final ItemViewModel viewModel = viewModels.get(position);
            if (holder != null && viewModel != null)
                holder.bind(viewModel);
        }

        @Override
        public int getItemCount()
        {
            return viewModels.size();
        }

        /**
         * Pulls the span size from the adapter's collection
         */
        final class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup
        {
            @Override
            public int getSpanSize(int position)
            {
                return viewModels.get(position).spanSize;
            }
        }
    }
}
