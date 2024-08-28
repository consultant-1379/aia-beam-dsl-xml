package com.ericsson.aia.bps.beam.flow;

import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

/**
 * This is a sample code of ParDo implmentation.
 *
 */
public class ParDoSample extends DoFn<KafkaRecord<String, String>, KV<String, String>> {

    /**
     * Sample method of processElement.
     * @param processContext ProcessContext
     */
    @ProcessElement
    public void processElement(final ProcessContext processContext) {
        final KafkaRecord<String, String> element = processContext.element();
        processContext.output(KV.of(element.getKV().getKey(), element.getKV().getValue()));
    }
}
