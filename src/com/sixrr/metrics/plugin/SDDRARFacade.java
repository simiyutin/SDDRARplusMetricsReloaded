package com.sixrr.metrics.plugin;

import com.simiyutin.au.sddrar.*;
import com.sixrr.metrics.Metric;
import com.sixrr.metrics.MetricCategory;
import com.sixrr.metrics.metricModel.MetricsResult;
import com.sixrr.metrics.metricModel.MetricsRun;
import com.sixrr.metrics.profile.MetricInstance;
import com.sixrr.metrics.profile.MetricsProfile;
import org.apache.commons.math3.linear.MatrixUtils;

import java.util.*;
import java.util.stream.Collectors;


public class SDDRARFacade {
    private static final double MIN_CONFIDENCE = 0.8;
    private static final double PERCENTAGE_OF_ERROR_THRESHOLD = 0.9;

    public static void trainAndPersistModel(MetricsProfile profile, MetricsRun metricsRun) {
        DataSet dataSet = extractDataSet(metricsRun);
        CorrelationFilter.filterByFeatureCorrelationRate(dataSet);
        Set<Rule> rules = RuleExtractor.extractRules(dataSet, MIN_CONFIDENCE);
        RulePack pack = new RulePack(rules, dataSet.getFeatureNames());
        SDDRARioHandler.dumpRulePack(pack);
        List<String> interestingMetrics = dataSet.getFeatureNames();
        SDDRARioHandler.dumpMetrics(interestingMetrics);
    }

    // приходит уже только с нужными метриками
    public static List<String> checkNewData(MetricsRun metricsRun) {
        DataSet dataSet = extractDataSet(metricsRun);
        RulePack rulePack = SDDRARioHandler.loadRulePack();
        Set<Rule> rules = rulePack.fitRulesToDataSet(dataSet);


        List<Integer> faultyIndices = ErrorComputer.getFaultyEntities(dataSet, rules, PERCENTAGE_OF_ERROR_THRESHOLD);

        Map<Integer, Set<Rule>> failedRules = ErrorComputer.getFailedRulesForEachEntity(dataSet, rules);
        List<String> names = dataSet.getEntityNames();
        List<String> faultyNames = faultyIndices.stream().map(names::get).collect(Collectors.toList());
        return faultyNames;
    }

    // понять какие метрики на что влияют, какие лучшие
    // но вроде модем взять сразу все и хрен с ним
    // но нет, потому что на входе отсеиватся только те, у кого было |mean - stedev| < val
    // вопрос : как определить проблему по проваленным правилам?

    public static void selectInterestingMetrics(MetricsProfile profile) {

        List<String> interestingMetrics = SDDRARioHandler.loadMetrics();
        List<MetricInstance> metricInstances = profile.getMetricInstances();
        for (MetricInstance instance : metricInstances) {
            instance.setEnabled(interestingMetrics.contains(instance.getMetric().getID()));
        }
    }

    private static DataSet extractDataSet(MetricsRun metricsRun) {

        MetricsResult result = metricsRun.getResultsForCategory(MetricCategory.Class);
        List<String> entityNames = Arrays.asList(result.getMeasuredObjects());
        List<String> featureNames = new ArrayList<>();
        for (Metric metric : result.getMetrics()) {
            featureNames.add(metric.getID());
        }

        Metric[] metrics = result.getMetrics();

        double[][] data = new double[entityNames.size()][featureNames.size()];
        for (int i = 0; i < entityNames.size(); i++) {
            for (int j = 0; j < featureNames.size(); j++) {
                Metric metric = metrics[j];
                String entity = entityNames.get(i);
                Double value = result.getValueForMetric(metric, entity);
                data[i][j] = value == null ? 0 : value;
            }
        }

        DataSet dataSet = new DataSet(MatrixUtils.createRealMatrix(data), entityNames, featureNames);
        return dataSet;
    }
}
