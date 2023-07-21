package com.sdapps.formula1fy.f1.fragments.driver.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.models.F1Contants
import com.sdapps.formula1fy.databinding.ModalViewBottomBinding
import com.sdapps.formula1fy.f1.bo.DriverBO


class BottomModal(val driverBo: DriverBO): BottomSheetDialogFragment() {
    private lateinit var context : Context

    private var binding: ModalViewBottomBinding? = null
    private lateinit var db : DbHandler
    private lateinit var raceresults: ArrayList<Race>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = ModalViewBottomBinding.inflate(inflater, container, false)
        initAll()
        return binding!!.root
    }

    fun initAll(){
        db = DbHandler(context, DataMembers.DB_NAME)
        binding!!.driverDOB.text = driverBo.driverDOB
        binding!!.driverName.text = driverBo.driverName
        binding!!.driverNation.text = driverBo.driverNationality
        val data =  getTheData()
        var dMap : HashMap<String, Int> = hashMapOf()
        val dList : ArrayList<PieEntry> = arrayListOf()

        dMap["Wins"] = data
        dMap["Total"] = 10
        for(type in dMap.keys){
            dList.add(PieEntry(dMap[type]!!.toFloat(), type))
        }
        val teamColorId = driverBo.constructorId
        val teamColor = F1Contants.teamColorMap.getOrDefault(teamColorId, ContextCompat.getColor(context, R.color.card_color))



        val pieDataSet = PieDataSet(dList,"")
        pieDataSet.valueTextSize = 12f //pie-chart data size
        pieDataSet.colors =
            arrayListOf(ContextCompat.getColor(context, R.color.card_color), ContextCompat.getColor(context,teamColor))
        binding!!.chart.description.isEnabled = false
        binding!!.chart.legend.textSize = 14f
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        binding!!.chart.data = pieData
        binding!!.chart.invalidate()


    }

    fun getTheData(): Int{

        try{
            db.openDB()
            raceresults = arrayListOf()
            raceresults.clear()
            val cursor = db.selectSql("select distinct season, round, start_position as gridPos, " +
                    "driver_position as endPos from CurrentSeasonResults where driver_code = '${driverBo.driverCode}'")

            if(cursor != null){

                while(cursor.moveToNext()){
                    val round = cursor.getString(1)
                    val startpo = cursor.getString(2)
                    val endpo = cursor.getString(3)
                    raceresults.add(Race(startpo,endpo))

                }
                cursor.close()
            }
            if(raceresults != null){
                val totalRace = raceresults.size
                val raceWon = raceresults.count { it.endPos == "1" }
                return raceWon

            }else {
                return 0
            }


        }catch (ex: Exception){
            ex.printStackTrace()
        }
        return 0
    }
    companion object {
        const val TAG = "BottomModalSheet"
    }


    data class Race(val startPos: String, val endPos: String)
}