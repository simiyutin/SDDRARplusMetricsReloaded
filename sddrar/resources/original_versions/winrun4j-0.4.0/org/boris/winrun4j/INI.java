/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.winrun4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides access to the INI file used to startup the app.
 */
public class INI
{
    static {
        PInvoke.bind(INI.class);
    }

    // Known INI keys
    public static final String MAIN_CLASS = ":main.class";
    public static final String SERVICE_CLASS = ":service.class";
    public static final String MODULE_NAME = "WinRun4J:module.name";
    public static final String MODULE_INI = "Winrun4J:module.ini";
    public static final String MODULE_DIR = "WinRun4J:module.dir";
    public static final String INI_DIR = "WinRun4J:ini.dir";
    public static final String WORKING_DIR = ":working.directory";
    public static final String SINGLE_INSTANCE = ":single.instance";
    public static final String DDE_ENABLED = ":dde.enabled";
    public static final String DDE_WINDOW_CLASS = ":dde.window.class";
    public static final String DDE_SERVER_NAME = ":dde.server.name";
    public static final String DDE_TOPIC = ":dde.topic";
    public static final String SERVICE_ID = ":service.id";
    public static final String SERVICE_NAME = ":service.name";
    public static final String SERVICE_DESCRIPTION = ":service.description";
    public static final String SERVICE_CONTROLS = ":service.controls";
    public static final String SERVICE_STARTUP = ":service.startup";
    public static final String SERVICE_DEPENDENCY = ":service.dependency";
    public static final String SERVICE_USER = ":service.user";
    public static final String SERVICE_PWD = ":service.password";
    public static final String SERVICE_LOAD_ORDER_GROUP = ":service.loadordergroup";

    /**
     * Gets a property from the INI file.
     * 
     * @param key.
     * 
     * @return String.
     */
    public static String getProperty(String key) {
        long k = NativeHelper.toNativeString(key, false);
        long r = NativeHelper.call(0, "INI_GetProperty", k);
        String res = null;
        if (r != 0) {
            res = NativeHelper.getString(r, 4096, false);
        }
        NativeHelper.free(k);
        return res;
    }

    /**
     * Gets the keys from the INI file.
     * 
     * @return String.
     */
    public static String[] getPropertyKeys() {
        long d = NativeHelper.call(0, "INI_GetDictionary");
        int n = NativeHelper.getInt(d);
        long keyPtr = NativeHelper.getInt(d + (Native.IS_64 ? 16 : 12));
        String[] res = new String[n];
        for (int i = 0, offset = 0; i < n; i++, offset += NativeHelper.PTR_SIZE) {
            long ptr = NativeHelper.getPointer(keyPtr + offset);
            res[i] = NativeHelper.getString(ptr, 260, false);
        }
        return res;
    }

    /**
     * Get the set of properties as a map.
     * 
     * @return Map.
     */
    public static Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<String, String>();
        String[] keys = getPropertyKeys();

        for (int i = 0; i < keys.length; i++) {
            props.put(keys[i], getProperty(keys[i]));
        }

        return props;
    }

    /**
     * Grab numbered entries from the properties.
     */
    public static String[] getNumberedEntries(Properties p, String baseKey) {
        ArrayList l = new ArrayList();
        int i = 1;
        while (true) {
            String v = p.getProperty(baseKey + "." + i);
            if (v != null)
                l.add(v);
            i++;
            if (i > 10 && v == null)
                break;
        }
        return (String[]) l.toArray(new String[l.size()]);
    }
}
