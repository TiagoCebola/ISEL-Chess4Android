package pt.isel.pdm.chess4android.history

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.chess4android.daily.DayPuzzleDTO
import pt.isel.pdm.chess4android.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of the ViewHolder pattern. Its purpose is to eliminate the need for
 * executing findViewById each time a reference to a view's child is required.
 */
class HistoryItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val dayView = itemView.findViewById<TextView>(R.id.day)
    private val puzzleIdView = itemView.findViewById<TextView>(R.id.puzzleId)
    private val resolvedView = itemView.findViewById<TextView>(R.id.isResolved)

    /**
     * Binds this view holder to the given puzzle item
     */
    fun bindTo(dayPuzzleDTO: DayPuzzleDTO, onItemClick: () -> Unit) {
        dayView.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dayPuzzleDTO.timestamp).toString()
        puzzleIdView.text = dayPuzzleDTO.puzzle.id
        if(!dayPuzzleDTO.status) resolvedView.text =  "Unresolved"
        else resolvedView.text =  "Resolved"


        itemView.setOnClickListener {
            // Avoid double click
            itemView.isClickable = false
            startAnimation {
                onItemClick()
                itemView.isClickable = true
            }
        }

    }

    /**
     * Starts the item selection animation and calls [onAnimationEnd] once the animation ends
     */
    private fun startAnimation(onAnimationEnd: () -> Unit) {

        val animation = ValueAnimator.ofArgb(
            ContextCompat.getColor(itemView.context, R.color.list_item_background),
            ContextCompat.getColor(itemView.context, R.color.list_item_background_selected),
            ContextCompat.getColor(itemView.context, R.color.list_item_background)
        )

        animation.addUpdateListener { animator ->
            val background = itemView.background as GradientDrawable
            background.setColor(animator.animatedValue as Int)
        }

        animation.duration = 400
        animation.doOnEnd { onAnimationEnd() }

        animation.start()
    }

}

/**
 * Adapts items in a data set to RecycleView entries
 */
class HistoryAdapter(
    private val dataSource : List<DayPuzzleDTO>,
    private val onItemClick: (DayPuzzleDTO) -> Unit
) : RecyclerView.Adapter<HistoryItemViewHolder>() {

    /**
     * Factory method of view holders (and its associated views)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_list, parent, false)
        return HistoryItemViewHolder(view)
    }

    /**
     * Associates a view into a new data element
     */
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bindTo(dataSource[position]) {
            onItemClick(dataSource[position])
        }
    }

    /**
     * The size of the data set
     */
    override fun getItemCount() = dataSource.size

}