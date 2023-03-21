package com.sdapps.formula1fy.view.constructors

import android.content.Context
import com.sdapps.formula1fy.core.DbHandler

class ConstructorPresenter(private val context: Context) : ConstructorInteractor.Presenter {
    private lateinit var view: ConstructorInteractor.View
    private lateinit var constructorList: ArrayList<ConstructorBO>


    override fun attachView(view: ConstructorInteractor.View) {
        this.view = view
    }

    override fun detachView() {
        TODO("Not yet implemented")
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

}