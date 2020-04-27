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
    var alasan: String? = null
):Parcelable

data class pasienLokasi(
    @SerializedName("lat")
    var latitude: Double? =null,
    @SerializedName("lng")
    var longitude: Double? =null
)

data class PasienLokasiResponse(
    @SerializedName("infocovid")
    var pasienLokasi: List<pasienLokasi>
)

data class InfoCovidResponse(
    @SerializedName("infocovid")
    var infocovid: List<InfoCovid>
)