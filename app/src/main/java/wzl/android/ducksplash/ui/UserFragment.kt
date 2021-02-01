package wzl.android.ducksplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.FragmentUserBinding
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.reserveStatusBar

/**
 *Created on 1/31/21
 *@author zhilin
 * 用户详情界面
 */
class UserFragment: Fragment() {

    private val args by navArgs<UserFragmentArgs>()
    private lateinit var viewBinding: FragmentUserBinding

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
            appBarLayout.reserveStatusBar()
            val url = args.user?.profileImage?.large?:""
            userInfoLayout.userHead.loadCirclePhotoUrl(url, R.drawable.drawable_circle_image_placeholder)
            val userName = if (args.user == null) "" else "${args.user?.firstName} ${args.user?.lastName?:""}"
            userInfoLayout.userName.text = userName
            userInfoLayout.location.text = args.user?.location
            userInfoLayout.photoCount.text = "${args.user?.totalPhotos}"
            userInfoLayout.favoriteCount.text = "${args.user?.totalLikes}"
            userInfoLayout.collectionCount.text = "${args.user?.totalCollections}"
            toolBar.title = args.user?.username ?: ""
        }
    }
}