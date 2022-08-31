package com.example.app_drawer.recycler_view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ComponentAppInfoBinding
import com.example.app_drawer.vo.AppInfoVo


class AppRecyclerViewAdapter(
    private val dataSet: MutableList<AppInfoVo>
) :
    RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "AppRecyclerViewAdapter"
    private lateinit var componentAppInfoBinding: ComponentAppInfoBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val TAG = "AppRecyclerViewAdapter"
        val iconImageView: ImageView
        val labelTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            iconImageView = view.findViewById(R.id.icon_image_view)
            iconImageView.setOnClickListener {
                Log.d(TAG, "setOnClickListener")
            }
            labelTextView = view.findViewById(R.id.label_text_view)
        }


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        componentAppInfoBinding =
            ComponentAppInfoBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ViewHolder(componentAppInfoBinding.root)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.iconImageView.setImageDrawable(dataSet[position].iconDrawable)
        viewHolder.iconImageView.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: onClick!!!!")
            viewHolder.itemView.context.startActivity(dataSet[position].execIntent)
        }
        viewHolder.labelTextView.text = dataSet[position].label

        Log.d("AppRecyclerViewAdapter", "${dataSet[position].packageName}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
