package ubuntudroid.chimney.profile

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.releaseContext
import ubuntudroid.chimney.R
import ubuntudroid.chimney.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        viewModel.load(this)
    }

    override fun onPause() {
        releaseContext(PROFILE_CONTEXT)
        super.onPause()
    }
}
