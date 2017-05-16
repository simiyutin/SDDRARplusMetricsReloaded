package com.sixrr.metrics.sddrar;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
    private RealMatrix data;
    private List<String> entityNames;
    private List<String> featureNames;

    public DataSet(RealMatrix data, List<String> entityNames, List<String> featureNames) {
        this.data = data;
        this.entityNames = entityNames;
        this.featureNames = featureNames;
    }

    // rows - entities, columns - features
    public static DataSet createaFromData(double[][] data) {
        List<String> entityNames = new ArrayList<>();
        List<String> featureNames = new ArrayList<>();
        final int featuresNum = data[0].length;
        final int entitiesNum = data.length;

        for (int i = 0; i < featuresNum; i++) {
            featureNames.add("feature" + i);
        }

        for (int j = 0; j < entitiesNum; j++) {
            entityNames.add("class" + j);
        }

        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        return new DataSet(matrix, entityNames, featureNames);
    }

    public RealMatrix getMatrix() {
        return data;
    }

    public void setData(RealMatrix data) {
        this.data = data;
    }

    public List<String> getEntityNames() {
        return entityNames;
    }

    public void setEntityNames(List<String> entityNames) {
        this.entityNames = entityNames;
    }

    public List<String> getFeatureNames() {
        return featureNames;
    }

    public void setFeatureNames(List<String> featureNames) {
        this.featureNames = featureNames;
    }
}
