package com.simiyutin.au.sddrar;

import com.simiyutin.au.sddrar.DataSet;

import java.io.*;
import java.util.Set;

public class SDDRARioHandler {

    public static void dumpRules(Set<Rule> rules) {
        dump(rules, "rules.dat");
    }

    public static DataSet loadDataSet(String name) {
        return loadAnything(name);
    }

    public static Set<Rule> loadRules() {
        return loadAnything("rules.dat");
    }

    public static void dump(Object obj, String name) {
        try {
            FileOutputStream fos = new FileOutputStream("/home/boris/au_2/nir/project/data/" + name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> T loadAnything(String name) {
        try {
            FileInputStream fis = new FileInputStream("/home/boris/au_2/nir/project/data/" + name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            T obj = (T) ois.readObject();
            return obj;
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
