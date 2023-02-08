package com.example.codelabandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.codelabandroid.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* So many code in fragment
*
* Use ListAdapter instead RecyclerView.Adapter
*
*  */


class MyFragment: Fragment()  {

    private val viewModel: MyViewModel by viewModel()

    private lateinit var adapter: MyAdapter

    private val onItemClicked: (itemModel: ItemModel) -> Unit = { itemModel ->
        // do something here when the item is clicked, like redirect to another activity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MyAdapter(onItemClicked)
        // set the adapter to your recycler view
        // observe changes of your state flow
        lifecycleScope.launchWhenCreated {
            viewModel.items.collectLatest { items ->
                // You will send the items to your adapter here
                adapter.submitList(items)
            }
        }
    }
}

class MyViewModel: ViewModel() {

    private val _sections = MutableLiveData<List<Section>>(emptyList())
    val sections: LiveData<List<Section>>
        get() = _sections

    private val _items = MutableStateFlow<List<ItemModel>>(emptyList())
    val items = _items.asStateFlow()

    init {
        val sectionsResult = mutableListOf<Section>()
        sectionsResult.addAll(requestSections())
        _sections.postValue(requestSections())
    }

    // implement a function that add a new item on rating list
//    fun addRating(newRate: String) {
//        val newList = mutableListOf<String>()
//        _rating.value?.let { newList.addAll(it) }
//        newList.add(newRate)
//        _rating.value = newList
//    }

    fun requestSections(): List<Section>  = emptyList()
}

data class Section(
    val id: Int,
    val name: String
)

data class ItemModel(
    val id: Int,
    val name: String
)


class MyAdapter(
    private val onItemClicked: (item: ItemModel) -> Unit
): ListAdapter<ItemModel, MyAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FragmentFirstBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(
        private val binding: FragmentFirstBinding,
        private val onItemClicked: (item: ItemModel) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemModel) {
            // Here you can get the item values to put this values on your view
            // Here you set the callback to a listener
            binding.root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemModel>() {
            override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
                // need a unique identifier to have sure they are the same item. could be a comparison of ids. In this case, that is just a list of strings just compare like this below
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
                // compare the objects
                return oldItem == newItem
            }

        }
    }
}
