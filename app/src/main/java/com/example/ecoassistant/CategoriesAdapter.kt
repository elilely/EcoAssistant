package com.example.ecoassistant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoriesAdapter(
    private val context: GuideFragment,
    private var categoriesList: List<DataClass>
) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class CategoriesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {//, clickListener: OnItemClickListener

        var materialImage: ImageView
        var materialTitle: TextView
        var materialCard: CardView

        init {
            materialImage = itemView.findViewById(R.id.sivImage)
            materialCard = itemView.findViewById(R.id.card)
            materialTitle = itemView.findViewById(R.id.materialTitle)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(categoriesList: List<DataClass>) {
        this.categoriesList = categoriesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_categories,
            parent, false
        )
        return CategoriesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currentItem = categoriesList[position]
        Glide.with(context).load(currentItem.image).into(holder.materialImage)
        holder.materialTitle.text = currentItem.name
        holder.materialCard.setOnClickListener {
            mListener.onItemClick(position)
        }
    }
}