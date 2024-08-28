package com.ericsson.aia.bps.beam.flow;

import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

/**
 * Sample implmentation of SimpleFunction.
 */
public class SimpleFunctionSample extends SimpleFunction<KV<String, String>, KV<String, String>> {

    /**
     * override the apply method.
     * @param keyValue Keyvalue
     * @return KV
     */
    public KV<String, String> apply(final KV<String, String> keyValue) {
        return keyValue;
    }
}
