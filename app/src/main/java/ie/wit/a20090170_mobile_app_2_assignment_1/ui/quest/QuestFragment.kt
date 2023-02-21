package ie.wit.a20090170_mobile_app_2_assignment_1.ui.quest

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentDonateBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentQuestBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.main.DonationXApp
import ie.wit.a20090170_mobile_app_2_assignment_1.models.DonationModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign.CampaignViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.quest.QuestViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.report.ReportViewModel

class QuestFragment : Fragment() {

    var totalQuestsCompleted = 0
    private var _fragBinding: FragmentQuestBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var questViewModel: QuestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentQuestBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        questViewModel = ViewModelProvider(this).get(QuestViewModel::class.java)
        questViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })


        //fragBinding.progressBar.max = 10000
        fragBinding.rewardAmountPicker.minValue = 1
        fragBinding.rewardAmountPicker.maxValue = 1000

        fragBinding.rewardAmountPicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
            fragBinding.enterRewardAmount.setText("$newVal")
        }
        setButtonListener(fragBinding)

        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.questError),Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentQuestBinding) {
        layout.questButton.setOnClickListener {
            val rewardAmount = if (layout.enterRewardAmount.text.isNotEmpty())
                layout.enterRewardAmount.text.toString().toInt() else layout.rewardAmountPicker.value
            val questName = if(layout.editQuestName.text.isNotEmpty())
                layout.editQuestName.text.toString() else "N/A"
            val questLocationName = if(layout.editQuestLocationName.text.isNotEmpty())
                layout.editQuestLocationName.text.toString() else "N/A"
            val questDescription = if(layout.editQuestDescription.text.isNotEmpty())
                layout.editQuestDescription.text.toString() else "N/A"
            if(totalQuestsCompleted >= layout.progressBar.max)
                Toast.makeText(context,"Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            else {
                //val paymentmethod = if(layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
                val questComplete = layout.questCompleteBox.isChecked
                if(questComplete) {
                    totalQuestsCompleted++
                }
                layout.totalQuestsCompleted.text = getString(R.string.total_donated,totalQuestsCompleted)
                layout.progressBar.progress = totalQuestsCompleted

                val quest = QuestModel(0, questName, questDescription, questLocationName, rewardAmount, questComplete)
                questViewModel.addQuest(quest)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val campaignViewModel = ViewModelProvider(this).get(CampaignViewModel::class.java)
        campaignViewModel.observableQuestsList.observe(viewLifecycleOwner, Observer {
            //totalQuestsCompleted = campaignViewModel.observableQuestsList.value!!.sumOf { it.isCompleted }
            totalQuestsCompleted = 0
            val quests = campaignViewModel.observableQuestsList.value!!
            for(quest in quests) {
                if(quest.isCompleted) {
                    totalQuestsCompleted++
                }
            }
            fragBinding.progressBar.progress = totalQuestsCompleted
            fragBinding.totalQuestsCompleted.text = getString(R.string.total_donated,totalQuestsCompleted)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_donate, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
