package com.chetana.firebaseauthdemo

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var user:String="",
    var email:String?="",
    var contact:String?="",
    var password:String?=""
)/*{
    constructor() : this("", "", "", "", 0)
}*/