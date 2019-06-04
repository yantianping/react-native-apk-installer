package com.cnull.apkinstaller;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import java.io.File;

public class ApkInstallerModule extends ReactContextBaseJavaModule {
  private ReactApplicationContext _context = null;

  // private static final String DURATION_SHORT_KEY = "SHORT";
  // private static final String DURATION_LONG_KEY = "LONG";

  public ApkInstallerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    _context = reactContext;
  }

  @Override
  public String getName() {
    return "ApkInstaller";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    // constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    // constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  @ReactMethod
  // public void show(String message, int duration) {
  public void test(String message) {
    Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void install(String path) {
    //  String cmd = "chmod 777 " +path;
    //  try {
    //      Runtime.getRuntime().exec(cmd);
    //  } catch (Exception e) {
    //      e.printStackTrace();
    //  }
    //  Intent intent = new Intent(Intent.ACTION_VIEW);
    //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //  intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
    //  _context.startActivity(intent);
	  
     if (Build.VERSION.SDK_INT>=26){
          int result=  _context.getPackageManager().checkPermission(Manifest.permission.INSTALL_PACKAGES,"com.fxzh.recycleapp");
          if (result==PackageManager.PERMISSION_GRANTED){
              Log.e("检查权限","有安装权限");
          }else if (result==PackageManager.PERMISSION_DENIED){
              Log.e("检查权限","没有有安装权限");
              //请求安装未知应用来源的权限
              ActivityCompat.requestPermissions(_context.getCurrentActivity(), new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},101);
          }
      }

      String cmd = "chmod 777 " +path;
      try {
          Runtime.getRuntime().exec(cmd);
      } catch (Exception e) {
          e.printStackTrace();
      }
      File file= new File(path);
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      //intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
      if(Build.VERSION.SDK_INT>= 24) { //判读版本是否在7.0以上
          //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
          Uri apkUri =
                  FileProvider.getUriForFile(_context, "com.fxzh.recycleapp.updateProvider", file);
          //添加这一句表示对目标应用临时授权该Uri所代表的文件
          intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
      }else{
          intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
      }

      _context.startActivity(intent);
  }
}
