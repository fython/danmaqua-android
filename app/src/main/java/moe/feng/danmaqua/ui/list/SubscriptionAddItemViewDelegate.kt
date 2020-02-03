package moe.feng.danmaqua.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import moe.feng.danmaqua.R

class SubscriptionAddItemViewDelegate(var callback: Callback? = null)
    : ItemViewDelegate<
        SubscriptionAddItemViewDelegate.Item,
        SubscriptionAddItemViewDelegate.ViewHolder>() {

    interface Callback {

        fun onSubscriptionAddClick()

    }

    object Item

    class ViewHolder(itemView: View) : BaseViewHolder(itemView)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.subscription_add_item_view, parent, false))
            .apply { itemView.setOnClickListener { callback?.onSubscriptionAddClick() } }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Item) {}

    override fun onViewRecycled(holder: ViewHolder) {
        holder.onRecycled()
    }

}