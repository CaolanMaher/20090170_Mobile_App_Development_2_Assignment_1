package ie.wit.a20090170_mobile_app_2_assignment_1.ui.questDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentQuestBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentQuestDetailBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.quest.QuestViewModel

class QuestDetailFragment : Fragment() {

    private val args by navArgs<QuestDetailFragmentArgs>()

    private lateinit var questDetailViewModel: QuestDetailViewModel

    private var _fragBinding: FragmentQuestDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!

    private lateinit var quest : QuestModel

    companion object {
        fun newInstance() = QuestDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentQuestDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        questDetailViewModel = ViewModelProvider(this).get(QuestDetailViewModel::class.java)
        questDetailViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        //val view = inflater.inflate(R.layout.fragment_quest_detail, container, false)

        Toast.makeText(context,"Quest ID Selected : ${args.questID}", Toast.LENGTH_LONG).show()

        //var quests = QuestManager.findAll()

        quest = QuestManager.findById(args.questID)!!

        fragBinding.editQuestID.text = quest.id.toString()

        fragBinding.editQuestNameDetail.setText(quest.name)
        fragBinding.editQuestLocationNameDetail.setText(quest.locationName)
        fragBinding.editQuestDescriptionDetail.setText(quest.description)
        fragBinding.editRewardAmountDetail.minValue = 0
        fragBinding.editRewardAmountDetail.maxValue = 1000
        fragBinding.editRewardAmountDetail.value = quest.reward
        fragBinding.editQuestCompleteDetail.isChecked = quest.isCompleted

        setButtonListener(fragBinding)

        return root

        //return view
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.questUpdateError),Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentQuestDetailBinding) {

        layout.updateQuestButton.setOnClickListener {
            val newRewardAmount = layout.editRewardAmountDetail.value
            val newQuestName = if(layout.editQuestNameDetail.text.isNotEmpty())
                layout.editQuestNameDetail.text.toString() else "N/A"
            val newQuestLocationName = if(layout.editQuestLocationNameDetail.text.isNotEmpty())
                layout.editQuestLocationNameDetail.text.toString() else "N/A"
            val newQuestDescription = if(layout.editQuestDescriptionDetail.text.isNotEmpty())
                layout.editQuestDescriptionDetail.text.toString() else "N/A"
            val newQuestComplete = layout.editQuestCompleteDetail.isChecked

            quest.reward = newRewardAmount
            quest.name = newQuestName
            quest.locationName = newQuestLocationName
            quest.description = newQuestDescription
            quest.isCompleted = newQuestComplete

            questDetailViewModel.updateQuest(quest)
        }

        layout.deleteQuestButton.setOnClickListener {
            questDetailViewModel.deleteQuest(quest.id)
        }

        /*
        layout.questButton.setOnClickListener {
            val rewardAmount = if (layout.enterRewardAmount.text.isNotEmpty())
                layout.enterRewardAmount.text.toString().toInt() else layout.rewardAmountPicker.value
            val questName = if(layout.editQuestName.text.isNotEmpty())
                layout.editQuestName.text.toString() else "N/A"
            val questLocationName = if(layout.editQuestLocationName.text.isNotEmpty())
                layout.editQuestLocationName.text.toString() else "N/A"
            val questDescription = if(layout.editQuestDescription.text.isNotEmpty())
                layout.editQuestDescription.text.toString() else "N/A"

            layout.progressBar.max++
            //if(totalQuestsCompleted >= layout.progressBar.max)
            //    Toast.makeText(context,"Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            //else {
            //val paymentmethod = if(layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
            val questComplete = layout.questCompleteBox.isChecked
            if(questComplete) {
                totalQuestsCompleted++
            }
            layout.totalQuestsCompleted.text = getString(R.string.total_donated,totalQuestsCompleted)
            layout.progressBar.progress = totalQuestsCompleted

            val quest = QuestModel(0, questName, questDescription, questLocationName, rewardAmount, questComplete)
            questViewModel.addQuest(quest)
            //}
        }

         */
    }

    /*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(QuestDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

     */

}