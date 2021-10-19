package da.reza.spy.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import da.reza.spy.R
import da.reza.spy.databinding.FinishGameBinding
import da.reza.spy.ui.adapter.GameStarterPlayerNameAdapter
import da.reza.spy.utiles.*
import da.reza.spy.viewModel.GameViewModel
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.TapsellPlus
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class FinishGameDialog: BottomSheetDialogFragment() , KoinComponent {


    private val viewModel:GameViewModel by activityViewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var spyFound = false
    private var _binding: FinishGameBinding? = null
    private val binding get() = _binding!!


    fun setSpyFound(found:Boolean) = apply {
        spyFound = found
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.NewDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FinishGameBinding>(inflater, R.layout.finish_game_dialog, container, false)
            .apply { _binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        firebaseAnalytics = Firebase.analytics
        requestAd()
        setUpViews()
        observeViewModel()
    }

    private fun setUpViews(){


        val iconlist = listOf(R.drawable.detective , R.drawable.trasnparent_detective_two)
        binding.detectiveIcon.setImageDrawable(AppCompatResources.getDrawable(requireActivity(),iconlist.random()))

        if (spyFound){
            binding.txtTitle.text = getString(R.string.spyFoundTitle)
            binding.btnConfirm.text = getString(R.string.spyFoundConfirmButton)
            binding.btnCancel.text = getString(R.string.spyFoundCancelButton)
        }else {
            binding.txtTitle.text = getString(R.string.spyNotFoundTitle)
            binding.btnConfirm.text = getString(R.string.spyNotFoundConfirmButton)
            binding.btnCancel.text = getString(R.string.spyNotFoundCancelButton)
        }


        binding.btnCancel.setOnClickListener {
            firebaseAnalytics.logEvent("finishGame") {
                param("isSpyFound" , spyFound.asLongValue() )
            }
            dismiss()
            findNavController().navigate(R.id.TimerToHome)

        }

        binding.btnConfirm.setOnClickListener{
            firebaseAnalytics.logEvent("playAgain") {
                param("playAgain" , "playAgain" )
            }
            viewModel.startGame()
        }

    }


    private fun observeViewModel(){

        viewModel.gameCardForPlay.observe(viewLifecycleOwner) {
            var spyName = " "
            it.forEach { card ->
                if (card.isSpy && card.playerName.isNotBlank()) spyName += " { ${card.playerName} }"
                if (card.playerName.isNotBlank()){
                    if (card.isSpy && !spyFound) {
                        viewModel.addSpyPlayerWinCounter(card.playerName)
                        viewModel.addPlayerPlayCounter(card.playerName)
                    }else if (card.isSpy && spyFound) {
                        viewModel.addPlayerPlayCounter(card.playerName)
                    }


                }
            }
            if (spyName.isNotBlank()){
                binding.spyNameLayout.setVisible()
                binding.txtSpyNames.text = spyName
            }

        }


        viewModel.startGame.onEach {
            findNavController().navigate(R.id.TimerPage_To_gameCard)
            dismiss()
        }.observeInLifecycle(this)

    }

    private fun requestAd(){
       RequestTapsellAd()
           .setActivity(requireActivity())
           .setAdViewGroup(binding.adContainer)
           .setScreenName("finishDialog")
           .build()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }




}