package id.go.dkksemarang.bidikcovid.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import id.go.dkksemarang.bidikcovid.R

class SearchPasienActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_pasien)

        val searchListFragment= SearchPasienFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.mainFragment, searchListFragment)
        fragmentTransition.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.onActionViewExpanded()
        searchView.isFocusableInTouchMode = true
        searchView.isIconified = false
        searchView.queryHint = "Cari nama pasien"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}
