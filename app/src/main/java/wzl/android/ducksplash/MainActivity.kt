package wzl.android.ducksplash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.databinding.ActivityMainBinding
import wzl.android.ducksplash.ui.UserFragmentDirections
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.util.toast
import wzl.android.ducksplash.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // 懒加载
    private lateinit var viewBinding : ActivityMainBinding
    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // 为导航添加监听，只有在导航到 MainFragment 的时候才可以展开 DrawerLayout 。
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainFragment) {
                viewBinding.drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_UNLOCKED,
                    viewBinding.menuLayout
                )
            } else {
                viewBinding.drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    viewBinding.menuLayout
                )
            }
        }
        mViewModel.loginPrefs.observe(this) {
            viewBinding.menuLayout.apply {
                if (it.profilePicture.isNullOrEmpty()) {
                    userHead.setImageResource(R.mipmap.ic_default_userprofile)
                } else {
                    userHead.loadCirclePhotoUrl(url = it.profilePicture)
                }
                userName.text = if (it.userName.isNullOrEmpty())
                    getString(R.string.user_login_tip) else it.userName
                email.text = if (it.email.isNullOrEmpty())
                    getString(R.string.user_login_email_placeholder) else it.email
                logoutButton.visibility = if (it.accessToken.isNullOrEmpty())
                    View.GONE else View.VISIBLE
            }
        }
        initDrawerMenu()
    }

    private fun initDrawerMenu() {
        viewBinding.menuLayout.apply {
            onUserClickListener = {
                if (mViewModel.isUserAuthorized()) {
                    // TODO: 2021/3/9 user main page
                    val prefs = mViewModel.loginPrefs.value
                    findNavController().navigateSafe(
                        UserFragmentDirections.actionGlobalUserFragmentWithUserName(userName = prefs?.userName)
                    )
                } else {
                    findNavController().navigateSafe(R.id.action_global_loginFragment)
                }
            }
            onSettingClickListener = {}
            onAboutClickListener = {}
            onLogoutClickListener = {
                mViewModel.logout()
            }
        }
    }

    fun showMenu() {
        if (!viewBinding.drawerLayout.isDrawerOpen(viewBinding.menuLayout)) {
            viewBinding.drawerLayout.openDrawer(viewBinding.menuLayout)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //ducksplash://unsplash-auth-callback?code=SARmNDBwDIHcBP17KqPzeMUFjxeEmLyWzTovUP3S69E
        Log.e("wzl", "onNewIntent: " + intent?.data)
        intent?.data?.let { uri ->
            if (uri.authority.equals(TokenProtoProvider.unsplashAuthCallback)) {
                uri.getQueryParameter("code")?.let { code->
                    mViewModel.unsplashAuthCallback(code)
                }
            }
        }
    }

    /**
     * https://developer.android.google.cn/guide/navigation/navigation-getting-started#kotlin
     * 导航到目的地片段中描述：
     * 使用 FragmentContainerView 创建 NavHostFragment，或通过 FragmentTransaction 手动将 NavHostFragment
     * 添加到您的 Activity 时，尝试通过 Navigation.findNavController(Activity, @IdRes int) 检索 Activity 的
     * onCreate() 中的 NavController 将失败。您应改为直接从 NavHostFragment 检索 NavController。
     */
    private fun findNavController() : NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHostFragment.navController
    }
}