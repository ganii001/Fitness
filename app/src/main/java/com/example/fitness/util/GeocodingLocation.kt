package com.example.fitness.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.IOException
import java.util.*

object GeocodingLocation {
    private const val TAG = "GeocodingLocation"

    fun getAddressFromLocation(
        locationAddress: String,
        context: Context?, handler: Handler?
    ) {
        val thread: Thread = object : Thread() {
            override fun run() {
                val geocoder = Geocoder(context, Locale.getDefault())
                var result: String? = null
                try {
                    val addressList: List<*>? =
                        geocoder.getFromLocationName(locationAddress, 1)
                    if (addressList != null && addressList.size > 0) {
                        val address =
                            addressList[0] as Address
                        val sb = StringBuilder()
                        sb.append(address.latitude).append("\n")
                        sb.append(address.longitude).append("\n")
                        result = sb.toString()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Unable to connect to Geocoder", e)
                } finally {
                    val message = Message.obtain()
                    message.target = handler
                    if (result != null) {
                        message.what = 1
                        val bundle = Bundle()
                        result = """
                            Address: $locationAddress
                            
                            Latitude and Longitude :
                            $result
                            """.trimIndent()
                        bundle.putString("address", result)
                        message.data = bundle
                    } else {
                        message.what = 1
                        val bundle = Bundle()
                        result = """Address: $locationAddress
 Unable to get Latitude and Longitude for this address location."""
                        bundle.putString("address", result)
                        message.data = bundle
                    }
                    message.sendToTarget()
                }
            }
        }
        thread.start()
    }

}