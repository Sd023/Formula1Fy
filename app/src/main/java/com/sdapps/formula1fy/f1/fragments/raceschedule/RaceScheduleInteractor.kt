package com.sdapps.formula1fy.f1.fragments.raceschedule

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO

interface RaceScheduleInteractor {

    interface View{
        fun setAdapter(list: ArrayList<RaceScheduleBO>)

    }

    interface Presenter{
        fun attachView(view: RaceScheduleInteractor.View)
        fun getRaceCalendar()

        fun getCurrentRound(db: DbHandler): Int
    }
}