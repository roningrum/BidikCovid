package id.go.dkksemarang.bidikcovid.pasien.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import kotlinx.android.synthetic.main.item_info_covid_pasien.view.*

class InfoCovidFilterAdapter(private val infoCovidList: ArrayList<InfoCovid>) : RecyclerView.Adapter<InfoCovidFilterAdapter.InfoCovidViewHolder>(), Filterable{

    private var infoCovidFilterList : ArrayList<InfoCovid>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = InfoCovidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_info_covid_pasien, parent, false))

    override fun getItemCount(): Int =
        if(infoCovidFilterList==null) 0 else infoCovidFilterList!!.size

    override fun onBindViewHolder(holder: InfoCovidFilterAdapter.InfoCovidViewHolder, position: Int) {
        holder.bindCovidPasie(infoCovidFilterList!![position])
    }

    inner class InfoCovidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindCovidPasie(infoCovid: InfoCovid){
            itemView.tv_nama_pasien.text = infoCovid.nama
            itemView.tv_alamat_pasien.text = infoCovid.alamat
            itemView.tv_status_pasien.text = infoCovid.status
        }
    }

    override fun getFilter(): Filter {
        return object  : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                infoCovidFilterList = if(charSearch.isEmpty()){
                    infoCovidList
                } else{
                    val resultList = ArrayList<InfoCovid>()
                    for(row in infoCovidList){
                        if(row.nama!!.contains(charSearch)|| row.flag!!.contains(charSearch) || row.alamat!!.contains(charSearch)){
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = infoCovidFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                infoCovidFilterList = results!!.values as ArrayList<InfoCovid>
                notifyDataSetChanged()
            }

        }
    }

    init {
        infoCovidFilterList = infoCovidList
    }
}