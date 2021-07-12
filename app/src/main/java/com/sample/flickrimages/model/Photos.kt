package com.sample.flickrimages.model

import com.sample.flickrimages.model.Photo

data class Photos(val page:Int, val pages:Int, val perpage:Int, val total:Int, val photo:List<Photo>)
