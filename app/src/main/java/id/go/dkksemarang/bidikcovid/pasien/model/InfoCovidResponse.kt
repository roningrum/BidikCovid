package id.go.dkksemarang.bidikcovid.pasien.model

import com.google.gson.annotations.SerializedName

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
)

data class InfoCovidResponse(
    @SerializedName("infocovid")
    var infocovid: List<InfoCovid>
)