package com.simiyutin.au.sddrar;

import com.simiyutin.au.sddrar.DataSet;

import java.io.*;

public class SDDRARioHandler {

    public static void dump(DataSet dataSet, String name) {
        try {
            FileOutputStream fos = new FileOutputStream("/home/boris/au_2/nir/project/data/" + name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataSet);
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
            DataSet dataSet = (DataSet) ois.readObject();
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
}
