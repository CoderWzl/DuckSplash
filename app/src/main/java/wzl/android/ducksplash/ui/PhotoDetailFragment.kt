package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.IMAGE_LARGE_SUFFIX
import wzl.android.ducksplash.IMAGE_THUMB_SUFFIX
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.CollectionDiffCallback
import wzl.android.ducksplash.adapter.PhotoDetailHeaderAdapter
import wzl.android.ducksplash.adapter.PhotoDetailRelatedCollectionsAdapter
import wzl.android.ducksplash.adapter.TagListAdapter
import wzl.android.ducksplash.databinding.FragmentPhotoDetailBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.NavMainViewModel
import wzl.android.ducksplash.viewmodel.PhotoDetailViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    private val args by navArgs<PhotoDetailFragmentArgs>()
    private lateinit var viewBinding: FragmentPhotoDetailBinding
    private lateinit var photoId: String
    private val viewModel by viewModels<PhotoDetailViewModel>()
    private var expanded = true

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
        viewModel.getPhoto(photoId)
        viewBinding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                expanded = verticalOffset == 0
            }
        )
        viewBinding.appBarLayout.setExpanded(expanded)
    }

    private fun observer() {
        viewModel.photo.observe(viewLifecycleOwner) {
            setupDetail(it)
            viewBinding.loadingContainer.isVisible = false
            viewBinding.recyclerView.isVisible = true
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            viewBinding.loadingContainer.isVisible = it
            viewBinding.recyclerView.isVisible = !it
            viewBinding.progressBar.isVisible = it
            viewBinding.errorTip.isVisible = false
        }
        viewModel.error.observe(viewLifecycleOwner) {
            viewBinding.loadingContainer.isVisible = true
            viewBinding.recyclerView.isVisible = false
            viewBinding.progressBar.isVisible = false
            viewBinding.errorTip.isVisible = true
            viewBinding.errorTip.text = it
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
        /*val vm by navGraphViewModels<NavMainViewModel>(R.id.nav_main)
        vm.sendPhotoIso(photo?.exif?.iso.toString())*/
        Log.d("zhilin", "setupDetail: ")
        photo?.let {
            val headerAdapter = PhotoDetailHeaderAdapter().apply {
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
                            it.coverPhoto.user.firstName + " " + it.coverPhoto.user.lastName
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

}