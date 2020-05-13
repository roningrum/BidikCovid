package id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.go.dkksemarang.bidikcovid.R

/**
 * A simple [Fragment] subclass.
 */
class PasienSudahSurveiLokasi : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pasien_sudah_survei_lokasi, container, false)
    }

}
