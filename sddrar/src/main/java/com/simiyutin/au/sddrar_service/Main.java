package com.simiyutin.au.sddrar_service;

import com.simiyutin.au.sddrar.CorrelationFilter;
import com.simiyutin.au.sddrar.DataSet;
import com.simiyutin.au.sddrar.Rule;
import com.simiyutin.au.sddrar.RuleExtractor;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        DataSet dataSet = IOHandler.readEntities();
        CorrelationFilter.filterByFeatureCorrelationRate(dataSet);
        Set<Rule> rules = RuleExtractor.extractRules(dataSet, 0.58);
    }

}
