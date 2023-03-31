package com.sdapps.formula1fy.f1.constructors.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.constructors.adapter.ConstructorAdapter
import com.sdapps.formula1fy.f1.constructors.ConstructorInteractor
import com.sdapps.formula1fy.f1.constructors.presenter.ConstructorPresenter

class ConstructorActivity : AppCompatActivity(), ConstructorInteractor.View {
    private lateinit var presenter: ConstructorPresenter
    private lateinit var db: DbHandler
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constructor)

        initAll()
    }

    override fun initAll() {
        presenter = ConstructorPresenter(this)
        presenter.attachView(this)
        recyclerView = findViewById(R.id.recyclerView)
        db = DbHandler(this, DataMembers.DB_NAME)
        db.createDB()

        val consList = presenter.getConstructorData(db)
        loadConstructorView(consList)
    }

    private fun loadConstructorView(list: ArrayList<ConstructorBO>) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val consAdapter = ConstructorAdapter(list)
        recyclerView.adapter = consAdapter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }


}