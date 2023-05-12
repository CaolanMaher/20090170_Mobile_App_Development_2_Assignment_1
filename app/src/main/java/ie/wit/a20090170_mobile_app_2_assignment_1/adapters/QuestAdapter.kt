package ie.wit.a20090170_mobile_app_2_assignment_1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.CardQuestBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.customTransformation
import timber.log.Timber

interface QuestClickListener {
    fun onQuestClick(quest: QuestModel)
    fun onQuestFavouriteClick(quest: QuestModel)
}

class QuestAdapter constructor(private var quests: ArrayList<QuestModel>,
                                  private val listener: QuestClickListener,
                                    private val readOnly: Boolean)
    : RecyclerView.Adapter<QuestAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardQuestBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        //return MainHolder(binding)
        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val quest = quests[holder.adapterPosition]
        holder.bind(quest,listener)
    }

    override fun getItemCount(): Int = quests.size

    inner class MainHolder(val binding : CardQuestBinding, private val readOnly : Boolean) : RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(quest: QuestModel, listener: QuestClickListener) {
            //binding.root.tag = quest.id
            binding.root.tag = quest
            binding.quest = quest
            Picasso.get().load(quest.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            if(quest.isFavourite) {
                Picasso.get().load(R.drawable.quest_icon)
                    .resize(200, 200)
                    //.transform(customTransformation())
                    //.centerCrop()
                    .into(binding.imagefavourite)
            }
            //binding.imageIcon.setImageResource(R.drawable.quest_icon)
            binding.root.setOnClickListener { listener.onQuestClick(quest) }
            binding.imagefavourite.setOnClickListener { listener.onQuestFavouriteClick(quest) }
            binding.executePendingBindings()
        }
    }

    /*
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
     */

    fun removeAt(position: Int) {
        quests.removeAt(position)
        notifyItemRemoved(position)
    }

}
