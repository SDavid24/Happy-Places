package com.example.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting a click event for Fab Button and calling the AddHappyPlaceActivity.
        fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlacesActivity::class.java)

            // Passing a request code while activity is launched.)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlaceLIstFromLocalDB()
    }


    /**
     * A function to get the list of happy place from local database.
     */
    private fun getHappyPlaceLIstFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList = dbHandler.getHappyPlaces()

       /** Calling a function which have created for getting list of inserted data from local database
         and passing the list to recyclerview to populate in UI.)
        */
        if (getHappyPlaceList.size > 0){

            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }else{
            rv_happy_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }


    //  Creating a function for setting up the recyclerview to UI.)
    /**
     * A function to populate the recyclerview to the UI.
     */
    private fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>){
        rv_happy_places_list.layoutManager =
            LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        rv_happy_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {

                //WHat happens after one clicks on the item view
                // TODO (Step 1: Pass the HappyPlaceDetails data model class to the detail activity.)
                val intent = Intent(this@MainActivity,
                    HappyPlaceDetailActivity::class.java)

                intent.putExtra(EXTRA_PLACE_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.

                startActivity(intent)
            }
        })


        // This binds the edit feature class to recyclerview
        val editSwipeHandler = object  : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // This below Calls the adapter function when it is swiped for edit
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                //val adapter = HappyPlacesAdapter(this@MainActivity, happyPlaceList)
                //rv_happy_places_list.adapter = adapter
                adapter.notifyEditItem(this@MainActivity,
                    viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)

        // This binds the delete feature class to recyclerview)
        val deleteSwipeHandler = object  : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // This below Calls the adapter function when it is swiped for delete
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceLIstFromLocalDB() // Gets the latest list from the local database after item being delete from it.
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)

    }


    /** It is called when the activity which launched with the request code and
    //  expecting a result from the launched activity.)
    // Call Back method  to get the Message from other Activity*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                getHappyPlaceLIstFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or back pressed" )
            }
        }
    }


    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"

    }

}
