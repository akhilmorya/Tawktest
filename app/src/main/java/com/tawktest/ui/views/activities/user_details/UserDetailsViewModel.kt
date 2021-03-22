package com.tawktest.ui.views.activities.user_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawktest.app.utils.BackoffCallback
import com.tawktest.data.Resource
import com.tawktest.data.local.Database
import com.tawktest.data.models.User
import com.tawktest.data.remote.api.Apis
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

class UserDetailsViewModel @Inject constructor(
        private val apis: Apis,
        private val database: Database
) : ViewModel() {
    private val userLive = MutableLiveData<Resource<Any>>()

    /**
     * Fetching user from local database by username
     *
     * @param username
     * */
    fun fetchLocalUser(username: String) {
        Thread {
            userLive.postValue(Resource.success(database.getUserDao().getUser(username)))
        }.start()
    }

    /**
     * Fetching user from server
     * We are using user object as parameter to save previous notes
     * Otherwise we can use only username as parameter
     *
     * @param userParam
     * */
    fun fetchUser(userParam: User) {
        apis.getUserDetails(userParam.login).enqueue(object : BackoffCallback<User>() {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        Thread {
                            it.notes = userParam.notes
                            database.getUserDao().update(it)
                            fetchLocalUser(it.login)
                        }.start()
                    }
                }
            }

            override fun onFailedAfterRetry(t: Throwable?) {
                userLive.value = Resource.error(data = t, message = "Something went wrong!")
            }
        })
    }

    /**
     * Update user to local database
     *
     * @param user with updated value
     * */
    fun updateUser(user: User) {
        Thread {
            database.getUserDao().update(user)
            fetchLocalUser(user.login)
        }.start()
    }

    /**
     * Exposing live user resource to its observers
     *
     * @return user live data
     * */
    fun getUser(): LiveData<Resource<Any>> = userLive
}