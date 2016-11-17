package com.ebay.example.ggd;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Item Decoration that identifies groups of cells and draws a background behind the group.
 * Additionally uses dummy views to provide ViewOutlineProviders for the backgrounds since
 * each View can only have one ViewOutlineProvider.
 *
 * Note: This is a simplified example that needs
 * {@link android.support.v7.widget.RecyclerView.LayoutManager}
 * to inherit from {@link LinearLayoutManager} due to the methods
 * {@link LinearLayoutManager#findFirstVisibleItemPosition()} and
 * {@link LinearLayoutManager#findLastVisibleItemPosition()}.
 */
public final class GroupedGridDecoration extends RecyclerView.ItemDecoration
{
    private final Drawable backgroundDrawable;
    private Rect drawingRect = new Rect();

    public GroupedGridDecoration(@NonNull Drawable backgroundDrawable)
    {
        this.backgroundDrawable = backgroundDrawable;
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        final LinearLayoutManager layoutManager = (LinearLayoutManager)parent.getLayoutManager();
        final int visibleLowerBound = layoutManager.findFirstVisibleItemPosition();
        final int visibleUpperBound = layoutManager.findLastVisibleItemPosition();

        // Variables to track the start and end of the group
        int firstPositionInGroup = visibleLowerBound;
        int lastPositionInGroup;
        int currentGroupId = -1;

        // Iterate over visible items
        for (int position = visibleLowerBound; position <= visibleUpperBound; position++)
        {
            final ItemViewHolder viewHolder = (ItemViewHolder)parent.findViewHolderForAdapterPosition(position);
            if (viewHolder != null)
            {
                final ItemViewModel viewModel = viewHolder.getBoundViewModel();
                final int groupId = viewModel.groupId;

                // Close group
                if (currentGroupId != groupId)
                {
                    lastPositionInGroup = position - 1;
                    drawBackground(c, parent, layoutManager, firstPositionInGroup, lastPositionInGroup, currentGroupId);

                    // Assign first position in new group
                    firstPositionInGroup = position;
                }

                if (position == visibleUpperBound)
                {
                    // The group starts and ends on this item, short circuit the round trip
                    if (currentGroupId != groupId)
                    {
                        firstPositionInGroup = position;
                        currentGroupId = groupId;
                    }
                    lastPositionInGroup = position;
                    drawBackground(c, parent, layoutManager, firstPositionInGroup, lastPositionInGroup, currentGroupId);
                }
                else if (currentGroupId != groupId)
                {
                    // Assign new groupInfo
                    currentGroupId = groupId;
                }
            }
        }
    }

    private void drawBackground(Canvas c, RecyclerView parent, LinearLayoutManager layoutManager,
                                int firstPositionInGroup, int lastPositionInGroup, int groupId)
    {
        // Create drawing rect and draw
        final View firstView = layoutManager.findViewByPosition(firstPositionInGroup);
        final View lastView = layoutManager.findViewByPosition(lastPositionInGroup);
        if (firstView != null && lastView != null)
        {
            drawingRect.set(
                    parent.getLeft() + 30,
                    firstView.getTop() - 10,
                    parent.getRight() - 30,
                    lastView.getBottom() + 10
            );
            backgroundDrawable.setBounds(drawingRect);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                View outlineView = null;
                View grandParent = parent.getRootView();

                if (grandParent != null)
                    outlineView = grandParent.findViewWithTag(Integer.toString(groupId));

                if (outlineView != null)
                    setOutline(backgroundDrawable, outlineView);
            }

            backgroundDrawable.draw(c);
        }
    }

    /**
     * Check if our dummy view has the appropriate outline class, if not replace it, then set
     * the new outline value and invalidateOutline.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setOutline(@NonNull final Drawable drawable, @NonNull final View view)
    {
        ViewOutlineProvider provider = view.getOutlineProvider();
        if (!(provider instanceof RecyclerViewOutlineProvider))
        {
            provider = new RecyclerViewOutlineProvider();
            view.setOutlineProvider(provider);
        }

        final Outline outline = new Outline();
        drawable.getOutline(outline);
        ((RecyclerViewOutlineProvider)provider).setOutline(outline);
        view.invalidateOutline();
    }

    /**
     * Outline provider that can be set by the decorator.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static final class RecyclerViewOutlineProvider extends ViewOutlineProvider
    {
        private Outline outline;

        @Override
        public void getOutline(View view, Outline outline)
        {
            if (this.outline != null)
                outline.set(this.outline);
        }

        void setOutline(Outline outline)
        {
            this.outline = outline;
        }
    }

    /**
     * Simple implementation of getItemOffsets that adds some hard coded gutter space around the
     * cells in this example code.
     */
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        final int adapterItemPosition = parent.getChildAdapterPosition(view);
        final int adapterItemCount = state.getItemCount();

        outRect.set(0,0,0,0);

        /* In a real implementation we would use the group identifier and a set of dimensions to
           define the inside padding, outside padding and spacing between groups. This sample is
           focused on the onDraw method so I'm cheating. */

        if (adapterItemPosition < 2 || adapterItemPosition == 10 || adapterItemPosition == 11
                || adapterItemPosition == 20 || adapterItemPosition == 21)
            outRect.top = 60;

        if (adapterItemPosition % 2 == 0)
            outRect.left = 40;
        else
            outRect.right = 40;

        if (adapterItemPosition > adapterItemCount - 3)
            outRect.bottom = 40;
    }
}