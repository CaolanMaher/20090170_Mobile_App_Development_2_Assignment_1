package ie.wit.a20090170_mobile_app_2_assignment_1.ui.map

import android.content.Intent
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentMapsBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import timber.log.Timber.Forest.i

class MapsFragment : Fragment(), OnMarkerDragListener {

    private val args by navArgs<MapsFragmentArgs>()

    private var _fragBinding: FragmentMapsBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var mapsViewModel: MapsViewModel

    private lateinit var quest : QuestModel

    private var latitude = 0.0
    private var longitude = 0.0

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        //val sydney = LatLng(-34.0, 151.0)

        var location = LatLng(52.245696, -7.139102)

        if(args.questID >= 0) {
            quest = QuestManager.findById(args.questID)!!
            location = LatLng(quest.latitude, quest.longitude)
        }

        val marker = googleMap.addMarker(MarkerOptions().position(location))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))

        if (marker != null) {
            marker.isDraggable = true
        }

        googleMap.setOnMarkerDragListener(this)

        latitude = location.latitude
        longitude = location.longitude
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        //if(args.questID >= 0) {
        //    quest = QuestManager.findById(args.questID)!!
        //}

        fragBinding.saveLocationButton.setOnClickListener {
            //val action = MapsFragmentDirections.actionMapsFragmentToQuestFragment(latitude.toFloat(), longitude.toFloat())
            //findNavController().navigate(action)
            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set("latitude", latitude)
            navController.previousBackStackEntry?.savedStateHandle?.set("longitude", longitude)
            navController.popBackStack()
        }

        return root
        //return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapsFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMarkerDrag(p0: Marker) {

    }

    override fun onMarkerDragEnd(marker: Marker) {
        i("DRAG ENDED")
        //if(args.questID >= 0) {
        //    quest.longitude = marker.position.longitude
        //    quest.latitude = marker.position.latitude
        //}
        //else {
            latitude = marker.position.latitude
            longitude = marker.position.longitude
        //}
    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}