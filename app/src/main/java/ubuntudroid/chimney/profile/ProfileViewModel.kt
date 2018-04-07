package ubuntudroid.chimney.profile

import android.arch.lifecycle.*
import io.stanwood.framework.loadingindicator.LoadingIndicatorViewModel
import ubuntudroid.chimney.data.Error
import ubuntudroid.chimney.data.Loading
import ubuntudroid.chimney.data.Success
import ubuntudroid.chimney.data.user.AccountManager
import ubuntudroid.chimney.data.steam.user.UserRepository

class ProfileViewModel(
        private val userRepository: UserRepository,
        private val accountManager: AccountManager
): ViewModel() {

    val userName: MutableLiveData<String> = MutableLiveData()
    private val player by lazy {
        userRepository.getPlayer(accountManager.getUserId())
    }

    val loadingIndicatorViewModel = LoadingIndicatorViewModel(object : LoadingIndicatorViewModel.ItemClickCallback {
        override fun onClick(trigger: LoadingIndicatorViewModel) {
            refresh()
        }
    })

    fun refresh() = userRepository.refreshPlayer(player)

    fun load(lifecycleOwner: LifecycleOwner) {
        player
                .observe(lifecycleOwner, Observer {
                    it?.let {
                        when (it.status) {
                            is Loading -> {
                                loadingIndicatorViewModel.loadingMessage = "Loading data..."
                                userName.postValue(null)
                            }
                            is Error -> {
                                loadingIndicatorViewModel.errorMessage = it.status.message
                                userName.postValue(null)
                            }
                            is Success -> {
                                loadingIndicatorViewModel.loadingMessage = null
                                userName.postValue(it.data?.personaName)
                            }
                        }
                    }
                })
    }
}