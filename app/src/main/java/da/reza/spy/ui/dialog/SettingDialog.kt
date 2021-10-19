package da.reza.spy.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import da.reza.spy.R
import da.reza.spy.data.model.GameValues
import da.reza.spy.databinding.AddWordDialogBinding
import da.reza.spy.databinding.SettingDialogBinding
import da.reza.spy.utiles.observeInLifecycle
import da.reza.spy.utiles.setVisible
import da.reza.spy.utiles.showToast
import da.reza.spy.viewModel.GameViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception
import java.lang.reflect.Executable

@KoinApiExtension
class SettingDialog:BottomSheetDialogFragment() , KoinComponent {


    private val viewModel:GameViewModel by inject()
    lateinit var binding: SettingDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<SettingDialogBinding>(inflater, R.layout.setting_bottom_sheet, container, false)
            .apply { binding = this }.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
        setStyle(STYLE_NORMAL, R.style.NewDialog)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.isMusicEnable.onEach {
            if (it){
                binding.iconMusic.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.ic_round_music_note_24,null))
            }else {
                binding.iconMusic.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.ic_round_music_off_24,null))
            }
        }.observeInLifecycle(this)
        viewModel.isSoundEnable.onEach {
            if (it){
                binding.iconSound.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.ic_round_music_note_24,null))
            }else {
                binding.iconSound.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.ic_round_music_off_24,null))
            }
        }.observeInLifecycle(this)




        binding.btnEmailUs.setOnClickListener {
            try {
                val emailIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "email_to"))
                emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("reza.da9@gmail.com"))
                requireActivity().startActivity(Intent.createChooser(emailIntent, "Send mail using..."))
            }catch (e:Exception){}
        }

        binding.btnMusicSwitch.setOnClickListener {
            viewModel.changeMusicSetting()
        }

        binding.btnSoundSwitch.setOnClickListener {
            viewModel.changeSoundSetting()
        }

        binding.btnGameDescription.setOnClickListener {
            findNavController().navigate(R.id.HomeToInformation)
            dismiss()
        }

    }



}