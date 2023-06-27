package com.sdapps.formula1fy.f1.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO

class HomeDriverAdapter(private var data: ArrayList<DriverBO>) :
    RecyclerView.Adapter<HomeDriverAdapter.ViewHolder>() {

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
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val stringBuilder = StringBuilder()
            val points = stringBuilder.append(data[position].totalPoints.toString() + " pts")
            holder.name.text =data[position].driverName
            holder.constructorName.text = data[position].constructorName
            holder.points.text = points
        } catch (ex: Exception) {
            commons.print(ex.message)
            ex.printStackTrace()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var points: TextView
        var constructorName: TextView
        var cardView: CardView

        init {
            name = itemView.findViewById(R.id.name) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            constructorName = itemView.findViewById(R.id.constructorName) as TextView
        }


    }
}

class HomeConstructorAdapter(private val data: ArrayList<ConstructorBO>) :
    RecyclerView.Adapter<HomeConstructorAdapter.ViewHolder>() {

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
            holder.name.text = data[position].name
            holder.position.text =  stringBuilder.append("#"+data[position].position)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var points: TextView
        var position: TextView
        var cardView: CardView

        init {
            name = itemView.findViewById(R.id.name) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            position = itemView.findViewById(R.id.constructorName) as TextView
        }


    }
}