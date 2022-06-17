package com.example.happyplaces.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.example.happyplaces.models.HappyPlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*


/**Map activity for viewing the location on map using the google map feature.*/

class MapActivity : AppCompatActivity(), OnMapReadyCallback { //extend onMapReadyCallBack so as to signify the context we are operating in and the go ahead to implement it member

    private var mHappyPlaceDetails: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        /**Conditional to check if we have extra information on the
         * intent that sent us to this activity*/
        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            //if true, we assigning the extra details in a happyPlaceModel format into the variable created up there
            mHappyPlaceDetails = intent.getSerializableExtra(
                MainActivity.EXTRA_PLACE_DETAILS)
                    as HappyPlaceModel
        }

        /**Customizing the tool bar*/
        if (mHappyPlaceDetails != null){
            setSupportActionBar(toolbar_map)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mHappyPlaceDetails!!.title

            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment : SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map)
                        as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }

        //override fun onMapReady()

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /**In-built function(which is the member of OnMapReadyCallBack) on when the map loads.
     * It is used for configuring the map  */
    override fun onMapReady(googleMap: GoogleMap) {

        //variable to know the exact location on the map
        val exactPosition = LatLng(mHappyPlaceDetails!!.latitude, mHappyPlaceDetails!!.longitude)

        //on setting the marker in the position on the map
        googleMap.addMarker(MarkerOptions().position(exactPosition)
            .title(mHappyPlaceDetails!!.location))

        //variable to zoom in on the exact location immediately the map loads
        val latLngZoom = CameraUpdateFactory.newLatLngZoom(exactPosition, 15f)

        //Animating the googleMap to go the said location
        googleMap.animateCamera(latLngZoom)
    }
}