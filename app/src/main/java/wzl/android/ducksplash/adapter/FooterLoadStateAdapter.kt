package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.LayoutPhotoLoadStateBinding

/**
 *Created on 2021/1/21
 *@author zhilin
 */
class FooterLoadStateAdapter(val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {

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

    var error = false

    init {
        viewBinding.root.setOnClickListener {
            if (error) {
                retry.invoke()
            }
        }
    }

    fun bind(state: LoadState) {
        error = state is LoadState.Error
        viewBinding.loadingTip.isVisible = state !is LoadState.NotLoading
        viewBinding.progressBar.isVisible = state is LoadState.Loading
        if (state is LoadState.Error) {
            viewBinding.loadingTip.text = state.error.localizedMessage
        } else if (state is LoadState.Loading) {
            viewBinding.loadingTip.text = viewBinding.root.context.getString(R.string.loading)
        }
    }

}