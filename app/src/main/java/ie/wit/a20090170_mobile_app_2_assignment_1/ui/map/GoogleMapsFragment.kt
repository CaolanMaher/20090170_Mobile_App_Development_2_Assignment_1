package ie.wit.a20090170_mobile_app_2_assignment_1.ui.map

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign.CampaignViewModel

class GoogleMapsFragment : Fragment() {

    /*
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

        /*
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
         */

        val setu = LatLng(52.245696, -7.139102)
        googleMap.addMarker(MarkerOptions().position(setu).title("Marker in Waterford"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(setu, 14f))
    }
     */

    private val googleMapsViewModel: GoogleMapsViewModel by activityViewModels()
    private val campaignViewModel: CampaignViewModel by activityViewModels()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        googleMapsViewModel.map = googleMap
        googleMapsViewModel.map.isMyLocationEnabled = true
        googleMapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                googleMapsViewModel.currentLocation.value!!.latitude,
                googleMapsViewModel.currentLocation.value!!.longitude
            )

            googleMapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            googleMapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
            googleMapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLng(loc))
            //googleMapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 5f))

            /*
            campaignViewModel.observableQuestsList.observe(
                viewLifecycleOwner,
                Observer { quests ->
                    quests?.let {
                        render(quests as ArrayList<QuestModel>)
                    }
                }
            )

            Handler().postDelayed({
                campaignViewModel.load()
            }, 2000)
             */
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupMenu()

        campaignViewModel.observableQuestsList.observe(
            viewLifecycleOwner,
            Observer { quests ->
                quests?.let {
                    render(quests as ArrayList<QuestModel>)
                }
            }
        )

        Handler().postDelayed({
            campaignViewModel.load()
        }, 2000)

        return inflater.inflate(R.layout.fragment_google_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun render(questsList: ArrayList<QuestModel>) {
        var markerColour: Float

        if(!questsList.isEmpty()) {
            googleMapsViewModel.map.clear()
            questsList.forEach {
                markerColour = if(it.userId.equals(this.campaignViewModel.auth.currentUser?.uid))
                    BitmapDescriptorFactory.HUE_AZURE + 5
                else
                    BitmapDescriptorFactory.HUE_RED
                googleMapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.name} ${it.reward}")
                        .snippet(it.description)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_campaign, menu)

                val item = menu.findItem(R.id.toggleQuests) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleQuests: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleQuests.isChecked = false

                toggleQuests.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) campaignViewModel.loadAll()
                    else campaignViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}