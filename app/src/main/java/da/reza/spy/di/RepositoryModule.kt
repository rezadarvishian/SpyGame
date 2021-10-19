package da.reza.spy.di

import da.reza.spy.repository.GameCardRepository
import da.reza.spy.repository.PlayerScoreRepository
import da.reza.spy.repository.SplashScreenRepository
import da.reza.spy.repository.WordRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { GameCardRepository(get() , get() , get()) }
    single { WordRepository(get() , get()) }
    single { PlayerScoreRepository(get()) }
    single { SplashScreenRepository(get() , get()) }
}