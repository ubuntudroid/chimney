package ubuntudroid.chimney.data


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.AsyncTask

/*
 TODO move this class as well as Resource and ViewLifecycleFragment to LiveDataExtensions library.
 NetworkBoundResource and Resource could as well be moved to stanwood/Network_android
  */

/**
 * ResultType: Type for the Resource data
 * RequestType: Type for the API response
 *
 * Strongly influenced by Google's architecture component sample implementation.
 */
abstract class NetworkBoundResource<ResultType, RequestType> {

    // TODO add some force refresh functionality (at first load AND while already in place!)

    private val result = object : MediatorLiveData<Resource<ResultType>>() {

        private var hasLoaded = false

        override fun onActive() {
            super.onActive()
            if (!hasLoaded) {
                hasLoaded = true
                load()
            }
        }
    }

    /**
     * returns a LiveData that represents the resource, implemented
     * in the base class.
     */
    val asLiveData: LiveData<Resource<ResultType>>
        get() = result

    private fun load() {
        result.value = Resource.loading()
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    result.value =
                            newData?.let { Resource.success(newData) }
                            ?: Resource.error("Empty DB result")
                }
            }
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
        val apiResponse = createCall()
        /*
        we re-attach dbSource as a new source,
        it will dispatch its latest value quickly
        */
        result.addSource(dbSource) { newData -> result.setValue(Resource.loading(newData)) }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            response?.let {
                when (response.status) {
                    is Success, is Loading -> saveResultAndReInit(response.data!!)
                    is Error -> {
                        onFetchFailed()
                        result.addSource(dbSource) {
                            result.setValue(
                                    Resource.error(response.status.message))
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
                result.addSource(loadFromDb()) { newData ->
                    result.value =
                            newData?.let { Resource.success(newData) }
                            ?: Resource.error("Empty DB result")
                }
            }
        }.execute()
    }
}
