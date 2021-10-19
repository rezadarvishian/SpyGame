package da.reza.spy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.databinding.PlayerNameItemBinding
import da.reza.spy.databinding.SuggestWordItemBinding
import da.reza.spy.ui.base.BindingViewHolder
import da.reza.spy.utiles.setInvisible
import da.reza.spy.utiles.setVisible


class GameStarterPlayerNameAdapter(private val isThisRoundPLayerNameAdapter:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private val playerName: MutableList<String> = mutableListOf()
    private var listener:OnPlayerNameClickListener? = null


    fun refreshData(items: MutableList<String>)  {
        val diffCallback = CustomDiffUtil(playerName, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        playerName.clear()
        playerName.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }


    fun setOnClickListener(listener:OnPlayerNameClickListener) {
        this.listener = listener
    }

    fun addNewPLayer(name:String):String?{
        return if (playerName.contains(name))" قبلا اضافه شده به لیست بالا"
        else {
            playerName.add(name)
            notifyItemChanged(playerName.indexOf(name))
             null
        }
    }

    fun getAllPlayerName() = playerName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_name_item, parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.binding.item = playerName[position]


            if (isThisRoundPLayerNameAdapter) {
                holder.binding.btnRemove.setVisible()
                holder.binding.btnAdd.setInvisible()
            }else {
                holder.binding.btnAdd.setVisible()
                holder.binding.btnRemove.setInvisible()
            }




            holder.itemView.setOnClickListener {
                if (isThisRoundPLayerNameAdapter){
                    playerName.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                }else {
                    listener?.onNameClicked(playerName[position])
                }
            }

        }
    }

    override fun getItemCount() = playerName.size


    private inner class ViewHolder(view: View) : BindingViewHolder<PlayerNameItemBinding>(view)


    interface OnPlayerNameClickListener{
        fun onNameClicked(name:String)
    }

}