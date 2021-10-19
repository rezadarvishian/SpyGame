package da.reza.spy.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.perf.metrics.AddTrace
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.databinding.ImageSearchBinding
import da.reza.spy.ui.adapter.SearchedImageAdapter
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.ui.dialog.AddWordDialog
import da.reza.spy.utiles.*
import da.reza.spy.viewModel.WordViewModel
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class ImageSearchFragment : BaseFragment<ImageSearchBinding>(R.layout.fragment_image_search) , SearchedImageAdapter.OnSearchItemClickLister {


    private val viewModel : WordViewModel by viewModel()
    override fun onBackPress() { findNavController().navigateUp() }
    override fun adContainerID() = binding.adContainer
    private val adapter = SearchedImageAdapter(this)


    @AddTrace(name = "ImageSearchTrace", enabled = true)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.imageRecycler.adapter = adapter
        binding.edtInputSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.progressBar.setVisible()
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    val keyWord = binding.edtInputSearch.text?.trim()
                    if (!keyWord.isNullOrBlank()){
                        viewModel.searchForImages(keyWord.toString())
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

    private fun observeViewModel(){
        viewModel.searchedImages.observe(viewLifecycleOwner){
            binding.progressBar.setInvisible()
            if (it.size>0){
                adapter.refreshData(it)
                binding.txtNotFound.setInvisible()
                binding.imageRecycler.setVisible()
            }else {
                binding.imageRecycler.setInvisible()
                binding.txtNotFound.setVisible()
            }

        }

        viewModel.message.onEach {
            requireActivity().showToast(it)
            binding.progressBar.setInvisible()
        }.observeInLifecycle(this)
    }

    override fun onItemClicked(item: ImageResult) {
        val saveItem = GameCardItem(
            cardName = binding.edtInputSearch.text?.trim().toString(),
            cardImage = item.largeImageURL,
        )
        AddWordDialog(saveItem).show(requireActivity().supportFragmentManager,"add_word")

    }


}