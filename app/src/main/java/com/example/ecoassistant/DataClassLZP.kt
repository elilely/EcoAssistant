package com.example.ecoassistant

class DataClassLZP {
    var name: String? = null
    var drikst: String? = null
    var nedrikst: String? = null
    var konteiners: String? = null
    var noderigi: String? = null
    var maksa: String? = null

    constructor(name: String?, drikst: String?, nedrikst: String?, konteiners: String?, noderigi: String?, maksa: String?){
        this.name = name
        this.drikst = drikst
        this.nedrikst = nedrikst
        this.konteiners = konteiners
        this.noderigi = noderigi
        this.maksa = maksa
    }
    constructor()
}