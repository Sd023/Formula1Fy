package com.sdapps.formula1fy.f1.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.F1Contants
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.ConstructorNewBO
import java.lang.StringBuilder

class HomeConstructorAdapter(private val data: ArrayList<ConstructorBO>,val listValues : ArrayList<ConstructorNewBO>) :
    RecyclerView.Adapter<HomeConstructorAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup: ViewGroup
    private  var driver1 : String = String()
    private  var driver2 : String = String()
    private  var teamColorId : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
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
            val sb = StringBuilder()
             teamColorId = data[position].consId

            val teamColor = F1Contants.teamColorMap.getOrDefault(teamColorId,R.color.ferrari_red)
            holder.teamBrandColor.setBackgroundColor(ContextCompat.getColor(context, teamColor))

            holder.constructor_name.text = data[position].name
            holder.constructor_points.text = data[position].points
            holder.teamDriver.text = sb.append(driver1).append(" | ").append(driver2)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
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


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var constructor_name: TextView
        var constructor_points: TextView
        var teamDriver : TextView
        var teamBrandColor : ImageView

        init {
            constructor_name = view.findViewById(R.id.teamName)
            constructor_points = view.findViewById(R.id.totalPoints)
            teamDriver = view.findViewById(R.id.teamDrivers)
            teamBrandColor = view.findViewById(R.id.teamBrandColor)

        }

    }
}
