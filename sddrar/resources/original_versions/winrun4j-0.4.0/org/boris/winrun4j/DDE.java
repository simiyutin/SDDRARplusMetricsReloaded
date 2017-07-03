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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A manager for DDE.
 */
public class DDE
{
    private static Set fileAssociationListeners = new LinkedHashSet();
    private static Set activationListeners = new LinkedHashSet();

    /**
     * To be called by the application when it is ready to receive DDE messages.
     */
    public static void ready() {
        NativeHelper.call(0, "DDE_Ready");
    }

    /**
     * Add a file association listener.
     * 
     * @param listener.
     */
    public static void addFileAssocationListener(FileAssociationListener listener) {
        DDE.fileAssociationListeners.add(listener);
    }

    /**
     * Execute a command. This will be called from WinRun4J binary.
     * 
     * @param command.
     */
    public static void execute(String command) {
        Iterator i = fileAssociationListeners.iterator();
        while (i.hasNext()) {
            FileAssociationListener listener = (FileAssociationListener) i.next();
            listener.execute(command);
        }
    }

    /**
     * Add an activation listener.
     * 
     * @param listener
     */
    public static void addActivationListener(ActivationListener listener) {
        activationListeners.add(listener);
    }

    /**
     * This will be called as part of the single instance.
     */
    public static void activate(String cmdLine) {
        Iterator i = activationListeners.iterator();
        while (i.hasNext()) {
            ActivationListener listener = (ActivationListener) i.next();
            listener.activate(cmdLine);
        }
    }
}
