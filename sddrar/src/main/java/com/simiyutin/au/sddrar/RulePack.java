package com.simiyutin.au.sddrar;

import java.io.Serializable;
import java.util.*;

/**
 * Created by boris on 23.05.17.
 */
public class RulePack implements Serializable {
    private Set<Rule> rules;
    private List<String> featureNames;

    public RulePack(Set<Rule> rules, List<String> featureNames) {
        this.rules = rules;
        this.featureNames = featureNames;
    }

    public Set<Rule> fitRulesToDataSet(DataSet dataSet) {

        Map<String, Integer> newIndices = new HashMap<>();
        for (int i = 0; i < dataSet.getFeatureNames().size(); i++) {
            newIndices.put(dataSet.getFeatureNames().get(i), i);
        }

        Set<Rule> resultRules = new HashSet<>();
        for (Rule rule : rules) {
            List<Rule.Node> body = new ArrayList<>();
            for (Rule.Node node : rule.getBody()) {
                if (node.getType() != Rule.Type.VALUE) {
                    body.add(new Rule.Node(node));
                } else {
                    Integer newIndex = newIndices.get(featureNames.get(node.getValue()));
                    body.add(new Rule.Node(newIndex));
                }
            }
            resultRules.add(Rule.fromBody(body));
        }

        return resultRules;
    }
}
