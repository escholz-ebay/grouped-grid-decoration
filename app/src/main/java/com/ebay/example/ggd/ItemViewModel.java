package com.ebay.example.ggd;

/**
 * View model for simple item with grouping and span information
 */
public final class ItemViewModel
{
    public final int groupId;
    public final int spanSize;
    public final String textValue;

    public ItemViewModel(int groupId, int spanSize, String textValue)
    {
        this.groupId = groupId;
        this.textValue = textValue;
        this.spanSize = spanSize;
    }
}