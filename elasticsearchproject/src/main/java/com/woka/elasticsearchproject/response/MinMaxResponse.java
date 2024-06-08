package com.woka.elasticsearchproject.response;

public class MinMaxResponse {
    private double minValue;
    private double maxValue;

    public MinMaxResponse(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }
}
