package org.codeforiraq.machinelearning.configs;


public class GtsrbQuantConfig extends  ModelConfig {

    @Override
    public String getModelFilename() {
        return "rice_data.tflite";
    }

    @Override
    public String getLabelsFilename() {
        return "labels.txt";
    }

    @Override
    public int getInputWidth() {
        return 224;
    }

    @Override
    public int getInputHeight() {
        return 224;
    }

    @Override
    public int getInputSize() {
        return getInputWidth() * getInputHeight() * getChannelsCount() * QUANT_BYTES_COUNT;
    }

    @Override
    public int getChannelsCount() {
        return 3;
    }

    @Override
    public float getStd() {
        return 128.f;
    }

    @Override
    public float getMean() {
        return 128.f;
    }

    @Override
    public boolean isQuantized() {
        return true;
    }
}
