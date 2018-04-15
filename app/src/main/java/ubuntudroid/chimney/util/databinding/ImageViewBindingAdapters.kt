package ubuntudroid.chimney.util.databinding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import ubuntudroid.chimney.util.glide.GlideApp

// TODO we possibly should provide both a Fragment? and an Activity? here to catch more use cases
class ImageViewBindingAdapters(private val activity: AppCompatActivity) {

    @BindingAdapter(value = ["android:src", "dontAnimate", "placeholder"], requireAll = false)
    fun loadImageFromString(imageView: ImageView, url: String?, dontAnimate: Boolean = true, placeholder: Drawable? = null) {
        if (url == null) {
            GlideApp.with(activity).apply {
                clear(imageView)
                load(placeholder)
                        .fitCenter()
                        .into(imageView)
            }
        } else {
            GlideApp.with(activity)
                    .load(url)
                    .fitCenter()
                    .apply {
                        if (dontAnimate) {
                            dontAnimate()
                        }
                        placeholder?.let { placeholder(it) }
                    }
                    .into(imageView)
        }
    }

}