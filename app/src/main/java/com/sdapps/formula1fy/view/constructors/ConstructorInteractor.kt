package com.sdapps.formula1fy.view.constructors

import com.sdapps.formula1fy.core.DbHandler

interface ConstructorInteractor {

    interface View{
        fun showLoading()
        fun hideLoading()

        fun initAll()

    }

    interface Presenter{
        fun getConstructorData(db:DbHandler): ArrayList<ConstructorBO>
        fun attachView(view: View)

        fun detachView()
    }
}