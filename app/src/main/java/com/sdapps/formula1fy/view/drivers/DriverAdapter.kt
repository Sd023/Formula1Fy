package com.sdapps.formula1fy.view.drivers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.utils.Commons

class DriverAdapter(private var data: ArrayList<DriverBO>) :
    RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private lateinit var commons: Commons

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        context = parent.context
        viewGroup = parent
        commons = Commons()
        return ViewHolder(layout)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val stringBuilder = StringBuilder()
            val points = stringBuilder.append(data[position].totalPoints.toString() + " pts")
            holder.driverName.text =data[position].driverName
            holder.constructorName.text = data[position].constructorName
            holder.points.text = points
        } catch (ex: Exception) {
            commons.print(ex.message)
            ex.printStackTrace()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var driverName: TextView
        var points: TextView
        var constructorName: TextView
        var cardView: CardView

        init {
            driverName = itemView.findViewById(R.id.driverName) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            constructorName = itemView.findViewById(R.id.constructorName) as TextView
        }


    }
}