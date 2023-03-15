package com.sdapps.formula1fy.home.homeadapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.R

class ConstructorAdapter(private val data : ArrayList<ConstructorBO>) : RecyclerView.Adapter<ConstructorAdapter.ViewHolder>(){

   private lateinit var context: Context
   private lateinit var viewGroup : ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        context = parent.context
        viewGroup = parent
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
       return data.size
    }


    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {


    }
}