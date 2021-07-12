package com.sample.flickrimages

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.flickrimages.model.Photo
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class GalleryAdapter(private val mContext:Context,private val photoListInformation:ArrayList<Photo>,private val listClickListener: ListClickListener): RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>(),Filterable {

    private val photoListInformationExample = ArrayList<Photo>(photoListInformation)

    class GalleryViewHolder(private val rowView: View):RecyclerView.ViewHolder(rowView),View.OnClickListener {
        private val imageView: ImageView = rowView.findViewById(R.id.mImageViewCardViewItem)
        private val titleView: TextView = rowView.findViewById(R.id.mTextViewTitle)
        private lateinit var photoInformation:Photo
        private lateinit var listener: ListClickListener

        internal fun setListeners(listClickListener: ListClickListener){
            rowView.setOnClickListener(this)
            this.listener=listClickListener
        }

        internal fun bind(photo:Photo){
            this.photoInformation=photo
            titleView.text=photo.title
            val imageUrl = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_w.png"
            Picasso.get().load(imageUrl).into(imageView)
        }

        override fun onClick(p0: View?) {
            if(this::listener.isInitialized && this::photoInformation.isInitialized){
                val imageUrl = "https://live.staticflickr.com/${photoInformation.server}/${photoInformation.id}_${photoInformation.secret}_c.png"
                listener.listItemClick(imageUrl,photoInformation.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_view_item,parent,false)
        val viewHolder=GalleryViewHolder(view)
        viewHolder.setListeners(listClickListener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) = holder.bind(photoListInformation[position])

    override fun getItemCount(): Int {
        return photoListInformation.size
    }

    inner class ExampleFilter: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            Log.e("CostcoImage","CostcoImage filter $p0")
            val filteredList = ArrayList<Photo>()
            if(p0==null && p0?.length==0){
                filteredList.addAll(photoListInformationExample)
            }else{
                val filterPattern = p0?.toString()?.toLowerCase(Locale.getDefault())?.trim()
                for(photo in photoListInformationExample){
                    if(filterPattern?.let { photo.title.toLowerCase(Locale.getDefault()).contains(it) } == true){
                        filteredList.add(photo)
                    }
                }
            }
            val result = FilterResults()
            result.values=filteredList
            return result
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            photoListInformation.clear()
            photoListInformation.addAll(p1?.values as ArrayList<Photo>)
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return ExampleFilter()
    }


}