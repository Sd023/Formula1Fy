package com.sdapps.formula1fy.f1.fragments.constructors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.f1.bo.ConstructorBO

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
            val points = stringBuilder.append(data[position].points.toString() + " pts")
            holder.points.text = points
            stringBuilder.clear()
            holder.driverName.text = data[position].name
            holder.position.text =  stringBuilder.append("#"+data[position].position)
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
        var position: TextView
        var cardView: CardView

        init {
            driverName = itemView.findViewById(R.id.driverName) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            position = itemView.findViewById(R.id.constructorName) as TextView
        }


    }
}