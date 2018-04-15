package ubuntudroid.chimney.profile

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.releaseContext
import org.koin.dsl.context.emptyParameters
import ubuntudroid.chimney.R
import ubuntudroid.chimney.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    // TODO later on we're going to use a Fragment here, but for now an Activity suffices

    private val viewModel by viewModel<ProfileViewModel>()

    /*
     TODO this works for now, but we need to provide a name to the DataBindingComponent when injecting
     to distinguish from other components for different Activites/Fragments. Unfortunately as of
     Koin 0.9.2-alpha-6 named injects seem to be broken, so we need to wait for the next alpha update.
      */
    private val dataBindingComponent: DataBindingComponent by inject { mapOf("activity" to this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile, dataBindingComponent)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        viewModel.load(this)
    }

    override fun onPause() {
        releaseContext(PROFILE_CONTEXT)
        super.onPause()
    }
}
