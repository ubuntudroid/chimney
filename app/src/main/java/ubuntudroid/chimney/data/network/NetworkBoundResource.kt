package ubuntudroid.chimney.data.network


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.AsyncTask
import ubuntudroid.chimney.data.*

/*
 TODO move this class as well as Resource, SingleLiveEvent and ViewLifecycleFragment to LiveDataExtensions library.
 NetworkBoundResource and Resource could as well be moved to stanwood/Network_android
  */

/**
 * ResultType: Type for the Resource data
 * RequestType: Type for the API response
 *
 * Strongly influenced by Google's architecture component sample implementation.
 */
abstract class NetworkBoundResource<ResultType, RequestType>: MediatorLiveData<Resource<ResultType>>(), Refreshable {

    private var dbSource: LiveData<ResultType>? = null
    private var apiSource: LiveData<Resource<RequestType>>? = null

    private var hasLoaded = false

    override fun onActive() {
        super.onActive()

        // TODO this might prevent us from refreshing for quite some time if the device has much RAM
        if (!hasLoaded) {
            hasLoaded = true
            load()
        }
    }

    private fun load(force: Boolean = false) {
        value = Resource.loading()
        dbSource = loadFromDb().apply {
            if (force) {
                fetchFromNetwork(this)
            } else {
                addSource(this) { data ->
                    removeSource(this)
                    if (shouldFetch(data)) {
                        fetchFromNetwork(this)
                    } else {
                        addSource(this) { newData ->
                            this@NetworkBoundResource.value =
                                    newData?.let { Resource.success(newData) }
                                    ?: Resource.error("Empty DB result")
                        }
                    }
                }
            }
        }
    }

    override fun refresh() {
        dbSource?.let { removeSource(it) }
        dbSource = null
        apiSource?.let { removeSource(it) }
        apiSource = null
        load(force = true)
    }

    override fun setValue(newValue: Resource<ResultType>?) {
        if (value != newValue) {
            super.setValue(newValue)
        }
    }

    override fun postValue(newValue: Resource<ResultType>?) {
        if (value != newValue) {
            super.postValue(newValue)
        }
    }

    /**
     * Called to save the result of the API response into the database
     */
    protected abstract fun saveCallResult(item: RequestType)

    /**
     * Called with the data in the database to decide whether it should be
     * fetched from the network.
     */
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    /**
     * Called to get the cached data from the database
     */
    protected abstract fun loadFromDb(): LiveData<ResultType>

    /**
     * Called to create the API call.
     */
    protected abstract fun createCall(): LiveData<Resource<RequestType>>

    /**
     * Called when the fetch fails. The child class may want to reset components
     * like rate limiter.
     */
    protected open fun onFetchFailed() {}

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        apiSource = createCall().apply {
            /*
            we re-attach dbSource as a new source,
            it will dispatch its latest value quickly
            */
            addSource(dbSource) { newData -> this@NetworkBoundResource.setValue(Resource.loading(newData)) }
            addSource(this) { response ->
                removeSource(this)
                removeSource(dbSource)

                response?.let {
                    when (response.status) {
                        is Success, is Loading -> saveResultAndReInit(response.data!!)
                        is Error -> {
                            onFetchFailed()
                            addSource(dbSource) {
                                this@NetworkBoundResource.setValue(
                                        Resource.error(response.status.message))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveResultAndReInit(response: RequestType) {
        // TODO replace with coroutine

        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg voids: Void): Void? {
                saveCallResult(response)
                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                /*
                we specially request a new live data,
                otherwise we will get immediately last cached value,
                which may not be updated with latest results received from network.
                */
                addSource(loadFromDb()) { newData ->
                    value =
                            newData?.let { Resource.success(newData) }
                            ?: Resource.error("Empty DB result")
                }
            }
        }.execute()
    }
}
