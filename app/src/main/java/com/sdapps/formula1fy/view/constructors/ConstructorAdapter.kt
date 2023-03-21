package com.sdapps.formula1fy.view.constructors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R

class ConstructorAdapter(private val data: ArrayList<ConstructorBO>) :
    RecyclerView.Adapter<ConstructorAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val stringBuilder = StringBuilder()
            holder.standing.text = data[position].position
            holder.driverName.text = stringBuilder.append(data[position].name)
            holder.points.text = data[position].points.toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var driverName: TextView
        var points: TextView
        var standing: TextView
        var cardView: CardView

        init {
            driverName = itemView.findViewById(R.id.driverName) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            standing = itemView.findViewById(R.id.driverPosition) as TextView
        }


    }
}