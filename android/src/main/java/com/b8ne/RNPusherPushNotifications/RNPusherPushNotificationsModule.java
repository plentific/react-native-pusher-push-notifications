
package com.b8ne.RNPusherPushNotifications;

import android.app.Activity;
import android.os.AsyncTask;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.pusher.android.notifications.fcm.FCMPushNotificationReceivedListener;

public class RNPusherPushNotificationsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private PusherWrapper pusher;

  public RNPusherPushNotificationsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNPusherPushNotifications";
  }

  @ReactMethod
  public void setAppKey(String appKey) {
    this.pusher = new PusherWrapper(appKey, this.reactContext, (FCMPushNotificationReceivedListener) getCurrentActivity());
  }

  	@ReactMethod
   	public void subscribe(final String interest, final Callback errorCallback, final Callback successCallback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                pusher.subscribe(interest);
            }
        });
    }

    @ReactMethod
    public void unsubscribe(final String interest, final Callback errorCallback, final Callback successCallback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                pusher.unsubscribe(interest);
            }
        });
    }
}
