package da.reza.spy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.repository.PlayerScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerScoreViewModel(private val repo : PlayerScoreRepository):ViewModel() {



    val playScoreList:Flow<List<PlayerNames>>
    get() = repo.getPlayerScoreList()


    fun deletePlayer(player:PlayerNames) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.deletePlayer(player)
            }
        }
    }

}