package wzl.android.ducksplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.FragmentUserBinding
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.reserveStatusBar
import java.util.ArrayList

/**
 *Created on 1/31/21
 *@author zhilin
 * 用户详情界面
 */
class UserFragment: Fragment() {

    private val args by navArgs<UserFragmentArgs>()
    private lateinit var viewBinding: FragmentUserBinding
    private var expanded = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentUserBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            root.reserveStatusBar()
            toolBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            val url = args.user?.profileImage?.large?:""
            userInfoLayout.userHead.loadCirclePhotoUrl(url, R.drawable.drawable_circle_image_placeholder)
            val userName = if (args.user == null) "" else "${args.user?.firstName} ${args.user?.lastName?:""}"
            userInfoLayout.userName.text = userName
            userInfoLayout.location.text = args.user?.location
            userInfoLayout.photoCount.text = "${args.user?.totalPhotos}"
            userInfoLayout.favoriteCount.text = "${args.user?.totalLikes}"
            userInfoLayout.collectionCount.text = "${args.user?.totalCollections}"
            userInfoLayout.description.text = args.user?.bio
            userInfoLayout.location.isVisible = args.user?.location != null
            userInfoLayout.description.isVisible = args.user?.bio != null
            toolBar.title = args.user?.username ?: ""
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                expanded = verticalOffset == 0
            })
            appBarLayout.setExpanded(expanded)
        }
        initViewPager()
    }

    private fun initViewPager() {
        args.user?.let {
            val titleRes: ArrayList<Int> = ArrayList()
            if (it.totalPhotos != 0) {
                titleRes.add(R.string.photo)
            }
            if (it.totalLikes != 0) {
                titleRes.add(R.string.favorite)
            }
            if (it.totalCollections != 0) {
                titleRes.add(R.string.collection)
            }
            viewBinding.apply {
                viewPager.adapter = UserViewPagerAdapter(
                    it.username,
                    titleRes,
                    this@UserFragment
                )
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = getString(titleRes[position])
                }.attach()
            }
        }
    }
}

private class UserViewPagerAdapter(
    val username: String,
    val titleRes: List<Int>,
    val fragment: Fragment
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return titleRes.size
    }

    override fun createFragment(position: Int): Fragment {
        return UserContentListFragment.newInstance(
            when(titleRes[position]) {
                R.string.photo -> UserPhotoFragment::class.java
                R.string.favorite -> UserFavoriteFragment::class.java
                R.string.collection -> UserCollectionFragment::class.java
                else -> throw IllegalArgumentException("unknown user content type")
            },
            username
        )
    }

}