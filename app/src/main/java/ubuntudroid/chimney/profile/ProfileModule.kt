package ubuntudroid.chimney.profile

import android.databinding.DataBindingComponent
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import ubuntudroid.chimney.util.databinding.ImageViewBindingAdapters

const val PROFILE_CONTEXT = "profile"

val profileModule: Module = applicationContext {

    // ProfileViewModel
    viewModel {
        ProfileViewModel(get(), get())
    }

    context(PROFILE_CONTEXT) {

        // DataBindingComponent
        bean {
            object : DataBindingComponent {
                override fun getImageViewBindingAdapters(): ImageViewBindingAdapters = get(parameters = {it.values})
            } as DataBindingComponent
        }

        // ImageViewBindingAdapters
        bean {
            ImageViewBindingAdapters(it["activity"])
        }

    }
}