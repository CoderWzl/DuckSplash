### 项目构建过程中遇到的问题

1. ##### 通过 Navigation 构建单 Activity 多 Fragment 应用时如何实现 Fragment 之间的通信，实现类似 Activity 的 startActivityForResult() 的效果。

   通过 Android developer 关于导航组件的介绍中能够找到解决方法 https://developer.android.google.cn/guide/navigation/navigation-programmatic 使用 ViewModel 在目标间共享界面相关数据。

   实际使用中发现新的问题 在 ViewModel 中配合 LiveData 之后，目标 fragment 发出事件后，原 fragment 订阅了 LiveData，每次订阅时都会接收到目标 fragment 发出的事件。LiveData 发送的是粘性事件。

   https://github.com/KunMinX/UnPeek-LiveData 可以解决问题，或者使用 SingleLiveEvent

   ```kotlin
   class SingleLiveEvent<T> : MutableLiveData<T?>() {
       private val mPending: AtomicBoolean = AtomicBoolean(false)
   
       override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
           super.observe(owner) {
               if (mPending.compareAndSet(true, false)) {
                   observer.onChanged(it)
               }
           }
       }
   
       @MainThread
       override fun setValue(@Nullable t: T?) {
           mPending.set(true)
           super.setValue(t)
       }
   
       /**
        * Used for cases where T is Void, to make calls cleaner.
        */
       @MainThread
       fun call() {
           value = null
       }
   }
   ```

   

