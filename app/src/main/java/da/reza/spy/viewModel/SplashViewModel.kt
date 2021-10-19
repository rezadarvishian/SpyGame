package da.reza.spy.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.repository.SplashScreenRepository
import da.reza.spy.utiles.ConstVal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(private val repo:SplashScreenRepository):ViewModel() {

    private val _firstRun = Channel<Boolean>(Channel.BUFFERED)
    val firstRun = _firstRun.receiveAsFlow()

    init {
       viewModelScope.launch {
           _firstRun.send(repo.isFirstRun())
       }
    }


    fun insertGameCard(word: String, url: String, cardState: Boolean , id:Int?=null) = viewModelScope.launch {
        val item = GameCardItem(id = id, cardName = word, cardImage = url, autoDelete = cardState)
        repo.insertGameCard(item)
    }

    fun setSetting(item: SettingEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.setSetting(item)
            }
        }
    }

}