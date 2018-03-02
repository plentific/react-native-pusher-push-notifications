package com.b8ne.RNPusherPushNotifications;

import android.app.Activity;
import android.util.Log;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.facebook.react.bridge.ReactContext;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.android.PusherAndroid;
import com.pusher.android.notifications.PushNotificationRegistration;
import com.pusher.android.notifications.fcm.FCMPushNotificationReceivedListener;
import com.pusher.android.notifications.interests.InterestSubscriptionChangeListener;
import com.pusher.android.notifications.tokens.PushNotificationRegistrationListener;

/**
 * Created by bensutter on 13/1/17.
 */

public class PusherWrapper {
    private static PushNotificationRegistration nativePusher;
    private ReactContext context;
    private FCMPushNotificationReceivedListener listener;

    public PusherWrapper(String appKey) {
        Log.d("PUSHER_WRAPPER", "Creating Pusher with App Key: " + appKey);
        System.out.print("Creating Pusher with App Key: " + appKey);
        if (nativePusher == null) {
            PusherAndroid pusher = new PusherAndroid(appKey);
            nativePusher = pusher.nativePusher();
        }
    }

    public PusherWrapper(String appKey, ReactContext context, FCMPushNotificationReceivedListener listener) {
      this.context = context;
      this.listener = listener;
      Log.d("PUSHER_WRAPPER", "Creating Pusher with App Key: " + appKey);
      System.out.print("Creating Pusher with App Key: " + appKey);
        if (nativePusher == null) {
            PusherAndroid pusher = new PusherAndroid(appKey);
            nativePusher = pusher.nativePusher();
        }

        try {
            nativePusher.registerFCM(context, new PushNotificationRegistrationListener() {
                @Override
                public void onSuccessfulRegistration() {
                    Log.d("PUSHER_WRAPPER", "Successfully registered to FCM");
                    System.out.print("Successfully registered to FCM");
                }

                @Override
                public void onFailedRegistration(int statusCode, String response) {
                    Log.d("PUSHER_WRAPPER", "FCM Registration failed with code " + statusCode + " " + response);
                    System.out.print("FCM Registration failed with code " + statusCode + " " + response);
                }
            });

        } catch (Exception ex) {
            Log.d("PUSHER_WRAPPER", "Exception in PusherWrapper " + ex.getMessage());
            System.out.print("Exception in PusherWrapper " + ex.getMessage());
        }
    }

    public PushNotificationRegistration getNativePusher() {
        return this.nativePusher;
    }

    public void subscribe(final String interest) {
        Log.d("PUSHER_WRAPPER", "Attempting to subscribe to " +  interest);
        System.out.print("Attempting to subscribe to " +  interest);
        nativePusher.subscribe(interest, new InterestSubscriptionChangeListener() {
            @Override
            public void onSubscriptionChangeSucceeded() {
                Log.d("PUSHER_WRAPPER", "Success! " + interest);
                System.out.print("Success! " + interest);
            }

            @Override
            public void onSubscriptionChangeFailed(int statusCode, String response) {
                Log.d("PUSHER_WRAPPER", ":(: received " + statusCode + " with" + response);
                System.out.print(":(: received " + statusCode + " with" + response);
            }
        });

        nativePusher.setFCMListener(this.listener);
    }

    public void unsubscribe(final String interest) {
        nativePusher.unsubscribe(interest, new InterestSubscriptionChangeListener() {
            @Override
            public void onSubscriptionChangeSucceeded() {
              Log.d("PUSHER_WRAPPER", "Success! " + interest);
              System.out.print("Success! " + interest);
            }

            @Override
            public void onSubscriptionChangeFailed(int statusCode, String response) {
                Log.d("PUSHER_WRAPPER", ":(: received " + statusCode + " with" + response);
                System.out.print(":(: received " + statusCode + " with" + response);
            }
        });
    }
}
