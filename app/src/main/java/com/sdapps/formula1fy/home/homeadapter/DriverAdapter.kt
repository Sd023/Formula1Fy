package com.sdapps.formula1fy.home.homeadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.Commons
import com.sdapps.formula1fy.login.F1Contants

class DriverAdapter(private var data : ArrayList<DriverBO>) : RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup : ViewGroup
    private lateinit var commons: Commons

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        context = parent.context
        viewGroup = parent
        commons = Commons(context)
        return ViewHolder(layout)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       try{
           val stringBuilder = StringBuilder()
           holder.standing.text = data[position].driverPosition
           holder.driverName.text = stringBuilder.append(data[position].driverName)
           holder.points.text = data[position].totalPoints.toString()
       }catch (ex: Exception){
           commons.print(ex.message)
           ex.printStackTrace()
       }
    }

    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        var driverName: TextView
        var points: TextView
        var standing: TextView
        var cardView: CardView

        init {
            driverName = itemView.findViewById(R.id.driverName) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            standing = itemView.findViewById(R.id.driverPosition) as TextView
        }



    }
}