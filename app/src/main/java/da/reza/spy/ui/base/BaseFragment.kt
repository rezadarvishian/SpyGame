package da.reza.spy.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import da.reza.spy.databinding.FinishGameBinding
import da.reza.spy.utiles.RequestTapsellAd
import org.koin.android.ext.android.get


abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val resId:Int)  : Fragment() {

    private var adRequest: RequestTapsellAd? = null

    abstract fun adContainerID():ViewGroup?

    abstract fun onBackPress()

    private var _binding: T? = null
    val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<T>(inflater, resId, container, false)
            .apply { _binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestAd()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPress()
                }
            })
    }

    private fun requestAd(){
        adRequest = RequestTapsellAd()
            .setActivity(requireActivity())
            .setAdViewGroup(adContainerID())
            .setScreenName(resId.toString())
            .build()
    }

    override fun onDestroyView() {
        adRequest?.destroyNativeBanner()
        _binding = null
        super.onDestroyView()
    }


}