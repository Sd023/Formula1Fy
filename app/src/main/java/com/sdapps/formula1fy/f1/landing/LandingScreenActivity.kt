package com.sdapps.formula1fy.f1.landing

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.f1.home.presenter.HomeScreenPresenter
import com.sdapps.formula1fy.f1.home.view.HomeScreenActivity

class LandingScreenActivity : AppCompatActivity(), LandingContractor.View, View.OnClickListener{

   private lateinit var appNameTitle : TextView
   private lateinit var startBtn : Button
   private var landPresenter : LandingPresenter? = null
   private lateinit var dbHandler: DbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_screen)
        landPresenter = LandingPresenter(applicationContext)
        dbHandler = DbHandler(applicationContext,DataMembers.DB_NAME)
        dbHandler.createDB()
        landPresenter!!.attachView(this, applicationContext)
        initAll()

    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun moveToNextScreen() {
       startActivity(Intent(this@LandingScreenActivity, HomeScreenActivity::class.java))
       finish()
    }

    override fun initAll() {
        startBtn = findViewById(R.id.start)
        startBtn.setOnClickListener(this)
        appNameTitle = findViewById(R.id.landingAppName)
        appNameTitle.setText(landPresenter?.getAppString(), TextView.BufferType.SPANNABLE)
    }

    override fun onClick(v: View?) {
        landPresenter!!.fetchAllData()

    }

    override fun onDestroy() {
        super.onDestroy()
        landPresenter!!.detachView()
    }

    override fun onResume() {
        super.onResume()
    }
}