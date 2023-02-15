package ie.wit.a20090170_mobile_app_2_assignment_1.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import ie.wit.a20090170_mobile_app_2_assignment_1.R

class DonationDetailFragment : Fragment() {

    private val args by navArgs<DonationDetailFragmentArgs>()

    companion object {
        fun newInstance() = DonationDetailFragment()
    }

    private lateinit var viewModel: DonationDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_donation_detail, container, false)

        Toast.makeText(context,"Donation ID Selected : ${args.donationid}", Toast.LENGTH_LONG).show()

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DonationDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}