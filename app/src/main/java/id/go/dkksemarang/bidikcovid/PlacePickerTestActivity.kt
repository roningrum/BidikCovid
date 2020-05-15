package id.go.dkksemarang.bidikcovid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_place_picker_test.*


class PlacePickerTestActivity : AppCompatActivity() {

    // konstanta untuk mendeteksi hasil balikan dari place picker
    private val PLACE_PICKER_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_picker_test)

        bt_ppicker.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            try {
                startActivityForResult(
                    builder.build(this@PlacePickerTestActivity),
                    PLACE_PICKER_REQUEST
                )
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace();
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace();
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this)
                val toasMsg = String.format(
                    "Place: %s \n" +
                            "Alamat: %s \n" +
                            "Latlng %s \n",
                    place.name,
                    place.address,
                    place.latLng.latitude,
                    place.latLng.longitude
                )
                tv_place_id.text = toasMsg
            }
        }
    }
}
