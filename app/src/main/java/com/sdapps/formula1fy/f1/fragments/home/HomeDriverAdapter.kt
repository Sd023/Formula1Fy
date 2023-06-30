package com.sdapps.formula1fy.f1.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.LatestResult
import java.lang.StringBuilder

class HomeDriverAdapter(private var data: ArrayList<DriverBO>) :
    RecyclerView.Adapter<HomeDriverAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private lateinit var commons: Commons

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
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
            holder.driver_position.text = data[position].driverPosition
            holder.driver_name.text = data[position].driverName
            holder.team_name.text = data[position].constructorName
            holder.driver_points.text = data[position].totalPoints.toString()

        } catch (ex: Exception) {
            commons.print(ex.message)
            ex.printStackTrace()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var driver_name: TextView
        var driver_position: TextView
        var team_name: TextView
        var driver_points: TextView
        var team_color: ImageView

        init {
            driver_name = view.findViewById(R.id.driverName)
            driver_position = view.findViewById(R.id.position)
            team_name = view.findViewById(R.id.teamName)
            driver_points = view.findViewById(R.id.totalPoints)
            team_color = view.findViewById(R.id.teamBrandColor)

        }


    }
}

class HomeConstructorAdapter(private val data: ArrayList<ConstructorBO>) :
    RecyclerView.Adapter<HomeConstructorAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
           holder.constructor_name.text = data[position].name
            holder.constructor_points.text = data[position].points
            holder.constructor_position.text = data[position].position
            if(data[position].consId.equals("REDBULL")){

            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return 5
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var constructor_name: TextView
        var constructor_points: TextView
        var constructor_position : TextView


        init {
            constructor_name = view.findViewById(R.id.driverName)
            constructor_points = view.findViewById(R.id.totalPoints)
            constructor_position = view.findViewById(R.id.position)

        }

    }
}

class LatestResultAdapter(private var data: MutableList<LatestResult>) :
    RecyclerView.Adapter<LatestResultAdapter.ViewHolder>() {

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
            val sb : StringBuilder = StringBuilder()
            sb.append(data[position].latestRoundPoints).append("Pts")
            holder.driver_name.text = data[position].driverName
            holder.constructorName.text = data[position].teamName
            holder.current_points.text = sb.toString()
        } catch (ex: Exception) {
            commons.print(ex.message)
            ex.printStackTrace()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var driver_name: TextView
        var constructorName: TextView
        var current_points: TextView

        init {
            driver_name = view.findViewById(R.id.name)
            constructorName = view.findViewById(R.id.constructorName)
            current_points = view.findViewById(R.id.points)

        }


    }
}