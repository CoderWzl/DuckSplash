package wzl.android.ducksplash.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import wzl.android.ducksplash.databinding.LayoutDrawerMenuBinding

/**
 *Created on 2/13/21
 *@author zhilin
 */
class DrawerMenu(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : FrameLayout(context, attrs, defStyleAttr) {

    var onUserClickListener: (() -> Unit)? = null
    var onSettingClickListener: (() -> Unit)? = null
    var onAboutClickListener: (() -> Unit)? = null

    private var viewBinding: LayoutDrawerMenuBinding = LayoutDrawerMenuBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context): this(context, null)

    init {
        viewBinding.userHead.setOnClickListener {
            onUserClickListener?.invoke()
        }
        viewBinding.userName.setOnClickListener {
            onUserClickListener?.invoke()
        }
        viewBinding.email.setOnClickListener {
            onUserClickListener?.invoke()
        }
        viewBinding.settings.setOnClickListener {
            onSettingClickListener?.invoke()
        }
        viewBinding.about.setOnClickListener {
            onAboutClickListener?.invoke()
        }

    }

}