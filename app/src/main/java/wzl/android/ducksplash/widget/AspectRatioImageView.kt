package wzl.android.ducksplash.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 *Created on 2021/1/13
 *@author zhilin
 */
class AspectRatioImageView(context: Context, attributes: AttributeSet?, defStyleAttr: Int) :
        AppCompatImageView(context, attributes, defStyleAttr) {

    var aspectRatio: Double = -1.0

    constructor(context: Context, attributes: AttributeSet?): this(context, attributes, 0)

    constructor(context: Context): this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (aspectRatio == -1.0) return

        val width = measuredWidth
        val height = (width * aspectRatio).toInt()
        if (height == measuredHeight) return
        setMeasuredDimension(width, height)
    }
}