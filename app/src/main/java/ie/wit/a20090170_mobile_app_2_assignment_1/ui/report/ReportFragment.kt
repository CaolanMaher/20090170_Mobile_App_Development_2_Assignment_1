package ie.wit.a20090170_mobile_app_2_assignment_1.ui.report

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.adapters.DonationAdapter
import ie.wit.a20090170_mobile_app_2_assignment_1.adapters.DonationClickListener
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentReportBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.main.DonationXApp
import ie.wit.a20090170_mobile_app_2_assignment_1.models.DonationModel

class ReportFragment : Fragment(), DonationClickListener {

    lateinit var app: DonationXApp
    private var _fragBinding: FragmentReportBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        reportViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
                donations ->
            donations?.let { render(donations) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ReportFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(donationsList: List<DonationModel>) {
        fragBinding.recyclerView.adapter = DonationAdapter(donationsList,this)
        if (donationsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.donationsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onDonationClick(donation: DonationModel) {
        //val action = ReportFragmentDirections.actionReportFragmentToDonationDetailFragment(donation.id)
        val action = ReportFragmentDirections.actionReportFragmentToDonationDetailFragment(donation.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
