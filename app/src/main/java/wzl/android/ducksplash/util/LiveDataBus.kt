package wzl.android.ducksplash.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 *Created on 2021/4/8
 *@author zhilin
 *
 * LiveDate 实现消息总线
 * https://tech.meituan.com/2018/07/26/android-livedatabus.html
 * https://github.com/JeremyLiao/LiveEventBus
 * 添加依赖
 * implementation 'com.jeremyliao:live-event-bus-x:1.7.3'
 */
object LiveDataBus {

    private val bus = HashMap<String, BusMutableLiveData<*>>()

    fun <T> with(key: String, type: Class<T>): BusMutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData<T>()
        }
        return bus[key] as BusMutableLiveData<T>
    }

    fun with(key: String): MutableLiveData<*> {
        return with(key, Object::class.java)
    }

}

private class ObserverWrapper<T>(
    private val observer: Observer<T>
) : Observer<T> {

    override fun onChanged(t: T) {
        if (isCallOnObserve()) {
            return
        }
        observer.onChanged(t)
    }

    private fun isCallOnObserve(): Boolean {
        val stackTrace: Array<StackTraceElement>? = Thread.currentThread().stackTrace
        if (stackTrace != null && stackTrace.isNotEmpty()) {
            stackTrace.forEach { element ->
                if ("android.arch.lifecycle.LiveData" == element.className &&
                    "observeForever" == element.methodName) {
                    return true
                }
            }
        }
        return false
    }

}

class BusMutableLiveData<T> : MutableLiveData<T>() {

    private val observerMap = HashMap<Observer<*>, Observer<*>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        try {
            hook(observer = observer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        if (!observerMap.containsKey(observer)) {
            observerMap[observer] = ObserverWrapper(observer)
        }
        super.observeForever(observerMap[observer] as Observer<in T>)
    }

    override fun removeObserver(observer: Observer<in T>) {
        val realObserver: Observer<in T> = if (observerMap.containsKey(observer))
            observerMap.remove(observer) as Observer<in T> else observer
        super.removeObserver(realObserver)
    }

    @Throws(Exception::class)
    private fun hook(observer: Observer<in T>) {
        //get wrapper's version
        val classLiveData = LiveData::class.java
        val fieldObservers: Field = classLiveData.getDeclaredField("mObservers")
        fieldObservers.isAccessible = true
        val objectObservers: Any? = fieldObservers.get(this)
        val classObservers: Class<*>? = objectObservers?.javaClass
        val methodGet: Method? = classObservers?.getDeclaredMethod("get", Any::class.java)
        methodGet?.isAccessible = true
        val objectWrapperEntry: Any? = methodGet?.invoke(objectObservers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        if (objectWrapper == null) {
            throw NullPointerException("Wrapper can not be bull!")
        }
        val classObserverWrapper: Class<*> = objectWrapper.javaClass.superclass
        val fieldLastVersion: Field = classObserverWrapper.getDeclaredField("mLastVersion")
        fieldLastVersion.isAccessible = true
        //get livedata's version
        val fieldVersion: Field = classLiveData.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        val objectVersion: Any? = fieldVersion.get(this)
        //set wrapper's version
        fieldLastVersion.set(objectWrapper, objectVersion)
    }
}
