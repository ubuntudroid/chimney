package ubuntudroid.chimney.util.databinding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import ubuntudroid.chimney.util.glide.GlideApp

@BindingAdapter(value = ["android:src", "dontAnimate", "placeholder"], requireAll = false)
fun loadImageFromString(imageView: ImageView, url: String?, dontAnimate: Boolean = true, placeholder: Drawable? = null) {
    /*
     TODO: according to Glide documentation using the View is inefficient for complex hierarchies,
     but using the context would either use the potentially leaking with(Context) method which just
     observes the application to auto-cancel requests, or - if we cast the Context to Activity
     which in most cases (except for RemoteViews etc.) would succeed - ignore the Fragment lifecycle.
     So even if the latter would be better the most optimized in terms of not wasting resources after
     the request has been issued is the with(View) variant as it also respects Fragment lifecycles.
     However the determination of the parent instance (Fragment vs. Activity vs. Application) can be
     costly especially in complex hierarchies as the documentation states.

     For now we're using the View variant with care, but will probably introduce different optimized
     variants later on.
      */
    if (url == null) {
        GlideApp.with(imageView).apply {
            clear(imageView)
            load(placeholder)
                    .fitCenter()
                    .into(imageView)
        }
    } else {
        GlideApp.with(imageView)
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