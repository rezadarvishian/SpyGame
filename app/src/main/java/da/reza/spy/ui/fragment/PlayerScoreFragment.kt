package da.reza.spy.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.databinding.PlayerScoreBinding
import da.reza.spy.ui.adapter.PlayerScoreAdapter
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.utiles.observeInLifecycle
import da.reza.spy.utiles.setInvisible
import da.reza.spy.utiles.setVisible
import da.reza.spy.viewModel.PlayerScoreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerScoreFragment : BaseFragment<PlayerScoreBinding>(R.layout.fragment_player_score) {


    private val viewModel : PlayerScoreViewModel by viewModel()
    private val adapter = PlayerScoreAdapter()
    private val scoreList: MutableList<PlayerNames> = mutableListOf()
    override fun onBackPress() { findNavController().navigateUp() }
    override fun adContainerID() = binding.adContainer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPlayerList()
        binding.edtSearchBox.doAfterTextChanged {
            val text = binding.edtSearchBox.text.toString()
            if (text.isEmpty()) {
                adapter.refreshData(scoreList)
            } else {
                adapter.searchInList(text)
            }
        }
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }


    private fun showPlayerList(){
        binding.scoreRecycler.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(recyclerViewCallBack)
        itemTouchHelper.attachToRecyclerView(binding.scoreRecycler)


        viewModel.playScoreList.onEach {
            if (it.isNotEmpty()){
                binding.txtNotFound.setInvisible()
                binding.scoreRecycler.setVisible()

                it.forEach {
                    it.winPercent = ((it.WinCounter.toDouble() / it.spyCounter) * 100).toInt()
                }

                val sorted = it.sortedBy { it.winPercent }
                    .toMutableList()
                    .asReversed()
                delay(300)
                adapter.refreshData(sorted)
                scoreList.clear()
                scoreList.addAll(sorted)
            }else {
                binding.scoreRecycler.setInvisible()
                binding.txtNotFound.setVisible()
            }
        }.observeInLifecycle(this)


    }


    private var recyclerViewCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(
        ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            viewModel.deletePlayer(scoreList[position])
            Toast.makeText(
                requireActivity(),
                getString(R.string.deleted),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


}