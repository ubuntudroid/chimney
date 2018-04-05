package ubuntudroid.chimney.profile

import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

const val PROFILE_CONTEXT = "profile"

/**
 * we use an own module just for view models as they are lazily evaluated,
 * but should outlive activities
  */
val profileModule: Module = applicationContext {

    // ProfileViewModel
    viewModel {
        ProfileViewModel(get(), get())
    }

    context(PROFILE_CONTEXT) {

    }
}