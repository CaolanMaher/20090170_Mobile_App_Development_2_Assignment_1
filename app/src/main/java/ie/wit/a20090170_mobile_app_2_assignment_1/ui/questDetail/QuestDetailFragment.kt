package ie.wit.a20090170_mobile_app_2_assignment_1.ui.questDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import ie.wit.a20090170_mobile_app_2_assignment_1.R

class QuestDetailFragment : Fragment() {

    private val args by navArgs<QuestDetailFragmentArgs>()

    companion object {
        fun newInstance() = QuestDetailFragment()
    }

    private lateinit var viewModel: QuestDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_donation_detail, container, false)

        Toast.makeText(context,"Donation ID Selected : ${args.questID}", Toast.LENGTH_LONG).show()

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QuestDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}