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
import com.sdapps.formula1fy.login.F1Contants

class DriverAdapter(private var data : ArrayList<DriverBO>, context: Context) : RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewGroup : ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stringBuilder = StringBuilder()
        holder.driverName.text =
            stringBuilder.append(data[position].driverName).append("\n").append(
                data[position].driverNumber
            ).toString()
        holder.driverNumber.text = StringBuilder().append(data[position].constructorId)
        holder.points.text =
            StringBuilder().append("Points: ").append(data[position].totalPoints)
        holder.standing.text = data[position].driverPosition
        try {
            if (data[position].driverId.equals(
                    F1Contants.ALBON,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.williams)
            ) else if (data[position].driverId.equals(
                    F1Contants.LATIFI,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.williams)
            ) else if (data[position].driverId.equals(
                    F1Contants.NICK_DE_VRIES,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.williams)
            ) else if (data[position].driverId.equals(
                    F1Contants.ALONSO,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.alpine)
            ) else if (data[position].driverId.equals(
                    F1Contants.OCON,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.alpine)
            ) else if (data[position].driverId.equals(
                    F1Contants.BOTTAS,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.alfa_romeo_racing)
            ) else if (data[position].driverId.equals(
                    F1Contants.ZHOU,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.alfa_romeo_racing)
            ) else if (data[position].driverId.equals(
                    F1Contants.GASLY,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.alphatauri)
            ) else if (data[position].driverId.equals(
                    F1Contants.TSUNODA,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.alphatauri)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.HAMILTON,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.mercedes)
            ) else if (data[position].driverId.equals(
                    F1Contants.RUSSELL,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.mercedes)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.HULKENBERG,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.aston_martin)
            ) else if (data[position].driverId.equals(
                    F1Contants.STROLL,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.aston_martin)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.VETTEL,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.aston_martin)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.LECLERC,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.ferrari)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.SAINZ,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.ferrari)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.KMAG,
                    ignoreCase = true
                )
            ) {
                holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.haas))
                holder.driverName.setTextColor(context.resources.getColor(R.color.black))
                holder.driverNumber.setTextColor(context.resources.getColor(R.color.black))
                holder.points.setTextColor(context.resources.getColor(R.color.black))
                holder.standing.setTextColor(context.resources.getColor(R.color.black))
            } else if (data[position]!!.driverId.equals(
                    F1Contants.SCHUMACHER,
                    ignoreCase = true
                )
            ) {
                holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.haas))
                holder.driverName.setTextColor(context.resources.getColor(R.color.black))
                holder.driverNumber.setTextColor(context.resources.getColor(R.color.black))
                holder.points.setTextColor(context.resources.getColor(R.color.black))
                holder.standing.setTextColor(context.resources.getColor(R.color.black))
            } else if (data[position]!!.driverId.equals(
                    F1Contants.NORRIS,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.mclaren)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.RICCIARDO,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.mclaren)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.PEREZ,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.redbull_racing)
            ) else if (data[position]!!.driverId.equals(
                    F1Contants.VERSTAPPEN,
                    ignoreCase = true
                )
            ) holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.redbull_racing)
            ) else holder.cardView.setCardBackgroundColor(
                context.resources.getColor(R.color.generic)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        var driverName: TextView
        var driverNumber: TextView
        var points: TextView
        var standing: TextView
        var cardView: CardView

        init {
            driverName = itemView.findViewById(R.id.driverName) as TextView
            driverNumber = itemView.findViewById(R.id.driverTeamNo) as TextView
            cardView = itemView.findViewById(R.id.cardView)
            points = itemView.findViewById(R.id.points) as TextView
            standing = itemView.findViewById(R.id.standing) as TextView
        }



    }
}