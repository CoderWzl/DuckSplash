### DuckSplash

##### Api 准备

使用 unsplash api 构建应用，访问 https://unsplash.com/ 注册并创建应用，获取 Access Key 和 Secret key

阅读开发文档 https://unsplash.com/documentation

##### 构建 App 原型

通过 Unsplash 文档需要构建如下页面：（参照开源项目 Resplash）

1. 首页：
   1. 最新：展示最新图片（photos api）
   2. 图集：展示图片合集（collections api）
2. 合集详情（图片列表）
3. 图片详情
4. 用户详情
5. 搜索：
   1. 图片
   2. 图集
   3. 用户
6. other：登录个人中心等

先完成 首页，图片合集和搜索界面确定 ui 框架，熟悉 Jetpack 各个组件，其他界面按照既定框架逐步完成

##### 使用 Jetpack 逐步完善 DuckSplash

1. 使用 Navigation 组件实现单 Activity 多 Fragment Ui 框架 

   熟悉 Navigation 实现 Fragment 之间的导航以及参数传递（TODO 完成 各组件建导航图）

   - https://developer.android.google.cn/guide/navigation
   - https://developer.android.com/codelabs/android-navigation?hl=en&continue=https%3A%2F%2Fcodelabs.developers.google.com%2F%3Fcat%3Dandroid#0

2. 了解 Architecture Components https://developer.android.com/codelabs/android-room-with-a-view-kotlin#1

   了解google 推荐的 Android 项目架构，开始构建自己的 ViewModel，Repository，DataSource

   DataSource 使用 Retrofit 后面使用 Room 组件

3. 使用 Retrofi 框架实现 Api 请求 https://square.github.io/retrofit/

4. 了解 Kotlin 协程 

   - https://developer.android.com/codelabs/kotlin-coroutines?hl=en&continue=https%3A%2F%2Fcodelabs.developers.google.com%2F%3Fcat%3Dandroid#0

5. 完成固定单页数据请求，使用 ViewBinding 将数据展示

6. 了解 Paging3 https://developer.android.com/codelabs/android-paging?hl=en&continue=https%3A%2F%2Fcodelabs.developers.google.com%2F%3Fcat%3Dandroid#1

   按照 codelab 完成各个列表的分页

7. 了解 Hilt https://developer.android.google.cn/training/dependency-injection/ （Di ：Knio、 Degger，https://www.jianshu.com/p/015eec1b1ce0）

   将项目中 View，ViewModel，Repository，ApiService，DataSource 之间的依赖通过 hilt 实现自动注入