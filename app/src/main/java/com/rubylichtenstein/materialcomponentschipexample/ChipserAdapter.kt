package com.rubylichtenstein.materialcomponentschipexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rv_item.*


class ChipserAdapter :
    RecyclerView.Adapter<ChipserAdapter.ChipserViewHolder>() {

    var onItemClickListener: ((String) -> Unit)? = null

    private val mSortedList =
        SortedList<String>(String::class.java, object : SortedListAdapterCallback<String>(this) {
            override fun compare(a: String, b: String): Int {
                return a.compareTo(b)
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: String, item2: String): Boolean {
                return item1 === item2
            }
        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipserViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return ChipserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipserViewHolder, position: Int) {
        val model = mSortedList.get(position)
        holder.bind(model)
    }

    fun add(model: String) {
        mSortedList.add(model)
    }

    fun remove(model: String) {
        mSortedList.remove(model)
    }

    fun clear() {
        mSortedList.clear()
    }

    fun add(models: List<String>) {
        mSortedList.addAll(models)
    }

    fun remove(models: List<String>) {
        mSortedList.beginBatchedUpdates()
        for (model in models) {
            mSortedList.remove(model)
        }
        mSortedList.endBatchedUpdates()
    }

    fun replaceAll(models: List<String>) {
        mSortedList.beginBatchedUpdates()
        for (i in mSortedList.size() - 1 downTo 0) {
            val model = mSortedList.get(i)
            if (!models.contains(model)) {
                mSortedList.remove(model)
            }
        }
        mSortedList.addAll(models)
        mSortedList.endBatchedUpdates()
    }

    override fun getItemCount(): Int {
        return mSortedList.size()
    }

    inner class ChipserViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: String) {
            text.text = item
            text.setOnClickListener {
                onItemClickListener?.invoke(text.text.toString())
            }
        }
    }
}
