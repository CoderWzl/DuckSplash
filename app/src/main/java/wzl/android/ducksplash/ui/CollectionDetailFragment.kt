package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoDiffCallback
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.api.createApiService
import wzl.android.ducksplash.databinding.FragmentCollectionDetailBinding
import wzl.android.ducksplash.repository.PhotoRepository
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.CollectionDetailViewModel
import wzl.android.ducksplash.viewmodel.CollectionDetailViewModelFactory

class CollectionDetailFragment : Fragment() {

    private lateinit var viewModel: CollectionDetailViewModel

    private lateinit var viewBinding: FragmentCollectionDetailBinding

    private val args: CollectionDetailFragmentArgs by navArgs()
    private val mAdapter = PhotoPagingAdapter(PhotoDiffCallback())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCollectionDetailBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            coordinatorLayout.reserveStatusBar()
            recyclerView.adapter = mAdapter.withLoadStateFooter(
                    footer = FooterLoadStateAdapter {
                        mAdapter.retry()
                    }
            )
            toolBar.title = args.title
            toolBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolBar.post {
                moreInfoContainer.apply {
                    setPadding(
                            paddingLeft,
                            toolBar.height + paddingTop,
                            paddingRight,
                            paddingBottom
                    )
                }
            }
            description.text = args.description
            otherDetails.text = requireContext().getString(R.string.photo_summary_user_info, args.totalPhotos, args.fullName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                CollectionDetailViewModelFactory(
                        PhotoRepository(
                                createApiService()
                        )
                )
        ).get(CollectionDetailViewModel::class.java)
        lifecycleScope.launch {
            viewModel.getCollectionPhoto(args.collectionId).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}