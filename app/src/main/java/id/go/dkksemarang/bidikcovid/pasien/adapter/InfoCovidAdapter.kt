package id.go.dkksemarang.bidikcovid.pasien.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import kotlinx.android.synthetic.main.item_info_covid_pasien.view.*

class InfoCovidAdapter(val infoCovidList: List<InfoCovid>, var context: Context?) : RecyclerView.Adapter<InfoCovidAdapter.InfoCovidViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = InfoCovidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_info_covid_pasien, parent, false))

    override fun getItemCount(): Int=
        infoCovidList.size

    override fun onBindViewHolder(holder: InfoCovidAdapter.InfoCovidViewHolder, position: Int) {
        holder.bindCovidPasie(infoCovidList[position])
    }

    inner class InfoCovidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindCovidPasie(infoCovid: InfoCovid){
            itemView.tv_nama_pasien.text = infoCovid.nama
            itemView.tv_alamat_pasien.text = infoCovid.alamat
            itemView.tv_status_pasien.text = infoCovid.status
            val lat = infoCovid.lat.toString()
            val lng = infoCovid.lng.toString()
            itemView.tv_koordinat_pasien.text = "$lat, $lng"
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(infoCovidList[adapterPosition])
            }
        }
    }
}

interface OnItemClickCallback{
    fun onItemClicked(infoCovid: InfoCovid)
}