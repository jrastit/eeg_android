package com.eeg.components.classifier;

/**
 * Created by dano on 05/06/17.
 */

public interface BufferListener {
    void getEpoch(double[][] buffer);
}
