package wzl.android.ducksplash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import wzl.android.ducksplash.databinding.ActivityMainBinding
import wzl.android.ducksplash.viewmodel.PhotoListViewModel

class MainActivity : AppCompatActivity() {

    // 懒加载
    private val viewBinding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setContentView(viewBinding.root)
        // 为导航添加监听，只有在导航到 MainFragment 的时候才可以展开 DrawerLayout 。
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainFragment) {
                viewBinding.drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_UNLOCKED,
                    viewBinding.secondLayout
                )
            } else {
                viewBinding.drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    viewBinding.secondLayout
                )
            }
        }
    }

    fun showMenu() {
        if (!viewBinding.drawerLayout.isDrawerOpen(viewBinding.secondLayout)) {
            viewBinding.drawerLayout.openDrawer(viewBinding.secondLayout)
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