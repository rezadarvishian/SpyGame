package da.reza.spy.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import da.reza.spy.R
import da.reza.spy.databinding.InformationBinding
import da.reza.spy.ui.base.BaseFragment
import java.io.IOException
import java.io.InputStream

class InformationFragment : BaseFragment<InformationBinding>(R.layout.fragment_information) {

    override fun onBackPress() { findNavController().navigateUp() }
    override fun adContainerID() = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtDescription.text =  Html.fromHtml(loadStringFromAsset(), Html.FROM_HTML_MODE_COMPACT)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun loadStringFromAsset(): String? {
        var tContents: String? = ""
        try {
            val stream: InputStream = requireActivity().assets.open("description.txt")
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            Toast.makeText(requireContext(), getString(R.string.file_stram_error), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        return tContents
    }

}
