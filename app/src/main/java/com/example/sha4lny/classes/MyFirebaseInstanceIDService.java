package com.example.sha4lny.classes;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.inject.Provider;
import com.google.firebase.installations.remote.FirebaseInstallationServiceClient;
import com.google.firebase.platforminfo.UserAgentPublisher;

public class MyFirebaseInstanceIDService extends FirebaseInstallationServiceClient {
    public MyFirebaseInstanceIDService(@NonNull Context context, @NonNull Provider<UserAgentPublisher> publisher, @NonNull Provider<HeartBeatInfo> heartbeatInfo) {
        super(context, publisher, heartbeatInfo);
    }
}
