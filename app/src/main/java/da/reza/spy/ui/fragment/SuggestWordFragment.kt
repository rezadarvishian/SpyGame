package da.reza.spy.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.perf.metrics.AddTrace
import da.reza.spy.R
import da.reza.spy.ui.adapter.SuggestWordAdapter
import da.reza.spy.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import da.reza.spy.databinding.SuggestWordBinding
import da.reza.spy.utiles.*
import da.reza.spy.viewModel.WordViewModel


class SuggestWordFragment : BaseFragment<SuggestWordBinding>(R.layout.fragment_suggest_word) , SuggestWordAdapter.OnSuggestWordListener {


    private val viewModel : WordViewModel by viewModel()
    override fun onBackPress() { findNavController().navigateUp() }
    override fun adContainerID() = binding.adContainer
    private val adapter = SuggestWordAdapter(this)

    @RequiresApi(Build.VERSION_CODES.N)
    @AddTrace(name = "ImageSearchTrace", enabled = true)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.imageRecycler.adapter = adapter
        binding.edtInputSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.progressBar.setVisible()
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    delay(200)
                    val keyWord = binding.edtInputSearch.text?.trim()
                    if (!keyWord.isNullOrBlank()){
                        viewModel.suggestWord(keyWord.toString())
                        viewModel.wordMeaning(keyWord.toString())
                        requireActivity().hideKeyboard()
                    }else {
                        binding.progressBar.setInvisible()
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeViewModel(){
        viewModel.suggestWord.observe(viewLifecycleOwner){
            binding.progressBar.setInvisible()
            if (it.size>0){
                adapter.refreshData(it)
                binding.txtNotFound.setInvisible()
            }else {
                binding.txtNotFound.setVisible()
            }

        }

        viewModel.wordMeaning.observe(viewLifecycleOwner){
           if (it.isNotBlank()){
               binding.wordMeaning.text =  Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
           }

        }

        viewModel.message.onEach {
            it.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }.observeInLifecycle(this)
    }

    override fun onItemClicked(item: String) {
       viewModel.insertGameCard(item,"",false)
    }


}