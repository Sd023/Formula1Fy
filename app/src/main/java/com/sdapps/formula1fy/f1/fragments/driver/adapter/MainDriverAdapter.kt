package com.sdapps.formula1fy.f1.fragments.driver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.F1Contants
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.fragments.driver.DriverInteractor
import java.lang.StringBuilder

class MainDriverAdapter(private var data: ArrayList<DriverBO>, val view: DriverInteractor.View) :
    RecyclerView.Adapter<MainDriverAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private lateinit var commons: Commons
    private var teamId : String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_driver_item, parent, false)
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

            teamId = data[position].constructorId
            val teamColor = F1Contants.teamColorMap.getOrDefault(teamId,R.color.ferrari_red)
            holder.teamColor.setBackgroundColor(ContextCompat.getColor(context, teamColor))

            holder.mainDriverName.text = data[position].driverName
            holder.driverTeamName.text = data[position].constructorName
            holder.driverTotalPoints.text = StringBuilder().append(data[position].totalPoints.toString()).append(" pts")

            holder.cardView.setOnClickListener{
                view.onCardClick(data[position])
            }

        } catch (ex: Exception) {
            commons.print(ex.message)
            ex.printStackTrace()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mainDriverName: TextView
        var driverTeamName : TextView
        var driverTotalPoints : TextView
        var cardView: CardView
        var teamColor : ImageView
        var expandListBtn : ImageView

        init {
            mainDriverName = view.findViewById(R.id.driverMainName)
            driverTeamName = view.findViewById(R.id.driverTeamName)
            driverTotalPoints = view.findViewById(R.id.driverTotalPoints)
            teamColor = view.findViewById(R.id.teamColor)
            cardView = view.findViewById(R.id.driverCardView)
            expandListBtn = view.findViewById(R.id.expandListBtn)

        }


    }
}