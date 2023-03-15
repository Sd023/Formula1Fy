package com.sdapps.formula1fy.login

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.home.HomeScreenActivity

class LoginScreenActivity : AppCompatActivity(), LoginContractor.View, OnClickListener {


    private lateinit var presenter: LoginScreenPresenter
    private lateinit var loginButton: Button
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var db: DbHandler
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB();
        initViews();
    }

    fun initDB() {
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        db.createDB()
    }


    fun initViews() {
        progressDialog = ProgressDialog(this)
        loginButton = findViewById(R.id.loginBtn)
        emailEdit = findViewById(R.id.loginEmail)
        passwordEdit = findViewById(R.id.loginPassword)
        presenter = LoginScreenPresenter(this)
        presenter.attachView(this)
        loginButton.setOnClickListener(this)
        auth = Firebase.auth
    }


    override fun showAlert() {

    }

    override fun onError() {
        Toast.makeText(applicationContext, "Error Fetching Details!", Toast.LENGTH_LONG).show()

    }

    override fun showLoading() {
        progressDialog.apply {
            setTitle("Formula1Fy")
            setMessage("Fetching Data...")
            setCancelable(false)
            show()
        }
    }

    override fun hideLoading() {
        progressDialog.hide()
    }

    override fun onClick(p0: View?) {
        try {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            presenter.performLogin(email, password)

        } catch (ex: Exception) {
            onError()
            ex.printStackTrace()
        }

    }
}