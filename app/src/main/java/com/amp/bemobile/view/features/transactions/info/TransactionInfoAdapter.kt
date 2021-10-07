package com.amp.bemobile.view.features.transactions.info

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amp.bemobile.databinding.ItemInfoTransactionBinding
import com.amp.bemobile.domain.features.transactions.models.TransactionView
import com.amp.bemobile.view.core.utils.viewBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class TransactionInfoAdapter(
    private val items: MutableList<TransactionView> = mutableListOf()
) : RecyclerView.Adapter<TransactionInfoAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(parent.viewBinding(ItemInfoTransactionBinding::inflate))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }


    fun populate(items: List<TransactionView>) {
        val diffResult = DiffUtil.calculateDiff(TransactionDiffCallback(this.items, items))

        this.items.clear()
        this.items.addAll(items)

        diffResult.dispatchUpdatesTo(this)
    }

    class TransactionViewHolder(private val itemBinding: ItemInfoTransactionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val decimalFormat = DecimalFormat("0.00")

        fun bind(item: TransactionView) {
            decimalFormat.roundingMode = RoundingMode.UP

            itemBinding.amount.text =
                "${decimalFormat.format(item.amount.toDouble())} ${item.currency}"
        }
    }

    class TransactionDiffCallback(
        private val oldItems: List<TransactionView>,
        private val newItems: List<TransactionView>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (oldItems[oldItemPosition].sku == newItems[newItemPosition].sku)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}