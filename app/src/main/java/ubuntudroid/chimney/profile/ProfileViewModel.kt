package ubuntudroid.chimney.profile

import android.arch.lifecycle.*
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.stanwood.framework.loadingindicator.LoadingIndicatorViewModel
import ubuntudroid.chimney.data.Error
import ubuntudroid.chimney.data.Loading
import ubuntudroid.chimney.data.Success
import ubuntudroid.chimney.data.user.AccountManager
import ubuntudroid.chimney.data.steam.user.UserRepository
import ubuntudroid.chimney.util.livedata.SingleLiveEvent

class ProfileViewModel(
        private val userRepository: UserRepository,
        private val accountManager: AccountManager
): ViewModel() {

    private var uiRefreshLiveData: SingleLiveEvent<Any>? = null

    var userName: MutableLiveData<String> = MutableLiveData()

    val loadingIndicatorViewModel = LoadingIndicatorViewModel(object : LoadingIndicatorViewModel.ItemClickCallback {
        override fun onClick(trigger: LoadingIndicatorViewModel) {
            // TODO I really don't like that we have to go through a livedata to actually call the
            // load method - there has to be a better way! Of course we could also expose this live data
            // to the outside (activity/fragment) and call load() from there, but this is supposedly
            // not ideal as well as a view model could also be used by multiple fragments which would
            // cause load() to be called multiple times or not at all (if we decide to only put it in
            // one fragment class which might not be attached at the point where the event is emitted)
            uiRefreshLiveData?.call()
        }
    })

    fun setupLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        uiRefreshLiveData = SingleLiveEvent<Any>().apply {
            observe(lifecycleOwner, Observer {
                load(lifecycleOwner)
            })
        }
        load(lifecycleOwner)
    }

    private fun load(lifecycleOwner: LifecycleOwner) {
        userRepository.getPlayerSummaries(accountManager.getUserId())
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