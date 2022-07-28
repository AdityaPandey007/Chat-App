package com.example.communicate

class User {
    var uid : String? = null
    var name : String? = null
    var phoneNumb : String? =null
    var profileImage :String? = null
    constructor(){}

    constructor(uid : String? , naame : String? , phoneNumb : String? , profileImage : String?){

        this.uid = uid
        this.name = naame
        this.phoneNumb = phoneNumb
        this.profileImage = profileImage
    }
}