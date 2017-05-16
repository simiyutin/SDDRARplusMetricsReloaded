package com.simiyutin.au.sddrar;

import org.apache.commons.math3.linear.MatrixUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by boris on 16.05.17.
 */
public class SDDRARFacade {
    private static final double MIN_CONFIDENCE = 0.8;
    private static final double PERCENTAGE_OF_ERROR_THRESHOLD = 0.5;

    public static void trainAndPersistModel(DataSet dataSet) {
        CorrelationFilter.filterByFeatureCorrelationRate(dataSet);
        Set<Rule> rules = RuleExtractor.extractRules(dataSet, MIN_CONFIDENCE);
        SDDRARioHandler.dumpRules(rules);
        List<String> interestingMetrics = dataSet.getFeatureNames();
        SDDRARioHandler.dump(interestingMetrics, "metrics.dat");
    }

    // приходит уже только с нужными метриками
    public static List<String> checkNewData(DataSet dataSet) {
        Set<Rule> rules = SDDRARioHandler.loadRules();
        List<Integer> faultyIndices = ErrorComputer.getFaultyEntities(dataSet, rules, PERCENTAGE_OF_ERROR_THRESHOLD);
        List<String> names = dataSet.getEntityNames();
        List<String> faultyNames = faultyIndices.stream().map(names::get).collect(Collectors.toList());
        return faultyNames;
    }
}
