package com.dbernic.testmap

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.dbernic.testmap.network.*
import com.dbernic.testmap.utils.Constants
import com.dbernic.testmap.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    val nwLatLng = LatLng(47.078284, 28.721764)
    val seLatLng = LatLng(46.975178, 28.915116)

    val refreshRouteMs = 60000L
    val refreshProgressMs = 1000L
    val progressRate = Math.round( refreshProgressMs.toDouble()/refreshRouteMs.toDouble()*100 ).toInt()

    private lateinit var gMap: GoogleMap
    private var line: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (mainMap as SupportMapFragment).getMapAsync(this)
    }

    private fun launchCounter(){
        object: CountDownTimer(refreshRouteMs, refreshProgressMs) {
            override fun onFinish() {
                mainProgress.progress = 100
                showRoute()
            }

            override fun onTick(ms: Long) {
                mainProgress.progress = mainProgress.progress + progressRate

            }
        }.start()
    }

    private fun showRoute(){
        mainProgress.progress = 0
        val (start, end) = Utils.getRndPoints(nwLatLng, seLatLng)
        boundMap(start, end)

        val request = Request(
            elevation = false, points_encoded = false, vehicle = "car",
            points = arrayListOf(arrayOf(start.longitude, start.latitude), arrayOf(end.longitude, end.latitude))
        )

        RestClient.get().getRoute(Constants.ROUTER_API_KEY, request).enqueue(object: BaseCallback<Result>(this){
            override fun response(r: Result) {
                drawPath(r.paths[0].points)
                launchCounter()
            }
        })
    }

    private fun drawPath(points: Points) {
        line?.let {
            it.remove()
        }

        val lineOptions = PolylineOptions()
        for (item in points.coordinates) {
            lineOptions.add(LatLng(item[1], item[0]))
        }

        line = gMap.addPolyline(lineOptions)

    }

    private fun boundMap(start: LatLng, end: LatLng){

        val builder = LatLngBounds.Builder()

        builder.include(start)
        builder.include(end)

        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 100)
        gMap.moveCamera(cu)
        gMap.setOnCameraChangeListener {
            val maxZoom = 16.0f
            if (it.zoom>maxZoom) gMap.moveCamera(CameraUpdateFactory.zoomTo(maxZoom))
        }

    }

    override fun onMapReady(map: GoogleMap?) {
        map!!.setOnMapLoadedCallback {
            gMap = map!!
            showRoute()
        }
    }
}