package com.example.app_drawer.adapter

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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImageView: ImageView
//        val logoImageView: ImageView
        val packageNameTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            iconImageView = view.findViewById(R.id.icon_image_view)
//            logoImageView = view.findViewById(R.id.logo_image_view)
            packageNameTextView = view.findViewById(R.id.package_name_text_view)
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
//        viewHolder.logoImageView.setImageDrawable(dataSet[position].logoDrawable)
        viewHolder.packageNameTextView.text = dataSet[position].packageName

        Log.d("AppRecyclerViewAdapter", "${dataSet[position].packageName}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
