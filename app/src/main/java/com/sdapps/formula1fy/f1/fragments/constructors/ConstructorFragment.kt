package com.sdapps.formula1fy.f1.fragments.constructors

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers

import com.sdapps.formula1fy.databinding.FragmentConstructorBinding
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import java.time.LocalDate
import kotlin.math.round

class ConstructorFragment : Fragment(), ConstructorInteractor.View {

    private var binding : FragmentConstructorBinding? = null
    private lateinit var presenter: ConstructorPresenter
    private lateinit var db: DbHandler
    private lateinit var recyclerView: RecyclerView
    private lateinit var context: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConstructorBinding.inflate(inflater, container, false)
        context = requireContext().applicationContext
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
    }

    override fun initAll() {
        presenter = ConstructorPresenter(context)
        presenter.attachView(this)
        db = DbHandler(context, DataMembers.DB_NAME)
        db.createDB()
        val consList = presenter.getConstructorData(db)
        loadConstructorView(consList)

    }

    private fun loadConstructorView(list: ArrayList<ConstructorBO>) {
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val consAdapter = ConstructorAdapter(list)
        binding!!.recyclerView.adapter = consAdapter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

}