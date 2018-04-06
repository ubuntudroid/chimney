package ubuntudroid.chimney.profile

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import ubuntudroid.chimney.data.Error
import ubuntudroid.chimney.data.Loading
import ubuntudroid.chimney.data.Success
import ubuntudroid.chimney.data.user.AccountManager
import ubuntudroid.chimney.data.user.UserRepository

class ProfileViewModel(
        private val userRepository: UserRepository,
        private val accountManager: AccountManager
): ViewModel() {

    var userName: ObservableField<String> = ObservableField()

    // TODO integrate loading indicator/error library
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var errorMessage: ObservableField<String> = ObservableField()

    fun loadUser(lifecycleOwner: LifecycleOwner) {
        userRepository.getPlayerSummaries(accountManager.getUserId())
                .observe(lifecycleOwner, Observer {
                    it?.let {
                        when (it.status) {
                            is Loading -> {
                                isLoading.set(true)
                                errorMessage.set(null)
                                userName.set(null)
                            }
                            is Error -> {
                                isLoading.set(false)
                                errorMessage.set(it.status.message)
                                userName.set(null)
                            }
                            is Success -> {
                                isLoading.set(false)
                                errorMessage.set(null)
                                userName.set(it.data?.personaName)
                            }
                        }
                    }
                })
    }
}