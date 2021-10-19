package da.reza.spy.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import android.widget.Toast
import java.io.ByteArrayOutputStream
import android.provider.Settings
import androidx.core.view.drawToBitmap
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.canhub.cropper.PickImageContract
import da.reza.spy.BuildConfig
import da.reza.spy.R
import da.reza.spy.databinding.CropImageBinding
import da.reza.spy.ui.base.BindActivity
import da.reza.spy.ui.dialog.MaterialQuestionDialog



class ImageCropActivity : BindActivity<CropImageBinding>() ,
    CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener  {

    override fun getLayoutResId() = R.layout.activity_image_crop
    private var cropImageUri: Uri? = null
    private var settingState = false


    @RequiresApi(Build.VERSION_CODES.M)
    private val openPicker = registerForActivityResult(PickImageContract()) { imageUri ->
        if (imageUri != null && CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)
        ) {
            cropImageUri = imageUri
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
            )
        } else {
            binding.cropImageView.setImageUriAsync(imageUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.cropImageView.let {
            it.setOnSetImageUriCompleteListener(this)
            it.setOnCropImageCompleteListener(this)
        }
        openPicker()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnConfirm.setOnClickListener { binding.cropImageView.getCroppedImageAsync() }
        binding.btnOpenGallery.setOnClickListener { openPicker() }
        binding.btnRotate.setOnClickListener {  binding.cropImageView.rotateImage(90) }
        binding.btnFlip.setOnClickListener {  binding.cropImageView.flipImageVertically() }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun openPicker(){
        if (CropImage.isExplicitCameraPermissionRequired(this)) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE
            )
        } else openPicker.launch(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cropImageView.setOnSetImageUriCompleteListener(null)
        binding.cropImageView.setOnCropImageCompleteListener(null)
    }

    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception?) {
        if (error != null) {
            Log.e("AIC", "Failed to load image by URI", error)
            Toast.makeText(this, "خطا در انتخاب تصویر ، دوباره تلاش کنید ، فقط تصویر قابل بارگزاری است", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        handleCropResult(result)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("CropImage" , "permission:$requestCode")
        when(requestCode){
            CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPicker.launch(true)
                } else {
                    showAccessDeniedError()
                }
            }

            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE -> {
                if (cropImageUri != null &&
                    grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    binding.cropImageView.setImageUriAsync(cropImageUri)
                } else {
                    showAccessDeniedError()
                }
            }
        }

    }

    private fun handleCropResult(result: CropImageView.CropResult?) {
       try {
           if (result != null && result.error == null) {
               Log.v("File Path", result.getUriFilePath(this).toString())


               var imageBitmap = if (binding.cropImageView.cropShape == CropImageView.CropShape.OVAL)
                       result.bitmap?.let { CropImage.toOvalBitmap(it) }
                   else result.bitmap


               if (imageBitmap == null) imageBitmap = binding.cropImageView.drawToBitmap()

               var compress = 75
               val size = imageBitmap.byteCount
               if (size.toFloat() / 10000000 > 1.0) compress = 50
               if (size.toFloat() / 10000000 > 2.0) compress = 40
               if (size.toFloat() / 10000000 > 4.0) compress = 10
               val bos = ByteArrayOutputStream()
               imageBitmap.compress(Bitmap.CompressFormat.JPEG, compress, bos)
               val bytes = bos.toByteArray()

               if (bytes.size > 500000) {
                   Toast.makeText(this, "حجم عکس بیشتر از 5 مگابایت !! لطفا عکس را بیشتر برش دهید", Toast.LENGTH_SHORT).show()
               }else {
                   val i = Intent()
                   i.putExtra("bitmap" ,bytes )
                   setResult(Activity.RESULT_OK, i)
                   finish()
               }

           } else {
               Log.e("AIC", "Failed to crop image", result?.error)
               Toast.makeText(this, "خطا در انتخاب تصویر دوباره تلاش کنید", Toast.LENGTH_SHORT).show()
               finish()
           }
       }catch (e:Exception){
           Toast.makeText(this, "خطا در انتخاب تصویر دوباره تلاش کنید", Toast.LENGTH_SHORT).show()
           Log.e("AIC", "Failed to crop image", e)
           finish()
       }
    }

    private fun showAccessDeniedError(){
        MaterialQuestionDialog(this)
            .setTitle(getString(R.string.accessDeniedTitle))
            .setCancelTitle(getString(R.string.accessDeniedCancelTitle))
            .setConfirmTitle(getString(R.string.accessDeniedConfirmTitle))
            .setAnimationResId(R.raw.access_denied)
            .setOnCancel{
                finish()
            }
            .setOnConfirm{
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                intent.data = uri
                try {
                    settingState = true
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "مشکلی رخ داد",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()




    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}
