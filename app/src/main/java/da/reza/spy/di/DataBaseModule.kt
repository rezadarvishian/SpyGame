package da.reza.spy.di

import androidx.room.Room
import da.reza.spy.data.db.GameCardDataBase
import da.reza.spy.utiles.ConstVal.DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataBaseModule = module{


    single { Room
        .databaseBuilder(androidApplication() , GameCardDataBase::class.java , DATABASE_NAME)
        .build()
    }


    single(createdAtStart = true) { get<GameCardDataBase>().GameCardDao() }
    single(createdAtStart = true) { get<GameCardDataBase>().settingDao() }
    single(createdAtStart = true) { get<GameCardDataBase>().PlayerDao() }


}