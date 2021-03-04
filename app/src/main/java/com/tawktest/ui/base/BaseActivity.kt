package com.tawktest.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tawktest.R
import com.tawktest.app.utils.NetworkUtils


abstract class BaseActivity : AppCompatActivity() {

    private lateinit var nDialog: AlertDialog
    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (::nDialog.isInitialized && isNetworkConnected()) {
                nDialog.getButton(Dialog.BUTTON_POSITIVE).callOnClick()
            }
        }
    }

    /**
     * Start an activity with data and finish (optional) current
     *
     * @param clazz
     * @param bundle
     * @param isFinish
     * */
    public final fun <T> navigateTo(
            clazz: Class<T>,
            bundle: Bundle? = null,
            isFinish: Boolean = false
    ) {
        val intent = Intent(this, clazz)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
        enterAnimation()
        if (isFinish) finish()
    }

    /**
     * Check if device is connected with internet
     *
     * @return true if connected otherwise false
     * */
    public final fun isNetworkConnected() = NetworkUtils.isConnected(this)

    override fun onBackPressed() {
        super.onBackPressed()
        exitAnimation()
    }

    private fun enterAnimation() {
        overridePendingTransition(R.anim.right_to_init, R.anim.init_to_left)
    }

    private fun exitAnimation() {
        overridePendingTransition(R.anim.left_to_init, R.anim.init_to_right)
    }

    /**
     * Show alert and get a callback of retry button
     *
     * @param callback
     * */
    public final fun networkAlert(callback: () -> Unit) {
        if (!::nDialog.isInitialized) {
            nDialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.ohh_no))
                    .setMessage(getString(R.string.enable_data_connection_message))
                    .setPositiveButton(getString(R.string.retry)) { di, _ ->
                        callback()
                        di.dismiss()
                    }.setNegativeButton(getString(R.string.cancel)) { di, _ ->
                        di.dismiss()
                    }.create()
        }
        nDialog.show()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
                networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        unregisterReceiver(networkChangeReceiver)
        super.onPause()
    }

}