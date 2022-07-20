package id.go.dkksemarang.bidikcovid.model

import com.google.gson.annotations.SerializedName

//kelas info covid response
class InfoCovidResponse {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: Boolean = false

    @SerializedName("infocovid")
    var infocovid: List<InfoCovid>? = null
}
