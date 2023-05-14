package com.example.ecoassistant

class DataClass {
    var name: String? = null
    var der: String? = null
    var neder: String? = null
    var sagatavo: String? = null
    var parstrade: String? = null
    var image: String? = null

    constructor(name: String?, der: String?, neder: String?, sagatavo: String?, parstrade: String?, image: String?){
        this.name = name
        this.der = der
        this.neder = neder
        this.sagatavo = sagatavo
        this.parstrade = parstrade
        this.image = image
    }
    constructor()

}