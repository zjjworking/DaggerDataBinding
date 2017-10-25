package com.zjj.daggerdatabinding.base

import android.databinding.DataBindingUtil.inflate
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter





/**
 * Created by zjj on 17/10/23.
 */
abstract class BaseDataBindingAdapter<B : ViewDataBinding,T>(layoutResId: Int,data:List<T>) : BaseQuickAdapter<T, DataBoundNewViewHolder<B>>(layoutResId,data) {
    var mListener: ((pos: Int) -> Unit)? = null

    override fun onBindViewHolder(holder: DataBoundNewViewHolder<B>, position: Int) {
        super.onBindViewHolder(holder, position)
//        holder.binding!!.root.setOnClickListener {
//            mListener?.invoke(holder.adapterPosition)
//        }
        holder.itemView.setOnClickListener {
            mListener?.invoke(holder.adapterPosition)
        }
    }

    override fun createBaseViewHolder(view: View): DataBoundNewViewHolder<B> {
        return DataBoundNewViewHolder(view)
    }
    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): DataBoundNewViewHolder<B> {

//        DataBoundNewViewHolder(inflate(mLayoutInflater, layoutResId, parent, false))
        val binding : B = inflate(mLayoutInflater, layoutResId, parent, false)
        val view: View
        if (binding == null) {
            view = getItemView(layoutResId, parent)
        } else {
            view = binding.getRoot()
        }
        val holder :DataBoundNewViewHolder<B>  = DataBoundNewViewHolder(view)
        holder.binding = binding
        return holder
    }


    fun setOnItemClickListener(listener: ((pos: Int) -> Unit)) {
        mListener = listener
    }

    override fun convert(helper: DataBoundNewViewHolder<B>, item: T) {
        convert(helper.binding!!,item)
        helper.binding!!.executePendingBindings()
    }

     abstract fun convert(binding: B, item: T)
}