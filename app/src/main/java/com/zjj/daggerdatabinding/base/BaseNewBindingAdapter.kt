package com.zjj.daggerdatabinding.base

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.jude.easyrecyclerview.EasyRecyclerView

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator

/**
 * Created by zjj on 17/10/20.
 */

abstract class BaseNewBindingAdapter<B : ViewDataBinding,T> : RecyclerView.Adapter<DataBoundViewHolder<B>> {
    protected var mObjects: MutableList<T> = ArrayList()
    protected var mEventDelegate: EventDelegate? = null
    protected var headers = ArrayList<ItemView>()
    protected var footers = ArrayList<ItemView>()

    protected var mItemClickListener: OnItemClickListener? = null
    protected var mItemLongClickListener: OnItemLongClickListener? = null

    internal var mObserver: RecyclerView.AdapterDataObserver? = null

    interface ItemView {
        fun onCreateView(parent: ViewGroup): View
        fun onBindView(headerView: View)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    inner class GridSpanSizeLookup(private val mMaxCount: Int) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            if (headers.size != 0) {
                if (position < headers.size) return mMaxCount
            }
            if (footers.size != 0) {
                val i = position - headers.size - mObjects.size
                if (i >= 0) {
                    return mMaxCount
                }
            }
            return 1
        }
    }

    fun obtainGridSpanSizeLookUp(maxCount: Int): GridSpanSizeLookup {
        return GridSpanSizeLookup(maxCount)
    }

    /**
     * Lock used to modify the content of [.mObjects]. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private val mLock = Any()


    /**
     * Indicates whether or not [.notifyDataSetChanged] must be called whenever
     * [.mObjects] is modified.
     */
    private var mNotifyOnChange = true

    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    var context: Context? = null


    /**
     * Constructor
     *
     * @param context The current context.
     */
    constructor(context: Context) {
        init(context, ArrayList())
    }


    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    constructor(context: Context, objects: Array<T>) {
        init(context, Arrays.asList(*objects))
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    constructor(context: Context, objects: MutableList<T>) {
        init(context, objects)
    }


    private fun init(context: Context, objects: MutableList<T>) {
        this.context = context
        mObjects = objects
    }


    fun stopMore() {
        if (mEventDelegate == null) throw NullPointerException("You should invoking setLoadMore() first")
        mEventDelegate!!.stopLoadMore()
    }

    fun pauseMore() {
        if (mEventDelegate == null) throw NullPointerException("You should invoking setLoadMore() first")
        mEventDelegate!!.pauseLoadMore()
    }

    fun resumeMore() {
        if (mEventDelegate == null) throw NullPointerException("You should invoking setLoadMore() first")
        mEventDelegate!!.resumeLoadMore()
    }


    fun addHeader(view: ItemView?) {
        if (view == null) throw NullPointerException("ItemView can't be null")
        headers.add(view)
        notifyItemInserted(footers.size - 1)
    }

    fun addFooter(view: ItemView?) {
        if (view == null) throw NullPointerException("ItemView can't be null")
        footers.add(view)
        notifyItemInserted(headers.size + count + footers.size - 1)
    }

    fun removeAllHeader() {
        val count = headers.size
        headers.clear()
        notifyItemRangeRemoved(0, count)
    }

    fun removeAllFooter() {
        val count = footers.size
        footers.clear()
        notifyItemRangeRemoved(headers.size + count, count)
    }

    fun getHeader(index: Int): ItemView {
        return headers[index]
    }

    fun getFooter(index: Int): ItemView {
        return footers[index]
    }

    val headerCount: Int
        get() = headers.size

    val footerCount: Int
        get() = footers.size

    fun removeHeader(view: ItemView) {
        val position = headers.indexOf(view)
        headers.remove(view)
        notifyItemRemoved(position)
    }

    fun removeFooter(view: ItemView) {
        val position = headers.size + count + footers.indexOf(view)
        footers.remove(view)
        notifyItemRemoved(position)
    }


    internal val eventDelegate: EventDelegate
        get() {
            if (mEventDelegate == null) mEventDelegate = DefaultEventDelegate(this)
            return mEventDelegate as EventDelegate
        }

    fun setMore(res: Int, listener: OnLoadMoreListener): View {
        val container = FrameLayout(context!!)
        container.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(res, container)
        eventDelegate.setMore(container, listener)
        return container
    }

    fun setMore(view: View, listener: OnLoadMoreListener): View {
        eventDelegate.setMore(view, listener)
        return view
    }

    fun setNoMore(res: Int): View {
        val container = FrameLayout(context!!)
        container.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(res, container)
        eventDelegate.setNoMore(container)
        return container
    }

    fun setNoMore(view: View): View {
        eventDelegate.setNoMore(view)
        return view
    }

    fun setError(res: Int): View {
        val container = FrameLayout(context!!)
        container.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(res, container)
        eventDelegate.setErrorMore(container)
        return container
    }

    fun setError(view: View): View {
        eventDelegate.setErrorMore(view)
        return view
    }


    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        if (observer is EasyRecyclerView.EasyDataObserver) {
            mObserver = observer
        } else {
            super.registerAdapterDataObserver(observer)
        }
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    fun add(`object`: T?) {
        if (mEventDelegate != null) mEventDelegate!!.addData(if (`object` == null) 0 else 1)
        if (`object` != null) {
            synchronized(mLock) {
                mObjects.add(`object`)
            }
        }
        if (mObserver != null) mObserver!!.onItemRangeInserted(count + 1, 1)
        if (mNotifyOnChange) notifyItemInserted(headers.size + count + 1)
        log("add notifyItemInserted " + (headers.size + count + 1))
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    fun addAll(collection: Collection<T>?) {
        if (mEventDelegate != null) mEventDelegate!!.addData(collection?.size ?: 0)
        if (collection != null && collection.size != 0) {
            synchronized(mLock) {
                mObjects.addAll(collection)
            }
        }
        val dataCount = collection?.size ?: 0
        if (mObserver != null) mObserver!!.onItemRangeInserted(count - dataCount + 1, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(headers.size + count - dataCount + 1, dataCount)
        log("addAll notifyItemRangeInserted " + (headers.size + count - dataCount + 1) + "," + dataCount)

    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    fun addAll(items: Array<T>?) {
        if (mEventDelegate != null) mEventDelegate!!.addData(items?.size ?: 0)
        if (items != null && items.size != 0) {
            synchronized(mLock) {
                Collections.addAll(mObjects, *items)
            }
        }
        val dataCount = items?.size ?: 0
        if (mObserver != null) mObserver!!.onItemRangeInserted(count - dataCount + 1, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(headers.size + count - dataCount + 1, dataCount)
        log("addAll notifyItemRangeInserted " + ((headers.size + count - dataCount + 1).toString() + "," + dataCount))
    }

    /**
     * 插入，不会触发任何事情
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    fun insert(`object`: T, index: Int) {
        synchronized(mLock) {
            mObjects.add(index, `object`)
        }
        if (mObserver != null) mObserver!!.onItemRangeInserted(index, 1)
        if (mNotifyOnChange) notifyItemInserted(headers.size + index + 1)
        log("insert notifyItemRangeInserted " + (headers.size + index + 1))
    }

    /**
     * 插入数组，不会触发任何事情
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    fun insertAll(`object`: Array<T>?, index: Int) {
        synchronized(mLock) {
            mObjects.addAll(index, Arrays.asList(*`object`!!))
        }
        val dataCount = `object`?.size ?: 0
        if (mObserver != null) mObserver!!.onItemRangeInserted(index + 1, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(headers.size + index + 1, dataCount)
        log("insertAll notifyItemRangeInserted " + ((headers.size + index + 1).toString() + "," + dataCount))
    }

    /**
     * 插入数组，不会触发任何事情
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    fun insertAll(`object`: Collection<T>?, index: Int) {
        synchronized(mLock) {
            mObjects.addAll(index, `object`!!)
        }
        val dataCount = `object`?.size ?: 0
        if (mObserver != null) mObserver!!.onItemRangeInserted(index + 1, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(headers.size + index + 1, dataCount)
        log("insertAll notifyItemRangeInserted " + ((headers.size + index + 1).toString() + "," + dataCount))
    }

    /**
     * 删除，不会触发任何事情
     *
     * @param object The object to remove.
     */
    fun remove(`object`: T) {
        val position = mObjects.indexOf(`object`)
        synchronized(mLock) {
            if (mObjects.remove(`object`)) {
                if (mObserver != null) mObserver!!.onItemRangeRemoved(position, 1)
                if (mNotifyOnChange) notifyItemRemoved(headers.size + position)
                log("remove notifyItemRemoved " + (headers.size + position))
            }
        }
    }

    /**
     * 删除，不会触发任何事情
     *
     * @param position The position of the object to remove.
     */
    fun remove(position: Int) {
        synchronized(mLock) {
            mObjects.removeAt(position)
        }
        if (mObserver != null) mObserver!!.onItemRangeRemoved(position, 1)
        if (mNotifyOnChange) notifyItemRemoved(headers.size + position)
        log("remove notifyItemRemoved " + (headers.size + position))
    }


    /**
     * 触发清空
     */
    fun clear() {
        val count = mObjects.size
        if (mEventDelegate != null) mEventDelegate!!.clear()
        synchronized(mLock) {
            mObjects.clear()
        }
        if (mObserver != null) mObserver!!.onItemRangeRemoved(0, count)
        if (mNotifyOnChange) notifyItemRangeRemoved(headers.size, count)
        log("clear notifyItemRangeRemoved " + headers.size + "," + count)
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     * in this adapter.
     */
    fun sort(comparator: Comparator<in T>) {
        synchronized(mLock) {
            Collections.sort(mObjects, comparator)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }


    /**
     * Control whether methods that change the list ([.add],
     * [.insert], [.remove], [.clear]) automatically call
     * [.notifyDataSetChanged].  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     * automatically call [                       ][.notifyDataSetChanged]
     */
    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    /**
     * 这个函数包含了头部和尾部view的个数，不是真正的item个数。
     * @return
     */
    @Deprecated("")
    override fun getItemCount(): Int {
        return mObjects.size + headers.size + footers.size
    }

    /**
     * 应该使用这个获取item个数
     * @return
     */
    val count: Int
        get() = mObjects.size

    private fun createSpViewByType(parent: ViewGroup, viewType: Int): View? {
        for (headerView in headers) {
            if (headerView.hashCode() == viewType) {
                val view = headerView.onCreateView(parent)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams
                if (view.layoutParams != null)
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
                else
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.isFullSpan = true
                view.layoutParams = layoutParams
                return view
            }
        }
        for (footerview in footers) {
            if (footerview.hashCode() == viewType) {
                val view = footerview.onCreateView(parent)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams
                if (view.layoutParams != null)
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
                else
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.isFullSpan = true
                view.layoutParams = layoutParams
                return view
            }
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<B> {
        val view = createSpViewByType(parent, viewType)

        val viewHolder = OnCreateViewHolder(parent, viewType)

        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(viewHolder.adapterPosition - headers.size) }
        }

        if (mItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(viewHolder.adapterPosition - headers.size) }
        }
        return viewHolder
    }

    abstract fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<B>


    override fun onBindViewHolder(holder: DataBoundViewHolder<B>, position: Int) {
        holder.itemView.id = position
        if (headers.size != 0 && position < headers.size) {
            headers[position].onBindView(holder.itemView)
            return
        }

        val i = position - headers.size - mObjects.size
        if (footers.size != 0 && i >= 0) {
            footers[i].onBindView(holder.itemView)
            return
        }
        OnBindViewHolder(holder, position - headers.size)
    }


    fun OnBindViewHolder(holder: DataBoundViewHolder<B>, position: Int) {
        //        holder.setData(getItem(position));
    }


    @Deprecated("")
    override fun getItemViewType(position: Int): Int {
        if (headers.size != 0) {
            if (position < headers.size) return headers[position].hashCode()
        }
        if (footers.size != 0) {
            /*
            eg:
            0:header1
            1:header2   2
            2:object1
            3:object2
            4:object3
            5:object4
            6:footer1   6(position) - 2 - 4 = 0
            7:footer2
             */
            val i = position - headers.size - mObjects.size
            if (i >= 0) {
                return footers[i].hashCode()
            }
        }
        return getViewType(position - headers.size)
    }

    fun getViewType(position: Int): Int {
        return 0
    }


    val allData: List<T>
        get() = ArrayList(mObjects)

    /**
     * {@inheritDoc}
     */
    fun getItem(position: Int): T {
        return mObjects[position]
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    fun getPosition(item: T): Int {
        return mObjects.indexOf(item)
    }

    /**
     * {@inheritDoc}
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int): Boolean
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.mItemLongClickListener = listener
    }

    private fun log(content: String) {
        if (EasyRecyclerView.DEBUG) {
            Log.i(EasyRecyclerView.TAG, content)
        }
    }
}
