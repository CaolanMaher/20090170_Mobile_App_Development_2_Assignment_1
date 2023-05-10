package ie.wit.a20090170_mobile_app_2_assignment_1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.CardQuestBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel

interface QuestClickListener {
    fun onQuestClick(quest: QuestModel)
}

class QuestAdapter constructor(private var quests: ArrayList<QuestModel>,
                                  private val listener: QuestClickListener)
    : RecyclerView.Adapter<QuestAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardQuestBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val quest = quests[holder.adapterPosition]
        holder.bind(quest,listener)
    }

    override fun getItemCount(): Int = quests.size

    inner class MainHolder(val binding : CardQuestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quest: QuestModel, listener: QuestClickListener) {
            //binding.root.tag = quest.id
            binding.root.tag = quest
            binding.quest = quest
            //binding.imageIcon.setImageResource(R.drawable.quest_icon)
            binding.root.setOnClickListener { listener.onQuestClick(quest) }
            binding.executePendingBindings()
        }
    }

    fun removeAt(position: Int) {
        quests.removeAt(position)
        notifyItemRemoved(position)
    }

}
