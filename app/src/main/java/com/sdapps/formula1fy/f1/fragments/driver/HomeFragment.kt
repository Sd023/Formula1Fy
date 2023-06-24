package com.sdapps.formula1fy.f1.fragments.driver

import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.databinding.FragmentDriverBinding
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), HomeContractor.View {
    private lateinit var db: DbHandler
    private lateinit var presenter: HomePresenter
    private lateinit var context: Context

    private var binding: FragmentDriverBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = FragmentDriverBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity?.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN


    }


    override fun onDestroyView() {
        super.onDestroyView()
        //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun initAll() {
        presenter = HomePresenter(context)
        presenter.attachView(this)
        db = DbHandler(context, DataMembers.DB_NAME)
       lifecycleScope.launch {
           presenter.getDriverData(db)
           presenter.getConstructorData(db)
       }

    }
    override fun setConstructorAdapter(list: ArrayList<ConstructorBO>) {
        binding!!.constructorRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val homeConstructorAdapter = HomeConstructorAdapter(list)
        binding!!.constructorRecyclerView.adapter = homeConstructorAdapter
    }

    override fun setDriverAdapter(list: ArrayList<DriverBO>) {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeDriverAdapter(list)
        binding!!.recyclerView.adapter = adapter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }


}