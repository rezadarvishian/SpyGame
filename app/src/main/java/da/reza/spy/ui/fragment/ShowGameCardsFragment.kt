package da.reza.spy.ui.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.perf.metrics.AddTrace
import da.reza.spy.R
import da.reza.spy.Spy
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.databinding.ShowGameCardBinding
import da.reza.spy.ui.adapter.GameCardAdapter
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.ui.dialog.MaterialQuestionDialog
import da.reza.spy.ui.dialog.ShowImageDialog
import da.reza.spy.viewModel.GameViewModel


class ShowGameCardsFragment : BaseFragment<ShowGameCardBinding>(R.layout.fragment_show_game_cards),
    GameCardAdapter.RecyclerViewInterface {


    private val viewModel: GameViewModel by activityViewModels()
    override fun adContainerID() = binding.adContainer
    private var mediaPlayer: MediaPlayer? = null
    private val adapter  = GameCardAdapter(this)
    private var players = 0

    val listener = object : ShowImageDialog.DialogListener {
        override fun onCloseClicked() {
            if (players == 0) {
                findNavController().navigate(R.id.GameCardsToTimer)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext() , R.raw.alert)
        initGameCardRecyclerView()
    }

    override fun onBackPress() {
        MaterialQuestionDialog(requireActivity())
            .setTitle(getString(R.string.exit_title2))
            .setCancelTitle(getString(R.string.exit_dialog_cancel_button))
            .setConfirmTitle(getString(R.string.exit_dialog_confirm_button))
            .setAnimationResId(R.raw.close)
            .setOnCancel {}
            .setOnConfirm { findNavController().navigate(R.id.GameCardToHome) }
            .show()
    }

    @AddTrace(name = "initGameCardTrace", enabled = true)
    private fun initGameCardRecyclerView() {
        binding.gameCardRecyclerView.setHasFixedSize(true)
        binding.gameCardRecyclerView.adapter = adapter
        viewModel.gameCardForPlay.observe(viewLifecycleOwner) {
            players = it.count()
            adapter.refreshGameCard(it)
        }
    }

    override fun showDialog(item: GameCardItem) {
        players -= 1
        ShowImageDialog(requireActivity(), listener).setGameCard(item).show()
        if (item.isStarterPlayer && Spy.isSoundEnable) mediaPlayer?.start()
    }

    override fun onDestroyView() {
        mediaPlayer?.release()
        super.onDestroyView()
    }

}