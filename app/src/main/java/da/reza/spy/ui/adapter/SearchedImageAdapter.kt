package da.reza.spy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import da.reza.spy.R
import da.reza.spy.data.remote.responses.ImageResult
import da.reza.spy.databinding.SearchedItemModel
import da.reza.spy.ui.base.BindingViewHolder


class SearchedImageAdapter(val listener:OnSearchItemClickLister) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageList: MutableList<ImageResult> = mutableListOf()


    fun refreshData(items: MutableList<ImageResult>)  {
        val diffCallback = CustomDiffUtil(imageList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        imageList.clear()
        imageList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.searched_image_item_model, parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.binding.item = imageList[position]


            holder.itemView.setOnClickListener {
                listener.onItemClicked(imageList[position])
            }

        }
    }

    override fun getItemCount() = imageList.size


    private inner class ViewHolder(view: View) : BindingViewHolder<SearchedItemModel>(view)


    interface OnSearchItemClickLister{
        fun onItemClicked(item:ImageResult)
    }

}