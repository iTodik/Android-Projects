package ge.itodadze.todoapp.view.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StaggeredGridSpacingItemDecoration(private val spacingHorizontal: Int,
                                         private val spacingVertical: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.left = spacingHorizontal
        outRect.right = spacingHorizontal
        outRect.bottom = spacingVertical
        outRect.top = spacingVertical

    }
}