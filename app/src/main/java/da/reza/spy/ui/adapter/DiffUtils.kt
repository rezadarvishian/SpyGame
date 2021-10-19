package da.reza.spy.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import da.reza.spy.data.model.GameCardItem
import org.jetbrains.annotations.Nullable


class DiffUtils(private val OldList: MutableList<GameCardItem>, private val newList: MutableList<GameCardItem>) : DiffUtil.Callback() {



    override fun getOldListSize(): Int {
        return OldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return OldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return OldList[oldItemPosition] == newList[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }


}