package com.sample.flickrimages.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.sample.flickrimages.model.GalleryDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WinterGalleryViewModel: ViewModel() {
    val photosLiveData:MutableLiveData<GalleryDetails> = MutableLiveData()


    fun getImagesForWinterGallery(apiKey:String, galleryId:String){
        val status = photosLiveData.value?.infoStatus
        if(TextUtils.isEmpty(status) && status!="Success"){
            Log.d("CostcoImage","CostcoImage getImagesForWinterGallery if block current status $status")
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    try{
                        val galleryURL ="https://www.flickr.com/services/rest/?method=flickr.galleries.getPhotos&api_key=${apiKey}&gallery_id=${galleryId}&format=json&nojsoncallback=1"
                        val urlConnection: HttpURLConnection = URL(galleryURL).openConnection() as HttpURLConnection
                        urlConnection.requestMethod = "GET"
                        urlConnection.connectTimeout=20000
                        if(urlConnection.responseCode ==HttpURLConnection.HTTP_OK){
                            val galleryStream: InputStream = BufferedInputStream(urlConnection.inputStream)
                            val galleryStreamReader= InputStreamReader(galleryStream)
                            val jsonReader = JsonReader(galleryStreamReader)
                            val gson = Gson()
                            val galleryDetails = gson.fromJson(jsonReader,GalleryDetails::class.java) as GalleryDetails
                            galleryDetails.infoStatus="Success"
                            galleryDetails.errorCode=HttpURLConnection.HTTP_OK
                            withContext(Dispatchers.Main){
                                photosLiveData.postValue(galleryDetails)
                            }
                            Log.d("CostcoImage","CostcoImage API Success and posted response")
                        }else{
                            withContext(Dispatchers.Main){
                                photosLiveData.postValue(GalleryDetails(null,"ok","Failure",urlConnection.responseCode))
                            }
                            Log.d("CostcoImage","CostcoImage API Failed and posted response")
                        }
                    }catch(e:Exception){
                        e.printStackTrace()
                        Log.d("CostcoImage","CostcoImage API exception and posted response")
                        val galleryImageResponse: GalleryDetails = GalleryDetails(null,"ok","Failure",503)
                        withContext(Dispatchers.Main){
                            photosLiveData.postValue(galleryImageResponse)
                        }
                    }
                }
            }

        }else{
            Log.d("CostcoImage","CostcoImage getImagesForWinterGallery else block current status $status")
        }
    }




}