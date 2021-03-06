/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.facebook.react.uiapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.react.BuildConfig;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.react.v8executor.V8ExecutorFactory;
import com.facebook.react.jscexecutor.JSCExecutorFactory;
import com.facebook.react.views.text.ReactFontManager;


import java.io.File;
import java.util.Arrays;
import java.util.List;


import static com.facebook.react.modules.systeminfo.AndroidInfoHelpers.getFriendlyDeviceName;

public class RNTesterApplication extends Application implements ReactApplication {
 private static final String JSE_CACHING_DIRECTORY_NAME = "cache";

  static class RNTesterReactMarker implements ReactMarker.MarkerListener {
    final private static String LOG_TAG = "RNTesterReactMarker";

    @Override
    public void logMarker(final ReactMarkerConstants name, final String tag, final int instanceKey) {
      StringBuilder builder = new StringBuilder();
      String markerName = name.toString();
      builder.append(" Marker: ").append(markerName);
      if (tag != null) {
        builder.append(" Tag: ").append(tag);
      }
      Log.i(LOG_TAG, builder.toString());
    }
  }

 private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

    @Override
    public String getJSMainModuleName() {
      return "RNTester/js/RNTesterApp.android";
    }

    @Override
    public String getBundleAssetName() {
      return "RNTesterApp.android.bundle";
    }

    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    public List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage()
      );
    }

    @Override
    public JavaScriptExecutorFactory getJavaScriptExecutorFactory() {
      ReactMarker.addListener(new RNTesterReactMarker());
      // We use the name of the device and the app for debugging & metrics
      String appName = getPackageName();
      String deviceName = getFriendlyDeviceName();

      File jseDir = getApplicationContext().getDir("jse", Context.MODE_PRIVATE);
      File jsDataStore = new File(jseDir, JSE_CACHING_DIRECTORY_NAME);
      String jseCacheDirectoryPath = "";
      if ((jsDataStore.exists() && jsDataStore.isDirectory()) || jsDataStore.mkdirs()) {
        jseCacheDirectoryPath = jsDataStore.getAbsolutePath();
      }
      if(BuildConfig.JS_ENGINE_USED.equals("V8")){
        return new V8ExecutorFactory(appName, deviceName, new V8ExecutorFactory.V8ConfigParams(jseCacheDirectoryPath, V8ExecutorFactory.V8ConfigParams.CacheType.CodeCache, false));
      }
      else if(BuildConfig.JS_ENGINE_USED.equals("JSC")){
        return new JSCExecutorFactory(appName, deviceName);
      }
      else{
        return null;
      }
    }
  };

  @Override
  public void onCreate() {
    ReactFontManager.getInstance().addCustomFont(this, "Rubik", R.font.rubik);
    super.onCreate();
  }

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }
};
