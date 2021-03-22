package com.tawktest.data.remote.api

import com.tawktest.data.models.User
import com.tawktest.data.models.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

interface Apis {

    @GET(Endpoints.USERS)
    fun getUsers(@QueryMap queries: Map<String, String>): Call<UserResponse>

    @GET(Endpoints.USER_DETAILS)
    fun getUserDetails(@Path("username") username: String): Call<User>
}