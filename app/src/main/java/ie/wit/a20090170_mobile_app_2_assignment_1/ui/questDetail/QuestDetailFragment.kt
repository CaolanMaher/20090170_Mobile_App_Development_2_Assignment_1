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
import androidx.navigation.fragment.findNavController
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.FragmentQuestDetailBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.quest.QuestFragmentDirections

class QuestDetailFragment : Fragment() {

    private val args by navArgs<QuestDetailFragmentArgs>()

    private lateinit var questDetailViewModel: QuestDetailViewModel

    private var _fragBinding: FragmentQuestDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!

    private lateinit var quest : QuestModel

    var totalQuestsCompleted = 0

    private var latitude = 0.0
    private var longitude = 0.0

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

        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Double>("latitude")
            ?.observe(viewLifecycleOwner) {
                latitude = it
            }
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Double>("longitude")
            ?.observe(viewLifecycleOwner) {
                longitude = it
            }

        //val view = inflater.inflate(R.layout.fragment_quest_detail, container, false)

        Toast.makeText(context,"Quest ID Selected : ${args.questID}", Toast.LENGTH_LONG).show()

        //var quests = QuestManager.findAll()

        val quests = QuestManager.findAll()

        totalQuestsCompleted = 0
        for(quest in quests) {
            if(quest.isCompleted) {
                totalQuestsCompleted++
            }
        }

        quest = QuestManager.findById(args.questID)!!

        fragBinding.editQuestID.text = quest.id.toString()

        fragBinding.editQuestNameDetail.setText(quest.name)
        fragBinding.editQuestLocationNameDetail.setText(quest.locationName)
        fragBinding.editQuestDescriptionDetail.setText(quest.description)
        fragBinding.editRewardAmountDetail.minValue = 1
        fragBinding.editRewardAmountDetail.maxValue = 1000
        fragBinding.editRewardAmountDetail.value = quest.reward
        fragBinding.editQuestCompleteDetail.isChecked = quest.isCompleted

        fragBinding.questsCompletedProgressDetail.max = quests.size
        fragBinding.questsCompletedProgressDetail.progress = totalQuestsCompleted

        fragBinding.questsCompletedSoFarDetailAmount.text = totalQuestsCompleted.toString()

        setButtonListener(fragBinding)

        return root

        //return view
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.questUpdateError),Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentQuestDetailBinding) {

        layout.editQuestLocationButton.setOnClickListener {
            val action =
                QuestDetailFragmentDirections.actionQuestDetailFragmentToMapsFragment(quest.id)
            findNavController().navigate(action)
        }

        layout.updateQuestButton.setOnClickListener {
            val newRewardAmount = layout.editRewardAmountDetail.value
            val newQuestName = if (layout.editQuestNameDetail.text.isNotEmpty())
                layout.editQuestNameDetail.text.toString() else "N/A"
            val newQuestLocationName = if (layout.editQuestLocationNameDetail.text.isNotEmpty())
                layout.editQuestLocationNameDetail.text.toString() else "N/A"
            val newQuestDescription = if (layout.editQuestDescriptionDetail.text.isNotEmpty())
                layout.editQuestDescriptionDetail.text.toString() else "N/A"
            val newQuestComplete = layout.editQuestCompleteDetail.isChecked

            quest.reward = newRewardAmount
            quest.name = newQuestName
            quest.locationName = newQuestLocationName
            quest.description = newQuestDescription
            quest.isCompleted = newQuestComplete
            quest.latitude = latitude
            quest.longitude = longitude

            questDetailViewModel.updateQuest(quest)

            //val quests = QuestManager.findAll()
            val quests = questDetailViewModel.getQuests()

            totalQuestsCompleted = 0
            for (quest in quests) {
                if (quest.isCompleted) {
                    totalQuestsCompleted++
                }
            }

            fragBinding.questsCompletedSoFarDetailAmount.text = totalQuestsCompleted.toString()
            fragBinding.questsCompletedProgressDetail.progress = totalQuestsCompleted

            layout.editQuestNameDetail.setText("")
            layout.editQuestDescriptionDetail.setText("")
            layout.editQuestLocationNameDetail.setText("")
            layout.editRewardAmountDetail.value = 1
            layout.editQuestCompleteDetail.isChecked = false
        }

        layout.deleteQuestButton.setOnClickListener {
            questDetailViewModel.deleteQuest(quest.id)
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

        //val quests = QuestManager.findAll()
        val quests = questDetailViewModel.getQuests()

        totalQuestsCompleted = 0
        for (quest in quests) {
            if (quest.isCompleted) {
                totalQuestsCompleted++
            }
        }

        fragBinding.questsCompletedSoFarDetailAmount.text = totalQuestsCompleted.toString()
        fragBinding.questsCompletedProgressDetail.max = quests.size
        fragBinding.questsCompletedProgressDetail.progress = totalQuestsCompleted

        //fragBinding.editQuestNameDetail.setText("")
        //fragBinding.editQuestLocationNameDetail.setText("")
        //fragBinding.editQuestDescriptionDetail.setText("")
    }

}