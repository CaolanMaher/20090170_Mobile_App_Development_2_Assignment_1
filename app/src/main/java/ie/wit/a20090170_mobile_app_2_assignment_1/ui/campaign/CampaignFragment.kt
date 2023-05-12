package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.adapters.QuestAdapter
import ie.wit.a20090170_mobile_app_2_assignment_1.adapters.QuestClickListener
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.CardQuestBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentCampaignBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.main.DNDCampaignApp
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.SwipeToDeleteCallback
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.SwipeToEditCallback

class CampaignFragment : Fragment(), QuestClickListener {

    lateinit var app: DNDCampaignApp
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
            quests?.let { render(quests as ArrayList<QuestModel>) }
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

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.questsRecyclerView.adapter as QuestAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                campaignViewModel.delete((viewHolder.itemView.tag as QuestModel).id)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.questsRecyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onQuestClick(viewHolder.itemView.tag as QuestModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.questsRecyclerView)

        return root
    }

    /*
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
     */

    fun addToFavourite() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.menu_campaign, menu)
        //super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_campaign, menu)

        val item = menu.findItem(R.id.toggleQuests) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleQuests: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
        toggleQuests.isChecked = false

        toggleQuests.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) campaignViewModel.loadAll()
            else campaignViewModel.load()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(questsList: ArrayList<QuestModel>) {
        fragBinding.questsRecyclerView.adapter = QuestAdapter(questsList,this, campaignViewModel.readOnly.value!!)
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

        if(!campaignViewModel.readOnly.value!!) {
            findNavController().navigate(action)
        }
    }

    override fun onQuestFavouriteClick(quest: QuestModel) {
        quest.isFavourite = !quest.isFavourite

        if (quest.isFavourite) {
            Toast.makeText(activity, "Added quest: ${quest.id} to favourites",
                Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(activity, "Removed quest: ${quest.id} from favourites",
                Toast.LENGTH_SHORT).show()
        }

        campaignViewModel.updateQuest(quest)
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
