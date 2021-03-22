package com.tawktest.ui.views.activities.user_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawktest.app.utils.BackoffCallback
import com.tawktest.data.Resource
import com.tawktest.data.local.Database
import com.tawktest.data.models.User
import com.tawktest.data.models.UserResponse
import com.tawktest.data.remote.api.Apis
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ListViewModel @Inject constructor(private val apis: Apis, private val database: Database) :
        ViewModel() {
    private val usersLive = MutableLiveData<Resource<Any>>()

    /**
     * Fetching all user list from local database
     * */
    fun fetchLocalUsers() {
        Thread {
            usersLive.postValue(Resource.success(database.getUserDao().getUsers()))
        }.start()
    }

    /**
     * Fetching user list from server
     *
     * @param id last loaded user id
     * @param pageCapacity how much data should be loaded according to page size
     * */
    fun fetchUsers(id: Int, pageCapacity: Int) {
        usersLive.value = Resource.loading(usersLive.value?.data)
        val queries = mutableMapOf<String, String>()
        queries["since"] = id.toString()
        queries["per_page"] = pageCapacity.toString()
        apis.getUsers(queries).enqueue(object : BackoffCallback<UserResponse>() {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body() as UserResponse
                    if (id == 0) {
                        usersLive.value = Resource.success(users)
                    } else {
                        usersLive.value?.let {
                            it.data?.let { data ->
                                val mUsers = (data as List<User>).toMutableList()
                                mUsers.addAll(users)
                                usersLive.value = Resource.success(mUsers)
                            }
                        }
                    }
                    Thread {
                        database.getUserDao().insertAll(users)
                    }.start()
                }
            }

            override fun onFailedAfterRetry(t: Throwable?) {
                usersLive.value = Resource.error(data = t, message = "Something went wrong!")
            }
        })
    }

    /**
     * Exposing live users resource to its observers
     *
     * @return users live data
     * */
    fun getUsers(): LiveData<Resource<Any>> = usersLive

}