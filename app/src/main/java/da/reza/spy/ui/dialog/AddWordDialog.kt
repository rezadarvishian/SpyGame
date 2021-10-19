package da.reza.spy.ui.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.*
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.databinding.AddWordDialogBinding
import da.reza.spy.ui.fragment.ImageCropActivity
import da.reza.spy.utiles.convertBitmapToBase64
import da.reza.spy.utiles.loadImage
import da.reza.spy.utiles.setVisible
import da.reza.spy.utiles.showToast
import da.reza.spy.viewModel.GameViewModel
import da.reza.spy.viewModel.WordViewModel
import kotlinx.android.synthetic.main.show_uamge_dialog.*
import kotlinx.android.synthetic.main.word_item_model.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddWordDialog(private var gameCardItem: GameCardItem? = null) : BottomSheetDialogFragment() {


    private val viewModel:WordViewModel by viewModel()

    lateinit var binding: AddWordDialogBinding
    private var gameCardId :Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<AddWordDialogBinding>(inflater, R.layout.add_word_dialog, container, false)
            .apply { binding = this }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (gameCardItem!=null){
            if (gameCardItem!!.cardImage.isNotBlank()){
                binding.imageView.loadImage(gameCardItem!!.cardImage)
                binding.imageLayout.setVisible()
                binding.txtAddImage.text = requireActivity().resources.getString(R.string.add_image_second)
            }
            binding.edtInputName.setText(gameCardItem!!.cardName)
            binding.autoDeleteSwitch.isChecked = gameCardItem!!.autoDelete
            gameCardId = gameCardItem!!.id
            binding.btnSave.text = requireActivity().resources.getString(R.string.submit_changes)
        }


        binding.btnSave.setOnClickListener {
            if (binding.edtInputName.text.isNullOrBlank()) {
                requireActivity().showToast("کلمه رو بنویس")
                return@setOnClickListener
            }
            var imageViewString = ""
            if (binding.imageView.drawable!=null){
                imageViewString= binding.imageView.drawToBitmap().convertBitmapToBase64()
            }
            viewModel.insertGameCard(
                binding.edtInputName.text?.trim().toString(),
                imageViewString,
                binding.autoDeleteSwitch.isChecked,
                gameCardId
            )
            dismiss()
        }


        binding.txtAddImage.setOnClickListener {
          startActivityForResult(Intent(requireActivity(), ImageCropActivity::class.java), 0)

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (data?.hasExtra("bitmap") == true) {
                    val bytes = data.extras?.getByteArray("bitmap")
                    Log.i("CropImage", "byteSize:${bytes?.size.toString()}")
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                    binding.imageView.setImageBitmap(bmp)
                    binding.imageLayout.setVisible()
                    binding.txtAddImage.text = requireActivity().resources.getString(R.string.add_image_second)
                }
            } catch (e: Exception) {
                Log.v("CropImage", "catch Exception${e}")
            }
        }
    }



}