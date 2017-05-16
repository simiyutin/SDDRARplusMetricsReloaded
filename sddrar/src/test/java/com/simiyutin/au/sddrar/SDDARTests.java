package com.simiyutin.au.sddrar;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SDDARTests {


    @Test
    public void testCorrelationFilteringPerfectCorr() {

        double[][] data = {
                {1,2,3,4},
                {2,4,6,8},
                {3,6,9,12}
        };
        DataSet control = DataSet.createaFromData(data);
        DataSet test = DataSet.createaFromData(data);
        CorrelationFilter.filterByFeatureCorrelationRate(test);
        assertEquals(control.getMatrix(), test.getMatrix());
    }

    @Test
    public void testCorrelationFilteringKickOut() {

        //last column highly uncorrelated and supposed to be kicked out
        double[][] testData = {
                {1,2,3,4,3},
                {2,4,6,8,2},
                {4,8,12,16,1},
                {8,16,24,32,5},
                {16,32,48,64,1},
                {32,64,96,128,8},
                {64,128,192,256,2},
        };

        double[][] expectedData = {
                {1,2,3,4},
                {2,4,6,8},
                {4,8,12,16},
                {8,16,24,32},
                {16,32,48,64},
                {32,64,96,128},
                {64,128,192,256}
        };

        DataSet expected = DataSet.createaFromData(expectedData);
        DataSet test = DataSet.createaFromData(testData);
        CorrelationFilter.filterByFeatureCorrelationRate(test);
        assertEquals(expected.getMatrix(), test.getMatrix());
    }

    @Test
    public void testRulesExtraction() {
        DataSet dataSet = createSimpleDataSet();
        Set<Rule> rules = RuleExtractor.extractRules(dataSet, 0.8);

        Set<Rule> expected = new HashSet<>();
        expected.add(Rule.valueOf("2 > 0 < 1"));
        expected.add(Rule.valueOf("3 < 2 > 0"));
        expected.add(Rule.valueOf("3 > 1"));

        assertEquals(expected, rules);
    }

    @Test
    public void testDumpLoad() {
        DataSet dataSet = createSimpleDataSet();
        SDDRARioHandler.dump(dataSet, "test");
        DataSet deserialized = SDDRARioHandler.load("test");
        Assert.assertEquals(dataSet.getMatrix().getData(), deserialized.getMatrix().getData());
    }

    @Test
    public void testForeignDataSetExtraction() {
        DataSet dataSet = SDDRARioHandler.load("dataset.dat");
        System.out.println(dataSet.getEntityNames().size());
        CorrelationFilter.filterByFeatureCorrelationRate(dataSet);
        Set<Rule> rules = RuleExtractor.extractRules(dataSet, 0.8);
        assertTrue(rules.size() > 0);
    }

    @Test
    public void testGetNumberOfErrors() {
        double[][] data = {
                {1,2,3},
                {3,2,1}
        };

        DataSet dataSet = DataSet.createaFromData(data);

        Set<Rule> rules = new HashSet<>();
        rules.add(Rule.valueOf("0 < 1 < 2"));
        rules.add(Rule.valueOf("0 < 2"));
        rules.add(Rule.valueOf("0 < 1"));
        rules.add(Rule.valueOf("1 < 2"));

        List<Integer> errors = ErrorComputer.getNumberOfErrors(dataSet, rules);

        assertEquals(Arrays.asList(0, 4), errors);
        assertEquals(Arrays.asList(0.0, 1.0), ErrorComputer.getPercentageOfErrors(errors, 4));

    }

    @Test
    // triggers first case of analysis
    public void testGetFaultyEntitiesCase1() {
        double[][] data = {
                {1,2,3},
                {3,2,1},
                {3,1,2}
        };

        Set<Rule> rules = new HashSet<>();
        rules.add(Rule.valueOf("0 < 1 < 2"));
        rules.add(Rule.valueOf("0 < 2"));
        rules.add(Rule.valueOf("0 < 1"));
        rules.add(Rule.valueOf("1 < 2"));

        DataSet dataSet = DataSet.createaFromData(data);
        List<Integer> faulty = ErrorComputer.getFaultyEntities(dataSet, rules, 0.5);
        assertEquals(Collections.singletonList(1), faulty);
    }

    @Test
    // triggers second case of analysis
    public void testGetFaultyEntitiesCase2() {
        double[][] data = {
                {1,2,3,4,5,6},
                {4,3,2,1,5,6},
                {1,2,3,4,6,5},
                {1,2,3,4,6,5},
                {1,2,3,4,6,5}
        };

        Set<Rule> rules = new HashSet<>();
        rules.add(Rule.valueOf("0 < 1"));
        rules.add(Rule.valueOf("1 < 2"));
        rules.add(Rule.valueOf("2 < 3"));
        rules.add(Rule.valueOf("3 < 4"));
        rules.add(Rule.valueOf("4 < 5"));

        DataSet dataSet = DataSet.createaFromData(data);
        List<Integer> faulty = ErrorComputer.getFaultyEntities(dataSet, rules, 0.8);
        assertEquals(Collections.singletonList(1), faulty);
    }


    private DataSet createSimpleDataSet() {
        double[][] data = {
                {2,4,3,1},
                {5,6,8,7},
                {9,10,12,11},
                {12,15,13,11},
                {1,2,4,3},
                {5,6,8,7},
                {9,10,12,11},
                {12,15,13,16},
                {27,21,29,24},
                {30,34,29,38}
        };

        return DataSet.createaFromData(data);
    }
}
