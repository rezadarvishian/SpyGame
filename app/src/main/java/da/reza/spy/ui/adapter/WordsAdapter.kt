package da.reza.spy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.databinding.WordItemModelBinding
import da.reza.spy.ui.base.BindingViewHolder
import da.reza.spy.viewModel.GameViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import da.reza.spy.viewModel.WordViewModel


@SuppressLint("NotifyDataSetChanged")
class WordsAdapter(private val viewModel :WordViewModel , val listener :OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val wordList: MutableList<GameCardItem> = mutableListOf()


    fun refreshData(items: MutableList<GameCardItem>)  {
        val diffCallback = DiffUtils(wordList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        wordList.clear()
        wordList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }


    fun searchInList(word: String) = apply {
        val filteredList = wordList.filter {
            it.cardName.contains(word, true)
        }.toMutableList()
        refreshData(filteredList)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        WordViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.word_item_model, parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WordViewHolder) {
            holder.binding.item = wordList[position]


            holder.binding.autoDeleteSwitch.setOnClickListener {
               try {
                   val card = wordList[position]
                   card.autoDelete = holder.binding.autoDeleteSwitch.isChecked
                   viewModel.changeAutoDeleteItem(card)
               }catch (e:Exception){

               }
            }


            holder.binding.cardImage.setOnClickListener {
                listener.onItemClick(wordList[position])
            }

        }
    }

    override fun getItemCount() = wordList.size


    private inner class WordViewHolder(view: View) : BindingViewHolder<WordItemModelBinding>(view)


    interface OnItemClickListener{
        fun onItemClick(item:GameCardItem)
    }

}