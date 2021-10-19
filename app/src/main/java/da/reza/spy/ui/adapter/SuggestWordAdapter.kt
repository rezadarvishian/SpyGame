package da.reza.spy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.databinding.SuggestWordItemBinding
import da.reza.spy.ui.base.BindingViewHolder


class SuggestWordAdapter(val listener: OnSuggestWordListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val wordList: MutableList<String> = mutableListOf()


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(items: MutableList<String>)  {
        wordList.clear()
        wordList.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.suggest_word_item, parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.binding.item = wordList[position]


            holder.binding.btnAddWord.setOnClickListener {
                listener.onItemClicked(wordList[holder.adapterPosition])
                wordList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }

        }
    }

    override fun getItemCount() = wordList.size


    private inner class ViewHolder(view: View) : BindingViewHolder<SuggestWordItemBinding>(view)


    interface OnSuggestWordListener{
        fun onItemClicked(item:String)
    }

}