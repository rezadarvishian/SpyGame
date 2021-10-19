package da.reza.spy.viewModel

import android.util.Patterns
import android.webkit.URLUtil
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.GameValues
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.repository.GameCardRepository
import da.reza.spy.repository.GameCardRepositoryInterface
import da.reza.spy.utiles.BaseResult
import da.reza.spy.utiles.ConstVal
import da.reza.spy.utiles.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Attributes
import kotlin.random.Random

class GameViewModel(private val repositoryInterface: GameCardRepository) : ViewModel() {



    private val _playerNumber = MutableLiveData<Int>(3)
    val playerNumber: LiveData<Int> = _playerNumber

    private val _spyNumber = MutableLiveData<Int>(1)
    val spyNumber: LiveData<Int> = _spyNumber

    private val _timerAmount = MutableLiveData<Int>(3)
    val timerAmount: LiveData<Int> = _timerAmount

    private val _playWithNames = MutableLiveData<Boolean>()
    val playWithNames: LiveData<Boolean> = _playWithNames

    private val _thisRoundPlayerNames = MutableLiveData<MutableList<String>>()
    val thisRoundPlayerNames: LiveData<MutableList<String>> = _thisRoundPlayerNames

    private val _gameCardForPlay = MutableLiveData<MutableList<GameCardItem>>()
    val gameCardForPlay :LiveData<MutableList<GameCardItem>> = _gameCardForPlay

    private val _startGame = Channel<Boolean>(Channel.BUFFERED)
    val startGame = _startGame.receiveAsFlow()

    private val _message = Channel<String>(Channel.BUFFERED)
    val message = _message.receiveAsFlow()

    private val _addPlayerState = Channel<String>(Channel.BUFFERED)
    val addPlayerState = _addPlayerState.receiveAsFlow()

    val allPlayerNames = repositoryInterface.allPlayerNames()

    val isMusicEnable = repositoryInterface.isMusicEnable()

    val isSoundEnable = repositoryInterface.isSoundEnable()



    fun changePLayerCount(count:Int){
        val playerNumber = (_playerNumber.value?:3)+count
        if (playerNumber<=2) return
        if (playerNumber<=4)  _spyNumber.value = 1
        _playerNumber.value = playerNumber
    }

    fun changeSpyCounter(count:Int) = viewModelScope.launch{
        val spyNumber = (_spyNumber.value?:1)+count
        val playerNumber = (_playerNumber.value?:3)
        if (spyNumber<=1) {
            return@launch
        }
        if (spyNumber+3>=playerNumber) {
            _message.send("تعداد بازیکن ها باید حداقل 4 نفر بیشتر از جاسوس ها باشه")
            return@launch
        }
        _spyNumber.value = spyNumber
    }

    fun changeTimer(amount:Int){
        val time = (_timerAmount.value?:1)+amount
        if (time<=3) return
        _timerAmount.value = time
    }

    fun changePlayWithNameState(state:Boolean){
       _playWithNames.value = state
    }

    fun startGame() = viewModelScope.launch {

        if(getWordListSize() <= 0) {
            _message.send("لیست کلمه ها رو ترکوندی ، نمیشه بازی کرد که! برو کلمه اضافه کن")
            return@launch
        }

        if (_playWithNames.value == true) {

            if (_thisRoundPlayerNames.value?.size ==0){
                _message.send("به همون تعداد بازیکن ها باید اسم وارد کنی")
                return@launch
            }

            val playersNameSize = _thisRoundPlayerNames.value!!.size
            if (playersNameSize !=  _playerNumber.value!!){
                _message.send("به همون تعداد بازیکن ها باید اسم وارد کنی")
                return@launch
            }
        }

        withContext(Dispatchers.IO) {
            val randomGameCard = repositoryInterface.getRandomGameCard()


            val gameCardList = mutableListOf<GameCardItem>()
            val allPlayerCount =  _playerNumber.value!! - _spyNumber.value!!

            repeat(allPlayerCount) {
                gameCardList.add(
                    GameCardItem(
                        id = it,
                        cardName = randomGameCard.cardName,
                        cardImage = randomGameCard.cardImage,
                        autoDelete = randomGameCard.autoDelete,
                        isEnable = randomGameCard.isEnable
                    )
                )
            }
            repeat(_spyNumber.value!!) {
                gameCardList.add(
                    GameCardItem(
                        id = allPlayerCount + it,
                        cardName = "شما جاسوسی!",
                        cardImage = "",
                        autoDelete = false,
                        isEnable = true,
                        isSpy = true
                    )
                )
            }

            if (_playWithNames.value == true){
                val playerName = _thisRoundPlayerNames.value!!
                playerName.shuffle()
                gameCardList.shuffle()
               repeat(gameCardList.size){
                   gameCardList[it].playerName = playerName[it]
               }
            }

            gameCardList.shuffle()
            gameCardList[0].isStarterPlayer = true
            _startGame.send(true)
            _gameCardForPlay.postValue(gameCardList)
        }
    }

    fun setPlayerNames(names:List<String>){
        _thisRoundPlayerNames.value = names.toMutableList()
    }

    suspend fun getWordListSize():Int {
        var size = 0
        viewModelScope.async {
            withContext(Dispatchers.IO){
                size = repositoryInterface.getWordListSize()
            }
        }.await()
        return size
    }

    fun changeMusicSetting() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repositoryInterface.changeMusicSetting()
            }
        }
    }

    fun addPlayerName(name:String) = viewModelScope.launch{

        val player = PlayerNames(name = name)
        val res = withContext(Dispatchers.IO){ repositoryInterface.addPlayer(player) }
        if (res.isNotBlank()){
            _message.send(res)
        }else {
            _addPlayerState.send(name)
        }

    }

    fun changeSoundSetting() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repositoryInterface.changeSoundSetting()
            }
        }
    }


    fun addSpyPlayerWinCounter(name:String)  = viewModelScope.launch{
        withContext(Dispatchers.IO){
            repositoryInterface.addWinCounterForSpyPlayer(name)
        }
    }
    fun addPlayerPlayCounter(name:String)  = viewModelScope.launch{
        withContext(Dispatchers.IO){
            repositoryInterface.addPlayCounterForPlayer(name)
        }
    }



}