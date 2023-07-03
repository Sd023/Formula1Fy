package com.sdapps.formula1fy.f1.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import java.lang.StringBuilder

class HomeConstructorAdapter(private val data: ArrayList<ConstructorBO>, val driversNameList : ArrayList<String>) :
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
         /*   var driverOne = driversNameList[0]
            var driverTwo = driversNameList[1]
            val sb = StringBuilder()
         */   holder.constructor_name.text = data[position].name
            holder.constructor_points.text = data[position].points
           /* holder.teamDriver.text = sb.append(driverOne).append(" | ").append(driverTwo)*/

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
        var teamDriver : TextView

        init {
            constructor_name = view.findViewById(R.id.teamName)
            constructor_points = view.findViewById(R.id.totalPoints)
            teamDriver = view.findViewById(R.id.teamDrivers)

        }

    }
}
