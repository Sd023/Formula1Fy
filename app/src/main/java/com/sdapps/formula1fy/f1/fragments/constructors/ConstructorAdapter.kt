package com.sdapps.formula1fy.f1.fragments.constructors

import android.content.Context
import android.media.Image
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
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.ConstructorNewBO

class ConstructorAdapter(private val data: ArrayList<ConstructorBO>, val listValues: ArrayList<ConstructorNewBO>) :
    RecyclerView.Adapter<ConstructorAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private  var driver1 : String = String()
    private  var driver2 : String = String()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.main_driver_item, parent, false)
        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            for(datas in listValues){
                if(datas.teamName.equals(data[position].name)){
                    driver1 = datas.driversOfTeam[0]
                    driver2 = datas.driversOfTeam[1]
                }
            }
            val stringBuilder = StringBuilder()
            stringBuilder.clear()
            holder.driverMainName.text = data[position].name
            holder.driverTotalPoints.text = StringBuilder().append(data[position].points.toString() + " pts")
            holder.driverTeamName.text = StringBuilder().append(driver1 + " | " + driver2)


            val colorId = data[position].consId
            val colorValue = F1Contants.teamColorMap.getOrDefault(colorId, R.color.ferrari_red)

            holder.teamColor.setBackgroundColor(ContextCompat.getColor(context, colorValue))

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var driverMainName: TextView
        var driverTotalPoints: TextView
        var driverTeamName: TextView
        var teamColor : ImageView

        init {
            driverMainName = itemView.findViewById(R.id.driverMainName) as TextView
            driverTeamName = itemView.findViewById(R.id.driverTeamName) as TextView
            driverTotalPoints = itemView.findViewById(R.id.driverTotalPoints) as TextView
            teamColor = itemView.findViewById(R.id.teamColor)
        }


    }
}