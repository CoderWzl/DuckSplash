package wzl.android.ducksplash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import wzl.android.ducksplash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 懒加载
    private val viewBinding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}