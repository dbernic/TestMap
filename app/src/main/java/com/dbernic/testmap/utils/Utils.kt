package com.dbernic.testmap.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng

object Utils {
    public fun getRndPoints(nwLatLng: LatLng, seLatLng: LatLng): Pair<LatLng, LatLng> {
        val multiplier = 1000000
        val latDiff = (nwLatLng.latitude - seLatLng.latitude) * multiplier
        val lngDiff = (seLatLng.longitude - nwLatLng.longitude) * multiplier

        val latStart = (0..latDiff.toInt()).random().toDouble() / multiplier
        val latEnd = (0..latDiff.toInt()).random().toDouble() / multiplier
        val lngStart = (0..lngDiff.toInt()).random().toDouble() / multiplier
        val lngEnd = (0..lngDiff.toInt()).random().toDouble() / multiplier

        Log.w("Calcs", "Results: $latStart, $latEnd, $lngStart, $lngEnd")

        val pointStart = LatLng(seLatLng.latitude + latStart, nwLatLng.longitude+lngStart)
        val pointEnd = LatLng(seLatLng.latitude + latEnd, nwLatLng.longitude+lngEnd)

        return Pair(pointStart, pointEnd)

    }

}