package wzl.android.ducksplash.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.IMAGE_LARGE_SUFFIX
import wzl.android.ducksplash.IMAGE_THUMB_SUFFIX
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.CollectionDiffCallback
import wzl.android.ducksplash.adapter.PhotoDetailHeaderAdapter
import wzl.android.ducksplash.adapter.PhotoDetailRelatedCollectionsAdapter
import wzl.android.ducksplash.api.ApiState
import wzl.android.ducksplash.databinding.FragmentPhotoDetailBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.ui.add.AddCollectionBottomSheet
import wzl.android.ducksplash.util.*
import wzl.android.ducksplash.viewmodel.PhotoDetailViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val DOWNLOADER = DOWNLOADER_SYSTEM
@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    private val args by navArgs<PhotoDetailFragmentArgs>()
    private lateinit var viewBinding: FragmentPhotoDetailBinding
    private lateinit var photoId: String
    private val viewModel by viewModels<PhotoDetailViewModel>()
    private var expanded = true
    @Inject lateinit var headerAdapter: PhotoDetailHeaderAdapter
    private val bottomSheetDialogFragment: AddCollectionBottomSheet by lazy {
        AddCollectionBottomSheet.newInstance(photoId, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.photo == null && args.photoId == null) {
            findNavController().popBackStack()
        }
        photoId = args.photoId ?: args.photo?.id!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentPhotoDetailBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.toolBar.reserveStatusBar()
        viewBinding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupPhoto(args.photo)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observer()
        Log.d("zhilin", "onActivityCreated: $viewModel")
        viewBinding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                expanded = verticalOffset == 0
            }
        )
        viewBinding.appBarLayout.setExpanded(expanded)
    }

    private fun observer() {
        viewModel.getPhoto(photoId).observe(viewLifecycleOwner) { state ->
            Log.d("zhilin", "observer: $state")
            when(state) {
                is ApiState.Loading -> {
                    viewBinding.loadingContainer.isVisible = true
                    viewBinding.recyclerView.isVisible = false
                    viewBinding.progressBar.isVisible = true
                    viewBinding.errorTip.isVisible = false
                }
                is ApiState.Error -> {
                    viewBinding.loadingContainer.isVisible = true
                    viewBinding.recyclerView.isVisible = false
                    viewBinding.progressBar.isVisible = false
                    viewBinding.errorTip.isVisible = true
                    viewBinding.errorTip.text = state.message
                }
                is ApiState.Success -> {
                    setupDetail(state.data)
                    viewBinding.loadingContainer.isVisible = false
                    viewBinding.recyclerView.isVisible = true
                }
            }
        }
        viewModel.currentUserCollections.observe(viewLifecycleOwner) { collections ->
            val collected: Boolean = collections != null && collections.isNotEmpty()
            headerAdapter.isCollected = collected
        }
    }

    private fun setupPhoto(photo: PhotoModel?) {
        photo?.let {
            val imageUrl = it.urls.raw + IMAGE_LARGE_SUFFIX
            val thumbUrl = it.urls.raw + IMAGE_THUMB_SUFFIX
            viewBinding.imageView.loadPhotoUrlWithThumbnail(imageUrl, thumbUrl, it.color)
        }
    }

    private fun setupDetail(photo: PhotoModel?) {
        Log.d("zhilin", "setupDetail: ")
        photo?.let {
            headerAdapter.apply {
                this.photo = photo
                onTagClickListener = {
                    findNavController().navigateSafe(
                        PhotoDetailFragmentDirections.actionPhotoDetailFragmentToSearchFragment(it)
                    )
                }
                onUserClickListener = {
                    findNavController().navigateSafe(
                        NavMainDirections.actionGlobalUserFragment(it)
                    )
                }
                onDownloadClickListener = {
                    requireContext().toast("download")
                    if (requireContext().fileExists(it.fileName, DOWNLOADER)) {
                        showFileExistsDialog(requireContext()) {
                            downloadPhoto(it)
                        }
                    } else {
                        downloadPhoto(it)
                    }
                }
                onFavoriteClickListener = {
                    if (viewModel.isUserAuthorized()) {
                        if (it.likedByUser == true) {
                            viewModel.unlikePhoto(it.id)
                        } else {
                            viewModel.likePhoto(it.id)
                        }
                        photo.likedByUser = photo.likedByUser?.not()
                        notifyDataSetChanged()
                    } else{
                        requireContext().toast("Please Login First")
                        findNavController().navigateSafe(R.id.action_global_loginFragment)
                    }
                }
                onBookmarkClickListener = {
                    if (viewModel.isUserAuthorized()) {
                        if (!bottomSheetDialogFragment.isAdded) {
                            bottomSheetDialogFragment.show(parentFragmentManager, "add_collection")
                        }
                    } else {
                        requireContext().toast("Please Login First")
                        findNavController().navigateSafe(R.id.action_global_loginFragment)
                    }
                }
            }
            val adapter = PhotoDetailRelatedCollectionsAdapter(CollectionDiffCallback())
                .apply {
                    submitList(photo.relatedCollections?.results)
                    onUserClickListener = {
                        findNavController().navigateSafe(
                            NavMainDirections.actionGlobalUserFragment(it)
                        )
                    }
                    itemClickListener = {
                        val fullName = if (it.coverPhoto?.user?.lastName == null) {
                            it.coverPhoto?.user?.firstName?:""
                        } else {
                            it.coverPhoto?.user?.firstName + " " + it.coverPhoto?.user?.lastName
                        }
                        findNavController().navigateSafe(
                            NavMainDirections.actionGlobalToCollectionDetailFragment(
                                it.id,
                                it.title,
                                it.totalPhotos?:0,
                                it.description,
                                fullName
                            )
                        )
                    }
                }

            val concatAdapter = ConcatAdapter()
            concatAdapter.addAdapter(headerAdapter)
            concatAdapter.addAdapter(adapter)
            viewBinding.recyclerView.adapter = concatAdapter
            viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun downloadPhoto(photo: PhotoModel) {
        if (requireContext().hasWritePermission()) {
            // TODO: 2021/4/13 download photo
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode = 0)
        }
    }

}