package com.tawktest.app.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)