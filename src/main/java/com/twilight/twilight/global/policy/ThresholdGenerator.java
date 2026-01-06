package com.twilight.twilight.global.policy;

import org.springframework.stereotype.Component;

@Component
public class ThresholdGenerator {

    public int searchThresholdGenerator(int ngramSize) {

        return Math.max(1, Math.min(5, (int) Math.ceil(ngramSize *0.3)));
    }


}
