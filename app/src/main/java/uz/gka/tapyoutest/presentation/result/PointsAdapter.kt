package uz.gka.tapyoutest.presentation.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gka.tapyoutest.databinding.ItemPointBinding
import uz.gka.tapyoutest.domain.model.Point

class PointsAdapter(private val items: List<Point>) : RecyclerView.Adapter<PointViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPointBinding.inflate(inflater, parent, false)
        return PointViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
