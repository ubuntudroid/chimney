package ubuntudroid.chimney.profile

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import ubuntudroid.chimney.data.user.AccountManager
import ubuntudroid.chimney.data.user.UserRepository

class ProfileViewModel(
        private val userRepository: UserRepository,
        private val accountManager: AccountManager
): ViewModel() {

    var userName: ObservableField<String> = ObservableField()

    fun loadUser(lifecycleOwner: LifecycleOwner) {
        userRepository.getPlayerSummaries(accountManager.getUserId())
                .observe(lifecycleOwner, Observer {
                    userName.set(it?.value?.personaName)
                })
    }

    override fun onCleared() {
        super.onCleared()
    }
}