package uz.gka.tapyoutest.presentation.result

import androidx.recyclerview.widget.RecyclerView
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.databinding.ItemPointBinding
import uz.gka.tapyoutest.domain.model.Point

class PointViewHolder(private val binding: ItemPointBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(point: Point) = with(binding) {
        tvPoints.text = tvPoints.context.getString(R.string.result_points, point.x, point.y)
    }
}