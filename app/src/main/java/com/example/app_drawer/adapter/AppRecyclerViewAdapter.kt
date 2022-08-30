package com.example.app_drawer.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.R
import com.example.app_drawer.data_set.AppInfo


class AppRecyclerViewAdapter(
    private val dataSet: MutableList<AppInfo>
) :
    RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val iconImageView: ImageView

        //        val logoImageView: ImageView
        val labelTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            iconImageView = view.findViewById(R.id.icon_image_view)
//            logoImageView = view.findViewById(R.id.logo_image_view)
            labelTextView = view.findViewById(R.id.label_text_view)
        }

        override fun onClick(view: View) {
            Log.d("dsfdsfd", "onClickonClickonClickonClick")
            val intent = Intent(view.context, AppRecyclerViewAdapter::class.java)
            view.getContext().startActivity(intent)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.component_app_info, viewGroup, false)

        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.iconImageView.setImageDrawable(dataSet[position].iconDrawable)

//        viewHolder.iconImageView.setOnClickListener {
//
//
//            onClickViewHolder(dataSet[position].packageName)
//        }
//        viewHolder.logoImageView.setImageDrawable(dataSet[position].logoDrawable)
        viewHolder.labelTextView.text = dataSet[position].label

        Log.d("AppRecyclerViewAdapter", "${dataSet[position].packageName}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

//    private fun onClickViewHolder(packageName: String?) {
//        Log.d("onClickViewHolder", "$packageName on click!!!")
//
//
//        val pm: PackageManager = packageManager
//
//        val intent = pm.getLaunchIntentForPackage("$packageName")
//
//        if (intent != null) {
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }
//
//    }

}
