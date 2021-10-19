package da.reza.spy.repository

import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.data.remote.responses.ImageResponse
import da.reza.spy.data.remote.responses.VazheYabResponse
import da.reza.spy.utiles.BaseResult
import kotlinx.coroutines.flow.Flow

interface GameCardRepositoryInterface  {


    fun isMusicEnable() :Flow<Boolean>

    fun isSoundEnable() :Flow<Boolean>


}