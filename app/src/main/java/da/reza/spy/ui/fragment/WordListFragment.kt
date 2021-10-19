package da.reza.spy.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.perf.metrics.AddTrace
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.ui.MainActivity.Companion.TAG
import da.reza.spy.ui.adapter.WordsAdapter
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.ui.dialog.*
import da.reza.spy.utiles.observeInLifecycle
import da.reza.spy.utiles.setInvisible
import da.reza.spy.utiles.setVisible
import da.reza.spy.viewModel.WordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class WordListFragment : BaseFragment<da.reza.spy.databinding.FragmentWordListBinding>(R.layout.fragment_word_list) , WordsAdapter.OnItemClickListener {


    private val viewModel: WordViewModel by viewModel()
    override fun adContainerID() = binding.adContainer
    override fun onBackPress() { findNavController().navigateUp() }
    private val wordList: MutableList<GameCardItem> = mutableListOf()
    private lateinit var wordsAdapter: WordsAdapter


    val listener = object : WordListInfoDialog.DialogListener {
        override fun onDeleteClicked() {
            MaterialQuestionDialog(requireActivity())
                .setTitle(getString(R.string.delete_question))
                .setCancelTitle(getString(R.string.delete_question_No))
                .setConfirmTitle(getString(R.string.delete_question_yes))
                .setAnimationResId(R.raw.delete)
                .setOnCancel {}
                .setOnConfirm {
                    viewModel.deleteAllWords()
                    wordsAdapter.refreshData(mutableListOf())
                }
                .show()
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        binding.edtSearchBox.doAfterTextChanged {

            val text = binding.edtSearchBox.text.toString()
            if (text.isEmpty()) {
                wordsAdapter.refreshData(wordList)
            } else {
                wordsAdapter.searchInList(text)
            }
        }

        binding.btnAddWord.setOnClickListener {
           AddWordDialog().show(requireActivity().supportFragmentManager,"add_word")
        }

        binding.btnInformation.setOnClickListener {
            WordListInfoDialog(requireActivity(), listener).show()
        }

        binding.btnImageSearch.setOnClickListener {
            findNavController().navigate(R.id.WordListToImageSearch)
        }

        binding.btnSuggestWord.setOnClickListener {
            findNavController().navigate(R.id.WordListToSuggestWord)
        }


    }


    @AddTrace(name = "wordListTrace", enabled = true)
    private fun setUpRecyclerView() {
        wordsAdapter = WordsAdapter(viewModel , this)
        val itemTouchHelper = ItemTouchHelper(recyclerViewCallBack)
        itemTouchHelper.attachToRecyclerView(binding.wordRecyclerView)
        binding.wordRecyclerView.adapter = wordsAdapter
        lifecycleScope.launchWhenResumed {
            viewModel.gameCards.onEach {

                if(it.isNotEmpty()){
                    delay(500)
                    binding.txtNotFound.setInvisible()
                    binding.wordRecyclerView.setVisible()
                    wordList.clear()
                    wordList.addAll(it.asReversed())
                    wordsAdapter.refreshData(it.toMutableList().asReversed())
                    Log.i(TAG, it.size.toString())
                }else {
                    binding.wordRecyclerView.setInvisible()
                    binding.txtNotFound.setVisible()
                }

            }.observeInLifecycle(this@WordListFragment)
        }
    }

    private var recyclerViewCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteGameCard(wordList[position])
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    override fun onItemClick(item: GameCardItem) {
        AddWordDialog(item).show(requireActivity().supportFragmentManager,"add_word")
    }

}