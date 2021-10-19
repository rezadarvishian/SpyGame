package da.reza.spy.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import da.reza.spy.R
import da.reza.spy.databinding.StarterFragmentBinding
import da.reza.spy.ui.adapter.GameStarterPlayerNameAdapter
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.utiles.*
import da.reza.spy.viewModel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach


class StarterFragment : BaseFragment<StarterFragmentBinding>(R.layout.fragment_starter) , GameStarterPlayerNameAdapter.OnPlayerNameClickListener {

    private val viewModel: GameViewModel by activityViewModels()
    private val thisRoundPLayerAdapter = GameStarterPlayerNameAdapter(true)
    private val allPlayerNameAdapter = GameStarterPlayerNameAdapter(false)
    override fun onBackPress() { findNavController().navigateUp() }
    override fun adContainerID() = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.thisRoundPLayerRecycler.adapter = thisRoundPLayerAdapter
        binding.allPLayerRecycler.adapter = allPlayerNameAdapter
        allPlayerNameAdapter.setOnClickListener(this)
        observeViewModel()
        binding.btnAddPlayer.setOnClickListener { viewModel.changePLayerCount(+1) }
        binding.btnMinusPlayer.setOnClickListener { viewModel.changePLayerCount(-1) }
        binding.btnAddSpy.setOnClickListener { viewModel.changeSpyCounter(+1) }
        binding.btnMinusSpy.setOnClickListener { viewModel.changeSpyCounter(-1) }
        binding.btnAddTime.setOnClickListener { viewModel.changeTimer(+1) }
        binding.btnMinusTime.setOnClickListener { viewModel.changeTimer(-1) }
        binding.playWithNameWitch.setOnClickListener { viewModel.changePlayWithNameState( binding.playWithNameWitch.isChecked) }
        binding.btnPlay.setOnClickListener {
            if (binding.playWithNameWitch.isChecked) {
                viewModel.setPlayerNames(thisRoundPLayerAdapter.getAllPlayerName())
            }
            viewModel.startGame()
        }


        binding.edtInputName.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                binding.btnAddName.callOnClick()
                return@OnEditorActionListener true
            }
            false
        })

        binding.btnAddName.setOnClickListener{
            val name = binding.edtInputName.text?.trim().toString()
            if (name.isNotBlank()) viewModel.addPlayerName(name)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel(){

        viewModel.playerNumber.observe(viewLifecycleOwner){
            it?.let {
                binding.txtPlayerCount.text = it.toString()
                val title = getString(R.string.this_round_players)
                binding.txtPlayerNameTitle.text = "$title $it بازیکن "
            }
        }
        viewModel.spyNumber.observe(viewLifecycleOwner){
            it?.let {
                binding.txtSpyCounter.text = it.toString()
            }
        }
        viewModel.timerAmount.observe(viewLifecycleOwner){
            it?.let {
                binding.txtTimeCounter.text = it.toString()
            }
        }

        viewModel.playWithNames.observe(viewLifecycleOwner){
            binding.playWithNameWitch.isChecked = it
            if (it) {
                binding.addNameLayout.setVisible()
                binding.inputLayout.setVisible()
            } else {
                binding.addNameLayout.setGone()
                binding.inputLayout.setGone()
            }
        }

        viewModel.message.onEach {
            requireActivity().showToast(it)
        }.observeInLifecycle(this)

        viewModel.allPlayerNames.onEach {
            if (!it.isNullOrEmpty()) {
                binding.emptyAllPlayerList.setInvisible()
                binding.allPLayerRecycler.setVisible()
                val playerName = mutableListOf<String>()
                it.forEach { playerName.add(it.name) }
                allPlayerNameAdapter.refreshData(playerName)
            }else {
                binding.allPLayerRecycler.setInvisible()
                binding.emptyAllPlayerList.setVisible()
                thisRoundPLayerAdapter.refreshData(mutableListOf())

            }
        }.observeInLifecycle(this)

        viewModel.thisRoundPlayerNames.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                binding.emptyThisRoundPlayerList.setInvisible()
                binding.thisRoundPLayerRecycler.setVisible()
                thisRoundPLayerAdapter.refreshData(it)
            }else {
                binding.thisRoundPLayerRecycler.setInvisible()
                binding.emptyThisRoundPlayerList.setVisible()
            }
        }

        viewModel.startGame.onEach {
            findNavController().navigate(R.id.StarterToGameCard)
        }.observeInLifecycle(this)

        viewModel.addPlayerState.onEach {
            val message = thisRoundPLayerAdapter.addNewPLayer(it)
            requireActivity().showToast(message)
            binding.emptyThisRoundPlayerList.setInvisible()
            binding.thisRoundPLayerRecycler.setVisible()
            binding.edtInputName.setText("")
        }.observeInLifecycle(this)

    }


    override fun onNameClicked(name: String) {
        val message = thisRoundPLayerAdapter.addNewPLayer(name)
        requireActivity().showToast(message)
        binding.emptyThisRoundPlayerList.setInvisible()
        binding.thisRoundPLayerRecycler.setVisible()
    }

}