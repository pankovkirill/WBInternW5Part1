package com.example.wbinternw5part1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wbinternw5part1.model.AppState
import com.example.wbinternw5part1.model.DataModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<AppState>()

    private val liveDataForViewToObserve: LiveData<AppState> = _data

    fun subscribe() = liveDataForViewToObserve

    fun getData() {
        _data.postValue(AppState.Loading)
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BASE_URL)
            .build()

        client
            .newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body.string()
                        val moshi = Moshi
                            .Builder()
                            .build()
                        val listType =
                            Types.newParameterizedType(List::class.java, DataModel::class.java)
                        val adapter: JsonAdapter<List<DataModel>> = moshi.adapter(listType)
                        _data.postValue(AppState.Success(adapter.fromJson(responseData)))
                    } else
                        _data.postValue(AppState.Error(error("Connection failed")))
                }

                override fun onFailure(call: Call, e: IOException) {
                    _data.postValue(AppState.Error(e))
                }
            })
    }

    companion object {
        private const val BASE_URL = "https://api.opendota.com/api/heroStats"
    }
}