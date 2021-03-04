package com.tawktest.app.utils

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager


class KeyboardUtils {
    companion object {
        /**
         * To hide soft input mode
         *
         * @param context
         * @param windowToken
         * */
        fun hideKeyboard(context: Context?, windowToken: IBinder) {
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

        /**
         * To hide soft input mode
         *
         * @param context
         * @param view
         * */
        fun hideKeyboard(context: Context?, view: View?) {
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        /**
         * To show soft input mode forcefully
         *
         * @param context
         * @param view
         * */
        fun showKeyboard(context: Context?, view: View?) {
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInputFromWindow(view?.applicationWindowToken, InputMethodManager.SHOW_FORCED, 0)
        }
    }
}