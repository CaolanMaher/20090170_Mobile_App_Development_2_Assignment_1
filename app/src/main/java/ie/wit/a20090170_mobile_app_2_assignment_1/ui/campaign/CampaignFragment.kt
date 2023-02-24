package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.adapters.QuestAdapter
import ie.wit.a20090170_mobile_app_2_assignment_1.adapters.QuestClickListener
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentCampaignBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.main.DonationXApp
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel

class CampaignFragment : Fragment(), QuestClickListener {

    lateinit var app: DonationXApp
    private var _fragBinding: FragmentCampaignBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var campaignViewModel: CampaignViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentCampaignBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.questsRecyclerView.layoutManager = LinearLayoutManager(activity)

        campaignViewModel = ViewModelProvider(this).get(CampaignViewModel::class.java)

        campaignViewModel.observableQuestsList.observe(viewLifecycleOwner, Observer {
                quests ->
            quests?.let { render(quests) }
        })

        Handler().postDelayed({
                              campaignViewModel.load()
        }, 2000)

        val searchButton = fragBinding.searchButton
        searchButton.setOnClickListener {
            val questNameToSearchFor = fragBinding.searchByQuestNameText.text.toString()
            campaignViewModel.searchByQuestName(questNameToSearchFor)
            //updateView()
        }

        val cancelSearchButton = fragBinding.cancelSearchButton
        cancelSearchButton.setOnClickListener {
            fragBinding.searchByQuestNameText.setText("")
            campaignViewModel.load()
        }

        /*
        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = CampaignFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }
         */
        return root
    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            campaignViewModel.observableQuestsList.observe(viewLifecycleOwner, Observer {
                    quests ->
                quests?.let { render(quests) }
            })
        }, 3000)
    }

     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_campaign, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(questsList: List<QuestModel>) {
        fragBinding.questsRecyclerView.adapter = QuestAdapter(questsList,this)
        if (questsList.isEmpty()) {
            fragBinding.questsRecyclerView.visibility = View.GONE
            fragBinding.questsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.questsRecyclerView.visibility = View.VISIBLE
            fragBinding.questsNotFound.visibility = View.GONE
        }
    }

    override fun onQuestClick(quest: QuestModel) {
        //val action = ReportFragmentDirections.actionReportFragmentToDonationDetailFragment(donation.id)
        val action = CampaignFragmentDirections.actionCampaignFragmentToQuestDetailFragment(quest.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        campaignViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
        //parentFragment?.let { campaignViewModel.observableQuestsList.removeObservers(it.viewLifecycleOwner) }
    }
}
