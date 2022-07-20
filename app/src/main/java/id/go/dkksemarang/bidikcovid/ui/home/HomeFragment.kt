package id.go.dkksemarang.bidikcovid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.ui.home.pageradapater.MyPagerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = MyPagerAdapter(childFragmentManager)
        viewpager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewpager)
    }
}
