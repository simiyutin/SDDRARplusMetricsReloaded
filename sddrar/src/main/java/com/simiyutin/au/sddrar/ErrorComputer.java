package com.simiyutin.au.sddrar;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ErrorComputer {

    public static List<Integer> getFaultyEntities(DataSet dataSet, Set<Rule> rules, double percentageOfErrorsThreshold) {

        List<Integer> errors = getNumberOfErrors(dataSet, rules);
        List<Double> pe = getPercentageOfErrors(errors, rules.size());

        Set<Integer> potentiallyFaulty = IntStream
                .range(0, pe.size())
                .filter(e -> pe.get(e) > percentageOfErrorsThreshold)
                .boxed()
                .collect(Collectors.toSet());

        List<Integer> faulty;

        if (!potentiallyFaulty.isEmpty()) {
            double avg = new Mean().evaluate(potentiallyFaulty.stream().mapToDouble(errors::get).toArray());
            faulty = potentiallyFaulty.stream().filter(e -> errors.get(e) > avg).collect(Collectors.toList());

        } else {
            double[] nonZeroErrors = errors.stream().mapToDouble(e -> e).filter(e -> e > 0).toArray();
            double avg = new Mean().evaluate(nonZeroErrors);
            double dev = new StandardDeviation().evaluate(nonZeroErrors);
            double eps = avg + dev;
            faulty = IntStream.range(0, pe.size()).filter(e -> errors.get(e) > eps).boxed().collect(Collectors.toList());
        }

        return faulty;
    }

    public static List<Double> getPercentageOfErrors(List<Integer> errors, int numberOfRules) {
        List<Double> pe = new ArrayList<>();
        for (int error : errors) {
            pe.add((double) error / numberOfRules);
        }
        return pe;
    }



    public static List<Integer> getNumberOfErrors(DataSet dataSet, Set<Rule> rules) {

        List<Integer> errors = new ArrayList<>();

        for (double[] entity: dataSet.getMatrix().getData()) {
            int errorsForEntity = 0;
            for (Rule rule : rules) {
                if (!rule.check(entity)) {
                    errorsForEntity++;
                }
            }
            errors.add(errorsForEntity);
        }

        return errors;
    }
}
