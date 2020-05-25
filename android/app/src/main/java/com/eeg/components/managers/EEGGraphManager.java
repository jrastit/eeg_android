package com.eeg.components.managers;

import com.eeg.components.graphs.EEGLive;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import javax.annotation.Nullable;


// Manages EEGGraph objects for import into React Native
public class EEGGraphManager extends SimpleViewManager<EEGLive> {
    private final static String REACT_CLASS = "EEG_GRAPH";
    static EEGLive curentGraph = null;
    EEGLive eegGraph;

    @Override
    // Used to reference this view type from js side
    public String getName() {
        return REACT_CLASS;
    }

    // Creates new EEGGraph views, accepting context as an argument. Context links the EEGGraph view to MainActivity
    @Override
    public EEGLive createViewInstance(ThemedReactContext context) {
        eegGraph = new EEGLive(context);
        curentGraph = eegGraph;
        return eegGraph;
    }

    // Called when view is detached from view hierarchy
    // Necessary to clean up graph here with react-router
    @Override
    public void onDropViewInstance(EEGLive graph) {
        graph.stopDataListener();
        graph.removeAllViews();
    }

    // Bridge function for channelOfInterestProp. Calls setChannelOfInterest in EEGGraph
    @ReactProp(name = "channelOfInterest")
    public void setChannelOfInterest(EEGLive graph, @Nullable int channel) {
        graph.setChannelOfInterest(channel);
    }

    // Bridge function for isRecording Prop. Calls setIsRecording in EEGGraph
    @ReactProp(name = "isRecording")
    public void setIsRecording(EEGLive graph, @Nullable boolean isRecording) {
        if(isRecording) {
            graph.startRecording();
        } else {
            graph.stopRecording();
        }
    }

    // Bridge function for isRecording Prop. Calls setIsRecording in EEGGraph
    @ReactProp(name = "isPlaying")
    public void setIsPlaying(EEGLive graph, @Nullable boolean isPlaying) {
        if(isPlaying) {
            graph.resume();
        } else {
            graph.pause();
        }
    }

    // Bridge function for offline Prop. Calls setOfflineMode in EEGGraph
    @ReactProp(name = "offlineData")
    public void setOfflineData(EEGLive graph, @Nullable String offlineData) {
        graph.setOfflineData(offlineData);
    }

    // Bridge function for notch Prop. Calls setNotchFrequency in EEGGraph
    @ReactProp(name = "notchFrequency")
    public void setNotchFrequency(EEGLive graph, @Nullable int notchFrequency) {
        graph.setNotchFrequency(notchFrequency);
    }

    // Bridge function for receiving 'start threads' and 'stop threads' commands from the
    // dispatchViewManagerCommand() method in JS. Currently, only used in stopping threads when
    // switching between graphs in the SandboxGraph component
    @Override
    public void receiveCommand(
            EEGLive view,
            int commandID,
            @Nullable ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandID) {
            case 0: {
                view.stopDataListener();
                return;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandID,
                        getClass().getSimpleName()));
        }
    }

    // Callback that will be triggered after all properties are updated in current update transaction
    //* (all @ReactProp handlers for properties updated in current transaction have been called)
    @Override
    protected void onAfterUpdateTransaction(EEGLive view) {
        super.onAfterUpdateTransaction(view);
        eegGraph.stopDataListener();
        eegGraph.startDataListener();
    }

    static public double[][] readData(){
        if (curentGraph != null){
            return curentGraph.readData();
        }
        return null;
    }
}
