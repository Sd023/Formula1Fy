package com.sdapps.formula1fy.home

interface HomeScreenInteractor {
    interface View {
        fun loadScreen()
        fun getMessageFromDead()

        fun moveToNextScreen(isDriver: Boolean)

    }

    interface Presenter {
        fun setupView(view: View)

    }
}