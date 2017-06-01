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
        CorrelationFilter.filterByFeatureCorrelationRate(dataSet); //todo пробное удаление
        List<String> interestingMetrics = dataSet.getFeatureNames();
        System.out.println(interestingMetrics);
        SDDRARioHandler.dumpMetrics(interestingMetrics);

        Set<Rule> rules = RuleExtractor.extractRules(dataSet, MIN_CONFIDENCE);
        RulePack pack = new RulePack(rules, dataSet.getFeatureNames());
        SDDRARioHandler.dumpRulePack(pack);

    }

    // приходит уже только с нужными метриками
    public static List<String> checkNewData(MetricsRun metricsRun) {
        DataSet dataSet = extractDataSet(metricsRun);
        List<String> interestingMetrics = dataSet.getFeatureNames();
        System.out.println(interestingMetrics);

        RulePack rulePack = SDDRARioHandler.loadRulePack();
        Set<Rule> rules = rulePack.fitRulesToDataSet(dataSet);


        List<Integer> faultyIndices = ErrorComputer.getFaultyEntities(dataSet, rules, PERCENTAGE_OF_ERROR_THRESHOLD);


        List<String> names = dataSet.getEntityNames();
        List<String> faultyNames = faultyIndices.stream().map(names::get).collect(Collectors.toList());

        Map<String, Set<Rule>> failed = ErrorComputer.getFailedRulesForEachEntity(dataSet, rules)
                .entrySet()
                .stream()
                .filter(entry -> faultyNames.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Map<Integer, Integer>> failedNumByClass = new HashMap<>();
        failed.forEach((key, value) -> {
            Map<Integer, Integer> res = new HashMap<>();
            value.forEach(rule -> {
                rule.getBody().stream().filter(node -> node.getType() == Rule.Type.VALUE).forEach(node -> {
                    Integer prev = res.get(node.getValue());
                    if (prev == null) {
                        res.put(node.getValue(), 0);
                    } else {
                        res.put(node.getValue(), prev + 1);
                    }
                });
            });
            failedNumByClass.put(key, res);
        });

        StringBuilder sb = new StringBuilder();

        failedNumByClass.forEach((key, value) -> {
            sb.append(key).append(":\n");
            value.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                    .forEach(entry -> {
                        sb.append(dataSet.getFeatureNames().get(entry.getKey())).append("->").append(entry.getValue()).append('\n');
                    });
            sb.append("\n\n");
        });

        System.out.println(sb);

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
