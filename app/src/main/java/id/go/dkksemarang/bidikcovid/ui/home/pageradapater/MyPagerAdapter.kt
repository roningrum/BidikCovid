package id.go.dkksemarang.bidikcovid.ui.home.pageradapater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien.PasienBelumSurveiLokasi
import id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien.PasienSudahSurveiLokasi

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val pages = listOf(PasienBelumSurveiLokasi(), PasienSudahSurveiLokasi())
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Belum Survei"
            else -> "Sudah Survei"
        }
    }

}