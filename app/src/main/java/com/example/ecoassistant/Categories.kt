package com.example.ecoassistant

import android.os.Parcel
import android.os.Parcelable

data class Categories( var materialName: String):Parcelable {//, val imageMaterial: Int, val toDoRules:String
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        //parcel.readInt(),
        //parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(materialName)
        //parcel.writeInt(imageMaterial)
        //parcel.writeString(toDoRules)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Categories> {
        override fun createFromParcel(parcel: Parcel): Categories {
            return Categories(parcel)
        }

        override fun newArray(size: Int): Array<Categories?> {
            return arrayOfNulls(size)
        }
    }
}

