package id.go.dkksemarang.bidikcovid.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("token")
    var token: String
)

