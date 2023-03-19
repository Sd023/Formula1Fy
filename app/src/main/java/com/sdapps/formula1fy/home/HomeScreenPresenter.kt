package com.sdapps.formula1fy.home

import android.content.Context

class HomeScreenPresenter(val context: Context) : HomeScreenInteractor.Presenter {

    private var view: HomeScreenInteractor.View? = null
    override fun setupView(view: HomeScreenInteractor.View) {
        this.view = view
    }

    public fun moveToNextScreen(isDriver: Boolean) {
        view?.moveToNextScreen(isDriver)
    }


}