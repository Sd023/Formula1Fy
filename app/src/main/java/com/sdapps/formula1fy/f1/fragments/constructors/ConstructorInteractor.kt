package com.sdapps.formula1fy.f1.fragments.constructors

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO

interface ConstructorInteractor {

    interface View{
        fun showLoading()
        fun hideLoading()

        fun initAll()

        fun setAdapter(list: ArrayList<ConstructorBO>, map : HashMap<String, ArrayList<String>>)
    }

    interface Presenter{
        fun getConstructorData(db: DbHandler)
        fun attachView(view: View)

        fun detachView()
    }
}