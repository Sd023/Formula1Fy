package com.sdapps.formula1fy.home

import android.content.Context
import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.core.DbHandler

class HomeScreenPresenter(val context: Context) : HomeScreenInteractor.Presenter {

    private var view: HomeScreenInteractor.View? = null
    private lateinit var constructorList: ArrayList<ConstructorBO>
    override fun setupView(view: HomeScreenInteractor.View) {
        this.view = view
    }


    override fun getConstructorData(db: DbHandler): ArrayList<ConstructorBO> {
        constructorList = ArrayList<ConstructorBO>()
        try {
            db.openDB()
            val c =
                db.selectSql("SELECT constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality from ConstructorMaster")
            if (c != null) {
                while (c.moveToNext()) {
                    val bo = ConstructorBO()
                    bo.consId = c.getString(0)
                    bo.name = c.getString(1)
                    bo.wins = c.getString(2)
                    bo.points = c.getString(3)
                    bo.position = c.getString(4)
                    bo.nationality = c.getString(5)
                    constructorList.add(bo)
                }
            }
            c!!.close()
            db.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()
            db.closeDB()
        }
        return constructorList
    }

    public fun moveToNextScreen(){
        view?.moveToNextScreen()
    }


}