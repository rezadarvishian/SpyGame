package da.reza.spy.ui.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindingViewHolder<out T : ViewDataBinding>(view: View) :
    RecyclerView.ViewHolder(view) {
    val binding: T = DataBindingUtil.bind(view)!!
}