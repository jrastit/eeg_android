package com.eeg;

import android.util.Log;

import com.eeg.MainApplication;
import com.eeg.components.classifier.ClassifierModule;
import com.eeg.components.emitter.AppNativeEventEmitter;
import com.eeg.components.managers.DataManager;
import com.eeg.components.managers.FilterGraphManager;
import com.eeg.components.managers.EEGGraphManager;
import com.eeg.components.managers.PSDGraphManager;
import com.eeg.components.connector.ConnectorModule;
import com.eeg.components.battery.BatteryModule;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

public class EEGPackage implements ReactPackage {

    public MainApplication appState;

    @Override
    // Register Native Modules to JS
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        appState.eventEmitter = new AppNativeEventEmitter(reactApplicationContext);
        Log.w("eventEmitter", " " + appState.eventEmitter);
        return Arrays.<NativeModule>asList(
                new ConnectorModule(reactApplicationContext),
                new ClassifierModule(reactApplicationContext),
				new BatteryModule(reactApplicationContext),
                new DataManager(reactApplicationContext),
                appState.eventEmitter);
    }

    @Override
    // Registers Java ViewManagers to JS
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Arrays.<ViewManager>asList(
                new EEGGraphManager(),
                new FilterGraphManager(),
                new PSDGraphManager()
        );
    }
}
