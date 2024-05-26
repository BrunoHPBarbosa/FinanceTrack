package com.hacksprint.financetrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hacksprint.financetrack.R

class ListIconsAdapter(
    private val icons: List<Int>,
    private val iconClickListener: IconClickListener
) : RecyclerView.Adapter<ListIconsAdapter.IconViewHolder>() {

    private var selectedIconPosition: Int = -1 // Índice do ícone selecionado

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.icon_selection_style, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = icons[position]
        holder.iconImageView.setImageResource(icon)

        // Define a visibilidade do círculo de seleção com base no ícone selecionado
        holder.selectionCircle.visibility = if (holder.adapterPosition == selectedIconPosition) View.VISIBLE else View.GONE

        // Define um clique ouvinte para os ícones
        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedIconPosition
            selectedIconPosition = holder.adapterPosition
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedIconPosition)

            // Notifica o ouvinte de clique do ícone
            iconClickListener.onIconClicked(icon)
        }
    }

    override fun getItemCount(): Int {
        return icons.size
    }

    // Define um método para atualizar o ícone selecionado externamente
    fun setSelectedIconPosition(position: Int) {
        selectedIconPosition = position
        notifyDataSetChanged()
    }

    // Classe ViewHolder para os ícones
    inner class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.icon_list)
        val selectionCircle: View = itemView.findViewById(R.id.circle_select)
    }

    // Interface para lidar com eventos de clique nos ícones
    interface IconClickListener {
        fun onIconClicked(iconResId: Int)
    }
}
