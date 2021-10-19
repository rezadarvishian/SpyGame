package da.reza.spy.ui.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.databinding.GameCardItemModelBinding
import da.reza.spy.ui.base.BindingViewHolder
import da.reza.spy.utiles.setGone
import da.reza.spy.utiles.setVisible


class GameCardAdapter(private val listener:RecyclerViewInterface,

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var gameCardList: MutableList<GameCardItem> = mutableListOf()

    private val visitedImageList = listOf(R.drawable.nose,R.drawable.hat,R.drawable.zarebin , R.drawable.notebook_icon)


    fun refreshGameCard(items:MutableList<GameCardItem>){
        val diffCallback = CustomDiffUtil(gameCardList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        gameCardList.clear()
        gameCardList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GameCardViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.game_card_item_model, parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GameCardViewHolder) {

            if (position % 2 == 0){
                holder.binding.frontImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        holder.binding.visitedImage.context,R.drawable.detective_one
                    )
                )
            }else {
                holder.binding.frontImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        holder.binding.visitedImage.context,R.drawable.detective_two
                    )
                )
            }


            if (gameCardList[position].isEnable) {
                holder.binding.disableColor.setGone()
            }else {
                holder.binding.disableColor.setVisible()
                holder.binding.visitedImage.setImageDrawable(AppCompatResources.getDrawable( holder.binding.visitedImage.context,visitedImageList.random()))
            }

            if (gameCardList[position].playerName.isNotBlank()) {
                holder.binding.txtPlayerName.text = gameCardList[position].playerName
                holder.binding.txtPlayerName.setVisible()
            } else {
                holder.binding.txtPlayerName.setGone()

            }

            holder.binding.frontImageView.setOnClickListener {
                if (gameCardList[position].isEnable){
                    gameCardList[position].isEnable = false
                    notifyItemChanged(position)
                    listener.showDialog(gameCardList[position])
                }

            }

        }

        Log.i("test" ,gameCardList.toString() )

    }

    override fun getItemCount() = gameCardList.size


    private inner class GameCardViewHolder(view: View) : BindingViewHolder<GameCardItemModelBinding>(view)

    interface RecyclerViewInterface{
        fun showDialog(item:GameCardItem)
    }

}