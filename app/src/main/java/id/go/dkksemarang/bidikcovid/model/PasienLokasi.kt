package id.go.dkksemarang.bidikcovid.model

import com.google.gson.annotations.SerializedName

class PasienLokasi {
    @SerializedName("id_pasien")
    var id_pasien: String? = null

    @SerializedName("latitude")
    var latitude: Double? = null

    @SerializedName("longitude")
    var longitude: Double? = null
}