package ubuntudroid.chimney.util.livedata

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

abstract class CoroutineLiveData<T> : LiveData<T>() {

    var deferred: Deferred<Unit>? = null

    override fun onActive() {
        super.onActive()

        if (deferred?.isCancelled == false) {
            return
        }

        deferred = async {
            postValue(runInBackground())
        }
    }

    override fun onInactive() {
        deferred?.cancel()
        super.onInactive()
    }

    /**
     * Executes a background operation.
     *
     * @return some value to be propagates as LiveData event
     */
    abstract suspend fun runInBackground(): T
}