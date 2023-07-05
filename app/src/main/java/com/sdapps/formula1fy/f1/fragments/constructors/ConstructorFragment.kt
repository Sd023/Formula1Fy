package com.sdapps.formula1fy.f1.fragments.constructors

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers

import com.sdapps.formula1fy.databinding.FragmentConstructorBinding
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.ConstructorNewBO
import com.sdapps.formula1fy.f1.fragments.home.HomePresenter
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.round

class ConstructorFragment : Fragment(), ConstructorInteractor.View {

    private var binding : FragmentConstructorBinding? = null
    private lateinit var presenter: ConstructorPresenter
    private lateinit var db: DbHandler
    private lateinit var context: Context
    private lateinit var helper: HomePresenter
    private lateinit var listValues : ArrayList<ConstructorNewBO>

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
        presenter.getConstructorData(db)
    }

    override fun setAdapter(list: ArrayList<ConstructorBO>, map : HashMap<String, ArrayList<String>>) {
        listValues = arrayListOf()
        for(entry in map.entries){
            val teamBO = ConstructorNewBO(entry.key,entry.value)
            listValues.add(teamBO)
        }
        binding!!.constructorRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val consAdapter = ConstructorAdapter(list,listValues)
        binding!!.constructorRecyclerView.adapter = consAdapter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

}