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
        val view = recycler.getViewForPosition(0)
        addView(view)
        measureChildWithMargins(view, 0, 0)

        val decoratedWidth = getDecoratedMeasuredWidth(view)
        val decoratedHeight = getDecoratedMeasuredHeight(view)

        Log.d("StackLayoutManager", "Width: $decoratedWidth, Height: $decoratedHeight")

        // Layout the view at the top-left corner
        layoutDecoratedWithMargins(view, 0, 0, decoratedWidth, decoratedHeight)
    }

    override fun canScrollVertically(): Boolean = false
    override fun canScrollHorizontally(): Boolean = false

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        // Request layout to update the view after items are removed
        requestLayout()
    }

    override fun onItemsChanged(recyclerView: RecyclerView) {
        super.onItemsChanged(recyclerView)
        requestLayout()
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        // Ensure the layout is updated after any changes
        requestLayout()
    }
}
