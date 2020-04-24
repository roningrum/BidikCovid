package id.go.dkksemarang.bidikcovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.go.dkksemarang.bidikcovid.R

/**
 * A simple [Fragment] subclass.
 */
class SearchPasienFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_search_pasien, container, false)
    }

}
