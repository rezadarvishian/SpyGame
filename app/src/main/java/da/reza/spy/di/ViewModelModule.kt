package da.reza.spy.di

import da.reza.spy.viewModel.GameViewModel
import da.reza.spy.viewModel.PlayerScoreViewModel
import da.reza.spy.viewModel.SplashViewModel
import da.reza.spy.viewModel.WordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {


    viewModel { GameViewModel(get()) }
    viewModel { WordViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { PlayerScoreViewModel(get()) }

}