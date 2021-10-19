package da.reza.spy.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import da.reza.spy.R
import da.reza.spy.databinding.HomeBinding
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.ui.dialog.MaterialQuestionDialog
import da.reza.spy.viewModel.GameViewModel
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import da.reza.spy.ui.dialog.SettingDialog
import da.reza.spy.utiles.animateInvisible
import da.reza.spy.utiles.animateVisible
import org.koin.core.component.KoinApiExtension
import kotlinx.coroutines.delay


class HomeFragment : BaseFragment<HomeBinding>(R.layout.fragment_home) {


    private val viewModel:GameViewModel by activityViewModels()
    override fun onBackPress() { openExitDialog() }
    override fun adContainerID() = binding.adContainer


    @KoinApiExtension
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterAnimation()
        binding.btnWordPage.setOnClickListener { findNavController().navigate(R.id.HomeToWordList) }
        binding.btnScoreList.setOnClickListener { findNavController().navigate(R.id.HomeToScoreList) }
        binding.btnStartGame.setOnClickListener { findNavController().navigate(R.id.HomeToStarter) }
        binding.btnSetting.setOnClickListener { SettingDialog().show(requireActivity().supportFragmentManager,"setting") }
    }

    private fun openExitDialog(){
        MaterialQuestionDialog(requireActivity())
            .setTitle(getString(R.string.exit_title))
            .setCancelTitle(getString(R.string.exit_dialog_cancel_button))
            .setConfirmTitle(getString(R.string.exit_dialog_confirm_button))
            .setAnimationResId(R.raw.close)
            .setOnCancel{}
            .setOnConfirm{requireActivity().finishAffinity()}
            .show()
    }

    private fun characterAnimation(){
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            var changeIcon = true
            repeat(Int.MAX_VALUE){
                delay(2000)
                binding.characterIcon.animateInvisible(binding.constraintLayout)
                delay(1000)
                changeIcon = if (changeIcon) {
                    binding.characterIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext() , R.drawable.trasnparent_detective_two
                        )
                    )
                    false
                }else {
                    binding.characterIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext() , R.drawable.detective
                        )
                    )
                    true
                }
                delay(1000)
                binding.characterIcon.animateVisible(binding.constraintLayout)
                delay(1500)
            }
        }
    }



}