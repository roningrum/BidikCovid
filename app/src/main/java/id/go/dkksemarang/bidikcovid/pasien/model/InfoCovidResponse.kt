package id.go.dkksemarang.bidikcovid.pasien.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InfoCovid(
    @SerializedName("pasien_id")
    var pasien_id: String? = null,
    @SerializedName("nama")
    var nama: String? = null,
    @SerializedName("alamat")
    var alamat: String? = null,
    @SerializedName("umur")
    var umur: String? = null,
    @SerializedName("jk")
    var jk: String? = null,
    @SerializedName("telp")
    var telp: String? = null,
    @SerializedName("flag")
    var flag: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("alasan")
    var alasan: String? = null,
    @SerializedName("latitude")
    var lat: Double?=null,
    @SerializedName("longitude")
    var lng: Double?=null
):Parcelable

data class InfoCovidResponse(
    @SerializedName("message")
    var message: String?=null,
    @SerializedName("status")
    var status: Boolean=false,
    @SerializedName("infocovid")
    var infocovid: List<InfoCovid>
)