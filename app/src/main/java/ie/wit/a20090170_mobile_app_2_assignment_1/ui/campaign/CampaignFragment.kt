package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentCampaignBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.main.DNDCampaignManagerApp

class CampaignFragment : Fragment() {

    lateinit var app : DNDCampaignManagerApp
    private var _fragBinding : FragmentCampaignBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var campaignViewModel: CampaignViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        _fragBinding = FragmentCampaignBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        // recycler view

        campaignViewModel = ViewModelProvider(this).get(CampaignViewModel::class.java)
        // quests list

        // floating action button
        // button clicker


        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}