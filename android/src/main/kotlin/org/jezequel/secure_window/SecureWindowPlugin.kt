package org.jezequel.secure_window

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.view.WindowManager.LayoutParams;
import androidx.lifecycle.Lifecycle
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.FlutterLifecycleAdapter;
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/** SecureWindowPlugin */
public class SecureWindowPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, LifecycleObserver {
  private var activity: Activity? = null
  lateinit var instance: SecureWindowPlugin

  override fun onDetachedFromActivity() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    if (::instance.isInitialized)
      instance.activity = binding.activity
    else
      this.activity = binding.activity
    val lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding) as Lifecycle
    lifecycle.addObserver(this)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    if (::instance.isInitialized)
      instance.activity = binding.activity
    else
      this.activity = binding.activity
    val lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding) as Lifecycle
    lifecycle.addObserver(this)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    //To change body of created functions use File | Settings | File Templates.
  }


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    instance = SecureWindowPlugin()
    val channel = MethodChannel(flutterPluginBinding.binaryMessenger, "secure_window")
    channel.setMethodCallHandler(instance)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun connectListener() {
    print("WE RESUMED")
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "secure_window")
      channel.setMethodCallHandler(SecureWindowPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "secure") {
      activity?.window?.addFlags(LayoutParams.FLAG_SECURE)
      result.success(true)
    } else if (call.method == "open") {
      activity?.window?.clearFlags(LayoutParams.FLAG_SECURE)
        result.success(true)
    } else {
      result.success(true)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}
