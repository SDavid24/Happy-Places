package com.example.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.activities.AddHappyPlacesActivity
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_happy_places.view.*
import kotlinx.android.synthetic.main.item_happy_place.view.*
import java.nio.file.Files.list

class HappyPlacesAdapter (
    private val context: Context,
    private val list: ArrayList<HappyPlaceModel>
    ): RecyclerView.Adapter <RecyclerView.ViewHolder>(){

    // Adding a variable for onClickListener interface
    private var onClickListener: OnClickListener? = null

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(context).
            inflate((R.layout.item_happy_place), parent, false)

            return MyViewHolder(itemView)
        }

    /**
      Binds each item in the ArrayList to a view Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an item.
      This new ViewHolder should be constructed with a new View that can represent the items of the given type. You can either create a new View manually or inflate it from an XML layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.itemView.iv_place_image.setImageURI(
                Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
               // Finally, add an onclickListener to the item
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    // A function to delete the happy place details which is inserted earlier from the local storage.
    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if (isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /** This Creates a function to edit the happy place details which is inserted earlier and pass the
     details through intent.
     */
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyPlacesActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)

        notifyItemChanged(position) //This updates the changes immediately and not until you open and close the app
    }

    override fun getItemCount() = list.size

    /**
     * A function to bind the onclickListener.
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    //Create an interface for onclickListener
    interface OnClickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }

    /**A ViewHolder describes an item view and metadata about its place within the RecyclerView.*/
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    }