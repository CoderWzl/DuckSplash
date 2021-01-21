package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.LayoutPhotoLoadStateBinding

/**
 *Created on 2021/1/21
 *@author zhilin
 */
class PhotoLoadStateAdapter(val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder = LoadStateViewHolder(
        retry = retry,
        LayoutPhotoLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

}

class LoadStateViewHolder(val retry: () -> Unit, val viewBinding: LayoutPhotoLoadStateBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(state: LoadState) {
        when(state) {
            is LoadState.NotLoading -> {

            }
            is LoadState.Loading -> {

            }
            is LoadState.Error -> {

            }
        }
    }

}