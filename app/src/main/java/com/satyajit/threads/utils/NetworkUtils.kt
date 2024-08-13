package com.satyajit.threads.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.satyajit.threads.connectivity.ConnectionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

val Context.connectivityManager get(): ConnectivityManager{
    return getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

//Current state of internet connection
val ConnectivityManager.currentConnectivityState: ConnectionState
    get() {
    val connected = allNetworks.any{ network ->
        getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false
    }
    return if(connected) ConnectionState.Available else ConnectionState.Unavailable
}

//Utility to check availability/unavailability of internet connection
//here I have used callback flow which basically converts the callbacks-APIs into flow
fun ConnectivityManager.observeConnectivityAsFlow() = callbackFlow{
    trySend(currentConnectivityState)
    val callback = NetworkCallback { connectionState ->
        trySend(connectionState)
    }
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    registerNetworkCallback(networkRequest, callback)
    //clean up resources associated with callback
    awaitClose {
        unregisterNetworkCallback(callback)
    }
}.distinctUntilChanged()

fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback{
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }
        override fun onUnavailable() {
            callback(ConnectionState.Unavailable)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}

@Composable
fun currentConnectionState(): ConnectionState {
    val connectivityManager = LocalContext.current.connectivityManager
    return remember { connectivityManager.currentConnectivityState }
}

@Composable
fun connectivityState(): State<ConnectionState> {
    val connectivityManager = LocalContext.current.connectivityManager
    return produceState(initialValue = connectivityManager.currentConnectivityState) {
        connectivityManager.observeConnectivityAsFlow().collect { value = it }
    }
}