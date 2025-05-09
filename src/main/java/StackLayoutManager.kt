import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StackLayoutManager : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        if (state.itemCount == 0) return

        // Only layout the first item
        val itemCount = state.itemCount
        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val decoratedWidth = getDecoratedMeasuredWidth(view)
            val decoratedHeight = getDecoratedMeasuredHeight(view)

            Log.d("StackLayoutManager", "Width: $decoratedWidth, Height: $decoratedHeight")

            // Layout the view at the top-left corner
            layoutDecoratedWithMargins(view, 0, 0, decoratedWidth, decoratedHeight)

            // Since we only want to show one item at a time, break after the first item
            break
        }
    }

    override fun canScrollVertically(): Boolean = false
    override fun canScrollHorizontally(): Boolean = false

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        // Request layout to update the view after items are removed
        requestLayout()
    }
}
