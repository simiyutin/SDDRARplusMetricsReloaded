package com.simiyutin.au.sddrar_service;

import com.simiyutin.au.sddrar.DataSet;
import com.simiyutin.au.sddrar.Rule;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IOHandler {

    public static DataSet readEntities() {
        return tempEntityProvider();
    }

    public static void dump(DataSet dataSet, String name) {
        try {
            FileOutputStream fos = new FileOutputStream("/home/boris/au_2/nir/project/data/" + name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataSet.getMatrix().getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataSet load(String name) {
        try {
            FileInputStream fis = new FileInputStream("/home/boris/au_2/nir/project/data/" + name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            double[][] data = (double [][]) ois.readObject();
            DataSet dataSet = DataSet.createaFromData(data);
            return dataSet;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }



    private static DataSet tempEntityProvider() {
        List<String> entityNames = new ArrayList<>();
        List<String> featureNames = new ArrayList<>();
        final int featuresNum = 20;
        final int entitiesNum = 250;
        double[][] data = new double[featuresNum][entitiesNum];

        for (int i = 0; i < featuresNum; i++) {
            featureNames.add("feature" + i);
            for (int j = 0; j < entitiesNum; j++) {
                data[i][j] = Math.random();
            }
        }

        for (int j = 0; j < entitiesNum; j++) {
            entityNames.add("class" + j);
        }

        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        return new DataSet(matrix, entityNames, featureNames);
    }
}
