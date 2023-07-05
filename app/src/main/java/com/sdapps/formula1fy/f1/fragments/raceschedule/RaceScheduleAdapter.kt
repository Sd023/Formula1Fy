package com.sdapps.formula1fy.f1.fragments.raceschedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import java.lang.StringBuilder

class RaceScheduleAdapter(private val data: ArrayList<RaceScheduleBO>, private val roundNumber: Int) :
    RecyclerView.Adapter<RaceScheduleAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.race_view, parent, false)
        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            holder.roundNo.text = StringBuilder().append("Round: ").append(data[position].round)
            holder.locality.text = StringBuilder().append(data[position].locality).append(" | ").append(data[position].country)
            holder.raceName.text = data[position].raceName
            holder.date.text = data[position].date

            val currentRoundNumber = data[position].round!!.toInt()
            if(roundNumber == currentRoundNumber){
                holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.card_color))
            }



        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var roundNo : TextView
        var locality :TextView
        var raceName : TextView
        var date : TextView
        var cardView : CardView

        init {
            roundNo= view.findViewById(R.id.roundNo)
            locality= view.findViewById(R.id.locality)
            raceName= view.findViewById(R.id.raceName)
            date= view.findViewById(R.id.date)
            cardView = view.findViewById(R.id.cardView)

        }


    }
}