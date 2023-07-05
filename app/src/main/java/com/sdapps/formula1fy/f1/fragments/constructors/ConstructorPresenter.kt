package com.sdapps.formula1fy.f1.fragments.constructors

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.fragments.home.HomePresenter

class ConstructorPresenter(private val context: Context) : ConstructorInteractor.Presenter {
    private lateinit var view: ConstructorInteractor.View
    private lateinit var constructorList: ArrayList<ConstructorBO>


    override fun attachView(view: ConstructorInteractor.View) {
        this.view = view
    }

    override fun detachView() {

    }

    override fun getConstructorData(db: DbHandler) {
        db.openDB()
        try{
            constructorList = arrayListOf()
            val c = db.selectSql("SELECT constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality FROM ConstructorMaster")
            if(c != null){
                while(c.moveToNext()){
                    val constructorBO = ConstructorBO().apply {
                        consId = c.getString(0)
                        name = c.getString(1)
                        wins = c.getString(2)
                        points = c.getString(3)
                        position = c.getString(4)
                        nationality = c.getString(5)
                    }
                    constructorList.add(constructorBO)
                }
                c.close()
            }
        }catch (ex: Exception){
            Commons().printException(ex)
        }
        view.setAdapter(constructorList, HomePresenter(context).getDriverConstructorMap(db))
    }


}