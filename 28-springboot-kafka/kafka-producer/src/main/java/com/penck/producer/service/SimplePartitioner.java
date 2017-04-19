package com.penck.producer.service;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * Created by peng on 2017/4/19.
 */
public class SimplePartitioner implements Partitioner {
    public SimplePartitioner(VerifiableProperties properties) {
    }

    @Override
    public int partition(Object key, int numPartitions) {
        int partition = 0;
        String stringKey = (String)key;
        int offset = stringKey.lastIndexOf('.');
        if (offset > 0) {
            partition = Integer.parseInt(stringKey.substring(offset+1)) % numPartitions;
        }
        return partition;
    }
}
