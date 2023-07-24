package com.sdapps.formula1fy.f1.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.LatestResult
import java.lang.StringBuilder

class HomeLatestResultAdapter(private var data: MutableList<LatestResult>) :
    RecyclerView.Adapter<HomeLatestResultAdapter.ViewHolder>() {

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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
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