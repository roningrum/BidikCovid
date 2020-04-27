package id.go.dkksemarang.bidikcovid.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import id.go.dkksemarang.bidikcovid.R

class SearchPasienActivity : AppCompatActivity() {
    companion object{
        var SEARCH_QUERY = "search"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_pasien)

        val searchListFragment= SearchPasienFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.contain_searh_pasien, searchListFragment)
        fragmentTransition.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.onActionViewExpanded()
        searchView.isFocusableInTouchMode = true
        searchView.isIconified = false
        searchView.queryHint = "Cari nama pasien"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                val data = Bundle()
                data.putString(SEARCH_QUERY, query)
                val fragment = SearchPasienFragment()
                fragment.arguments = data
                supportFragmentManager.beginTransaction().replace(R.id.contain_searh_pasien, fragment).commit()
                Log.d("Query", "Key$SEARCH_QUERY query Key $query")

                if(query == ""){
                    searchView.clearFocus()
                }
                hideKeyBoard(searchView)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("Query", "$newText")
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun hideKeyBoard(searchView: SearchView) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }
}
