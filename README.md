# grouped-grid-decoration
Sample of an RecyclerView.ItemDecoration that groups cells into card-like treatments. The functionality in question is in the GroupedGridDecoration#onDraw and the Views in the layout being used for their ViewOutlineProviders.


## Background on Issue
We have an ask to have a recycled vertical grid where cells are grouped together inside of cards. Multiple cards, each with multiple recycled cells inside. We're using an ItemDecoration to solve this.

Each cell's view model contains a group identifier that is used in an ItemDecoration to determine where each group starts and ends. Using this information we can iterate over the cells on the screen and draw background(s) behind the groups and update them as the user scrolls.
Prior to API 21 we are adding a shadow by using an offset grey solid under our background. For API 21+ we wanted to use the dynamic shadows with elevation but came across some limitations.

Each View can only have one ViewOutlineProvider and the Outline must be defined as a single contiguous polygon. Since we have multiple multi-cell "cards", each with their own shadow we would need more than just the ViewOutlineProvider on the RecyclerView.

As a workaround, I added N sibling Views to the RecyclerView that took up the same width/height and were not focusable or clickable, then I used those Views as holders for my additional ViewOutlineProviders. This was performant and didn't explode our view hierarchy depth but has a strange artifact. It draws the shadow treatment on top of the RecyclerView, regardless of where the Views are placed with respect to the RecyclerView.

I would rather not fall back on a rasterized 9-patch at different resolutions.

## Additional Information

While putting this sample together I found some alternate solutions without artifacts similar to my original:

**branch: nested_views**
If I wrap my RecyclerView N deep in simple ViewGroups that hold onto the ViewOutlineProviders everything renders in the correct order but I don't like the idea of adding depth to the view tree that way.

**branch: partially_nested_views**
When putting this example together I tried to get a failing and non-failing group so they could be seen side-by-side, but when I wrapped one of the ViewGroups around the RecyclerView and left the others where they were it worked as I expected, which was surprising to me.

## Questions
- Should I just use a 9-patch and never speak of this again?
- Do you have any additional background information or guidance for me with regards to why I'm experiencing this behavior so I can better my solution?

