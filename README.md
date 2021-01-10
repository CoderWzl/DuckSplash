### Jetpack 入门，项目笔记

- Android 5.0 之上实现透明状态栏

  ```java
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    
      //5.0 全透明实现
      //getWindow.setStatusBarColor(Color.TRANSPARENT)
      Window window = getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |                 View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);    
      window.setStatusBarColor(Color.TRANSPARENT);
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
   //4.4 全透明状态栏
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
  } 
  ```

  主题

  ```xml
  <style name="AppTheme_Translate" parent="Theme.AppCompat.Light.NoActionBar">
      <!-- Customize your theme here. -->
      <item name="colorPrimary">@color/colorPrimary</item>
      <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
      <item name="colorAccent">@color/colorAccent</item>
      <item name="windowActionBar">false</item>
      <item name="windowNoTitle">true</item>
      <item name="android:windowTranslucentStatus">true</item>
  </style>
  ```

  然后在布局的 xml 里面的根 View 上设置 android:fitSystemWindows="true"

