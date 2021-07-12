package com.sample.flickrimages.activites

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sample.flickrimages.FlickrApplication
import com.sample.flickrimages.GalleryAdapter
import com.sample.flickrimages.ListClickListener
import com.sample.flickrimages.R
import com.sample.flickrimages.model.Photo
import com.sample.flickrimages.viewmodel.WinterGalleryViewModel

class GalleryImageScreen : AppCompatActivity(),ListClickListener {
    private lateinit var searchText: EditText
    private lateinit var winterGalleryViewModel: WinterGalleryViewModel
    private lateinit var reCyclerView: RecyclerView
    private lateinit var galleryLoader:ProgressBar
    private lateinit var galleryAdapter: GalleryAdapter
    companion object {
        const val FULLSCREEN_TITLE: String="Fullscreen_Title"
        const val FULLSCREEN_IMAGE_URL: String="FullScreen_ImageURL"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_image_screen)
        searchText = findViewById(R.id.search_gallery)
        reCyclerView = findViewById(R.id.gallery_list_view)
        galleryLoader = findViewById(R.id.image_loader)
        winterGalleryViewModel = ViewModelProvider(this).get(WinterGalleryViewModel::class.java)
        winterGalleryViewModel.photosLiveData.observe(this, { galleryInformation ->
            galleryLoader.visibility=View.GONE
            if (galleryInformation.errorCode != 200) {
                val snackBar = Snackbar.make(reCyclerView, "Server responded following error ${galleryInformation.errorCode}", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction("OK") { snackBar.dismiss() }
                snackBar.show()
            } else {
                galleryInformation.photos?.let { photos ->
                    val photoList = photos.photo
                    if (photoList.isNotEmpty()) {
                        searchText.inputType=InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
                        val layoutManager = LinearLayoutManager(this)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        reCyclerView.layoutManager = layoutManager
                        galleryAdapter = GalleryAdapter(this, photoList as ArrayList<Photo>, this)
                        reCyclerView.adapter = galleryAdapter
                    } else {
                        val snackBar = Snackbar.make(reCyclerView, "Server responded with zero entries", Snackbar.LENGTH_INDEFINITE)
                        snackBar.setAction("OK") { this.finish() }
                        snackBar.show()
                    }
                }

            }
        })
        if(FlickrApplication.getInstance().isNetworkAvailable(this)){
            galleryLoader.visibility=View.VISIBLE
            winterGalleryViewModel.getImagesForWinterGallery("f7819658263ca0723615c079c213cfa7", "72157717887648561")
        }else{
            val snackBar = Snackbar.make(reCyclerView, "No Internet Connection.", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("OK") { snackBar.dismiss() }
            snackBar.show()
        }

        searchText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                galleryAdapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }

    override fun listItemClick(url: String, title: String) {
        if(FlickrApplication.getInstance().isNetworkAvailable(this)){
            Log.d("CostcoImage", "CostcoImage URL=$url, title=$title")
            val fullImageScreenIntent = Intent(this, FullImageScreen::class.java)
            fullImageScreenIntent.putExtra(FULLSCREEN_IMAGE_URL, url)
            fullImageScreenIntent.putExtra(FULLSCREEN_TITLE, title)
            startActivity(fullImageScreenIntent)
        }else{
            val snackBar = Snackbar.make(reCyclerView, "No Internet Connection.", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("OK") { snackBar.dismiss() }
            snackBar.show()
        }

    }


}