package da.reza.spy.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import da.reza.spy.R
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.databinding.SplashBinding
import da.reza.spy.utiles.animateInvisible
import da.reza.spy.utiles.animateVisible
import da.reza.spy.utiles.observeInLifecycle
import da.reza.spy.viewModel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel:SplashViewModel by viewModel()
    private var _binding:SplashBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        observeViewModel()

    }

    private fun observeViewModel(){
        viewModel.firstRun.onEach {
            if (it) {
                resources.getStringArray(R.array.gameWord).map {
                    viewModel.insertGameCard(it, "", false)
                }
                viewModel.setSetting(SettingEntity(isMusicEnable = true, isSoundEnable = true))
                viewModel.setFirstRun(false)
                characterAnimation()
            }else {
                characterAnimation()
            }
        }.observeInLifecycle(this)
    }


    private fun characterAnimation(){
        lifecycleScope.launchWhenResumed {
            delay(500)
            binding.characterIcon.animateInvisible(binding.constraintLayout)
            delay(1000)
            binding.characterIcon.setImageDrawable(AppCompatResources.getDrawable(this@SplashActivity , R.drawable.detective))
            binding.characterIcon.animateVisible(binding.constraintLayout)
            delay(1500)
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}