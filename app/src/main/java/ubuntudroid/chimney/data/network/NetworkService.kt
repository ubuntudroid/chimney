package ubuntudroid.chimney.data.network

import android.content.Context
import android.net.ConnectivityManager

class NetworkService(private val appContext: Context) {

    private val connectivityManager by lazy {
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isConnectedNow(): Boolean =
            connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}