package com.sdapps.formula1fy.f1.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import java.lang.StringBuilder

class HomeNextRaceListAdapter(private var data: ArrayList<RaceScheduleBO>) :
    RecyclerView.Adapter<HomeNextRaceListAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private lateinit var commons: Commons

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.next_race_list, parent, false)
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

            holder.nextRound.text = data[position].raceName
            holder.round.text = StringBuilder().append("Round: ").append(data[position].round)
            holder.nextDateTime.text = data[position].date


        } catch (ex: Exception) {
            commons.print(ex.message)
            ex.printStackTrace()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var nextRound: TextView
        var round: TextView
        var nextDateTime: TextView
        var year: TextView

        init {
            nextRound = view.findViewById(R.id.nextRound)
            round = view.findViewById(R.id.round)
            nextDateTime = view.findViewById(R.id.nextDateTime)
            year = view.findViewById(R.id.year)

        }


    }
}