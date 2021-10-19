package da.reza.spy.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.remote.responses.ImageResult
import org.jetbrains.annotations.Nullable


class CustomDiffUtil<T>(private val OldList: MutableList<T>, private val newList: MutableList<T>) : DiffUtil.Callback() {



    override fun getOldListSize(): Int {
        return OldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return OldList[oldItemPosition] == newList[newItemPosition]
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