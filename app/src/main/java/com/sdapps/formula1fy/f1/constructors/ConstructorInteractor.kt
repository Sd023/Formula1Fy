package com.sdapps.formula1fy.f1.constructors

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO

interface ConstructorInteractor {

    interface View{
        fun showLoading()
        fun hideLoading()

        fun initAll()

    }

    interface Presenter{
        fun getConstructorData(db: DbHandler): ArrayList<ConstructorBO>
        fun attachView(view: View)

        fun detachView()
    }
}