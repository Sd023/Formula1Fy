package com.sdapps.formula1fy.f1.fragments.constructors

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO

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
       return constructorList
    }


}