package com.sdapps.formula1fy.f1.landing

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.utils.NetworkTools
import com.sdapps.formula1fy.f1.base.view.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LandingScreenActivity : AppCompatActivity(), LandingContractor.View,
    View.OnClickListener {

    private lateinit var appNameTitle: TextView
    private lateinit var startBtn: Button
    private lateinit var landPresenter: LandingPresenter
    private lateinit var dbHandler: DbHandler
    private lateinit var manager: LandingManager
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert : AlertDialog

    private lateinit var pf: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        pf = ProgressDialog(this)
        setContentView(R.layout.activity_landing_screen)
        landPresenter = LandingPresenter(applicationContext)
        dbHandler = DbHandler(applicationContext, DataMembers.DB_NAME)
        dbHandler.createDB()
        landPresenter.attachView(this, applicationContext)
        manager = LandingManager(landPresenter, dbHandler)
        manager.attachManagerview(this)
        initAll()

    }


    override fun showLoading() {
        pf.setTitle(resources.getString(R.string.app_name))
        pf.setMessage(resources.getString(R.string.fetching_data))
        pf.setCancelable(false)
        pf.show()
    }

    override fun hideLoading() {
        try {
            pf.dismiss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun moveToNextScreen() {
        startActivity(Intent(this@LandingScreenActivity, BaseActivity::class.java))
        finish()
    }

    override fun initAll() {
        startBtn = findViewById(R.id.start)
        appNameTitle = findViewById(R.id.landingAppName)
        appNameTitle.setText(landPresenter?.getAppString(), TextView.BufferType.SPANNABLE)
        startBtn.setOnClickListener(this)
    }

    override fun showDialog() {
        dialog = AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.offline_mode))
            .setTitle(resources.getString(R.string.no_internet))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()
                moveToNextScreen()

            })

        alert = dialog.create()
        alert.show()
    }

    override fun onClick(v: View?) {
        CoroutineScope(Dispatchers.Main).launch {
            manager.getAllNecessaryData(applicationContext)
        }



    }

    override fun showAlert(){
        dialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.no_internet))
            .setMessage(resources.getString(R.string.unable_to_download))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()
            })

        alert = dialog.create()
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        landPresenter!!.detachView()
    }

    override fun onResume() {
        super.onResume()
    }

     fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}