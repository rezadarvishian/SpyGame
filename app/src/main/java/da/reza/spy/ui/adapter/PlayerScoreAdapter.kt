package da.reza.spy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.databinding.PlayerScoreItemBinding
import da.reza.spy.databinding.SuggestWordItemBinding
import da.reza.spy.ui.base.BindingViewHolder


class PlayerScoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val playerList: MutableList<PlayerNames> = mutableListOf()


    fun refreshData(items: MutableList<PlayerNames>)  {
        val diffCallback = CustomDiffUtil(playerList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        playerList.clear()
        playerList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun searchInList(word: String) = apply {
        val filteredList = playerList.filter {
            it.name.contains(word, true)
        }.toMutableList()
        refreshData(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_score_item, parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.binding.progress.progress = playerList[position].winPercent
            holder.binding.userName.text = playerList[position].name
            holder.binding.txtPlayCount.text = playerList[position].spyCounter.toString()
            holder.binding.txtWinnerCount.text = playerList[position].WinCounter.toString()

        }
    }

    override fun getItemCount() = playerList.size


    private inner class ViewHolder(view: View) : BindingViewHolder<PlayerScoreItemBinding>(view)




}