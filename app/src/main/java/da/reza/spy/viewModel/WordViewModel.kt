package da.reza.spy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.repository.WordRepository
import da.reza.spy.utiles.BaseResult
import da.reza.spy.utiles.ConstVal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordViewModel(private val repo:WordRepository):ViewModel() {



    private val _searchedImages = MutableLiveData<MutableList<ImageResult>>()
    val searchedImages: LiveData<MutableList<ImageResult>> = _searchedImages

    private val _suggestWord =MutableLiveData<MutableList<String>>()
    val suggestWord: LiveData<MutableList<String>> = _suggestWord

    private val _wordMeaning =MutableLiveData<String>()
    val wordMeaning: LiveData<String> = _wordMeaning

    private val _message = Channel<String>(Channel.BUFFERED)
    val message = _message.receiveAsFlow()

    val gameCards = repo.getAllGameCards()



    fun deleteGameCard(item: GameCardItem) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repo.deleteGameCard(item)
        }
    }

    private fun insertGameCardIntoDB(item: GameCardItem) = viewModelScope.launch {
        repo.insertGameCard(item)
        _message.send("کلمه اضافه شد")
    }

    fun insertGameCard(word: String, url: String, cardState: Boolean , id:Int?=null) = viewModelScope.launch {

        if (word.isEmpty()) {
            _message.send("Word must not be Empty")
            return@launch
        }
        if (word.length > ConstVal.MAX_NAME_LENGTH) {
            _message.send("Word is too Long")
            return@launch
        }

        val item = GameCardItem(id = id, cardName = word, cardImage = url, autoDelete = cardState)
        insertGameCardIntoDB(item)
    }

    fun searchForImages(str: String)  = viewModelScope.launch {


        if (str.matches("[a-zA-Z0-9]*".toRegex())) {
            val res = withContext(Dispatchers.IO){
                repo.searchImage(str)
            }
            if (res.status == BaseResult.Status.SUCCESS){
                res.data?.imageList?.let {
                    _searchedImages.value = it.toMutableList()
                }
            }else {
                _message.send("ای وای انگار کار نمیکنه")
            }
        }else {
            val trasnlate = withContext(Dispatchers.IO){
                repo.translateWord(str)
            }
            if (trasnlate.status == BaseResult.Status.ERROR){
                _message.send("ای وای انگار کار نمیکنه انگلیسی سرچ کن")
                return@launch
            }
            if (trasnlate.data == null){
                _message.send("ترجمه کلمه مشکل داره میتونی انگلیسی سرچ کنی؟")
                return@launch
            }
            if (trasnlate.data.response.code != 200){
                _message.send("مثل اینکه سرویس ترجمه کلمه قطع شده ! میتونی انگلیسی سرچ کنی")
                return@launch
            }
            if (trasnlate.data.data.NumberFound<=0 || trasnlate.data.data.results.isEmpty()){
                _message.send("نشد که ترجمه اش کنم برات سرچ کنم ، میشه انگلیسی سرچ کنی ؟")
                return@launch
            }


            val englishWord = trasnlate.data.data.results[0].word

            val res = withContext(Dispatchers.IO){
                repo.searchImage(englishWord)
            }
            if (res.status == BaseResult.Status.SUCCESS){
                res.data?.imageList?.let {
                    _searchedImages.value = it.toMutableList()
                }
            }else {
                _message.send("ای وای انگار کار نمیکنه")
            }
        }





    }

    fun suggestWord(str: String)  {

        viewModelScope.launch {
            val res = withContext(Dispatchers.IO){
                repo.suggestWord(str)
            }
            if (res.status == BaseResult.Status.SUCCESS){

                if (res.data != null){
                    if (res.data?.response?.code == 200){
                        val relatedWord = res.data.data.suggestion
                        _suggestWord.value = relatedWord.toMutableList()
                    }else{
                        _message.send("مثل اینکه سرویس واژه یاب قطعه")
                    }
                }else {
                    _message.send("چیزی پیدا نشد")
                }

            }else {
                _message.send("این سرویس خسته است! بعدا زور بزن")
            }
        }

    }

    fun wordMeaning(str: String)  {

        viewModelScope.launch {
            val res = withContext(Dispatchers.IO){
                repo.wordMeaning(str)
            }
            if (res.status == BaseResult.Status.SUCCESS){

                if (res.data != null){
                    if (res.data?.response?.code == 200){
                        _wordMeaning.value = res.data.data.text
                    }else{
                        _message.send("مثل اینکه سرویس واژه یاب قطعه")
                    }
                }else {
                    _message.send("چیزی پیدا نشد")
                }

            }else {
                _message.send("این سرویس خسته است! بعدا زور بزن")
            }
        }

    }

    fun changeAutoDeleteItem(gameCard:GameCardItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.changeGameCardAutoDelete(gameCard)
            }
        }
    }

    fun deleteAllWords(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.deleteAllWords()
            }
        }
    }



}