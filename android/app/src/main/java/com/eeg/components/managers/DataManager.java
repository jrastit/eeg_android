package com.eeg.components.managers;

import com.eeg.components.graphs.EEGGraph;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

public class DataManager extends ReactContextBaseJavaModule {

    public DataManager(ReactApplicationContext reactContext){
        super(reactContext);
    }

    @Override
    //getName is required to define the name of the module represented in JavaScript
    public String getName() {
        return "DataManager";
    }

    @ReactMethod
    public void readData(Callback errorCallback, Callback successCallback) {
        try {
            System.out.println("readData from Java");
            double[][] data = EEGGraphManager.readData();

            String ret = "";
            if (data != null){
                for (int i = 0; i < data.length; i++){
                    ret += "D"
                            + Double.toString(data[i][0]) + ","
                            + Double.toString(data[i][1]) + ","
                            + Double.toString(data[i][2]) + ","
                            + Double.toString(data[i][3]) + "X";
                }
            }

            successCallback.invoke(ret);

        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }
}
