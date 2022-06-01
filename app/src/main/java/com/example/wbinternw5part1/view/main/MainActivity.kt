package com.example.wbinternw5part1.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wbinternw5part1.viewmodel.MainViewModel
import com.example.wbinternw5part1.R
import com.example.wbinternw5part1.databinding.ActivityMainBinding
import com.example.wbinternw5part1.model.AppState
import com.example.wbinternw5part1.model.DataModel
import com.example.wbinternw5part1.view.details.DetailsFragment
import com.example.wbinternw5part1.view.main.adapter.MainAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(dataModel: DataModel) {
                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.container, DetailsFragment.newInstance(dataModel))
                    .commit()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initView()
    }

    private fun initView() {
        binding.mainActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainActivityRecyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.subscribe().observe(this) { renderData(it) }
        viewModel.getData()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                Toast.makeText(this, appState.error.message, Toast.LENGTH_SHORT).show()
            }
            is AppState.Loading -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            is AppState.Success -> {
                appState.data?.let {
                    if (it.isEmpty())
                        Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
                    else
                        adapter.setData(it)
                }
            }
        }
    }
}