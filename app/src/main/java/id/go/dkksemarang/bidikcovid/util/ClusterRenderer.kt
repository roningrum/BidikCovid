package id.go.dkksemarang.bidikcovid.util

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.model.InfoCovid


class ClusterRenderer(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<InfoCovid>?
) :
    DefaultClusterRenderer<InfoCovid>(
        context,
        map,
        clusterManager
    ) {

    companion object {
        // 1
        private const val MARKER_DIMENSION = 48 // 2
    }

    private var iconGenerator: IconGenerator = IconGenerator(context)
    private var markerImageView: ImageView = ImageView(context)

    override fun onBeforeClusterItemRendered(item: InfoCovid, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerImageView.setImageResource(R.drawable.ic_lokasi_pasien) // 6
        val icon = iconGenerator.makeIcon() // 7
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)) // 8
        markerOptions.title(item.title)
    }


    init {
        // 3
        markerImageView.layoutParams = ViewGroup.LayoutParams(
            MARKER_DIMENSION,
            MARKER_DIMENSION
        )
        iconGenerator.setContentView(markerImageView) // 4
    }
}