package com.uom.cse.androidagent;


import android.content.Context;

import com.espertech.esper.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Abstraction entity for an Esper/Asper
 * - engine instance.
 */

public class Asper
{
    // Collection of queries / EPL statementes
    private static ArrayList<String> queries;
    // Collection of event-listeners
    private static ArrayList<Listener> listeners;
    // Collection of registered event-types
    private static ArrayList<String> types;
    // Default Esper administrator
    private static EPAdministrator administrator;
    // Default service provider
    private static EPServiceProvider service;
    // Default runtime instance
    private static EPRuntime runtime;
    // Default configuration instance
    private static Configuration configuration;

    /**
     * Setup-procedure initiation
     * upon application initialization
     */
    static
    {
        setup();
    }

    /**
     * Engine initialization procedure.
     */
    private static void setup()
    {
        configuration = new Configuration();

        // Database configuration
        ConfigurationDBRef reference = new ConfigurationDBRef();
        // SQLite refrence setup
        reference.setDriverManagerConnection(
                "SQLite.JDBCDriver",
                "jdbc:sqlite:" + Database.DATABASE_PATH,
                new Properties()
        );
        // Meta origin must be set to sample.
        // See chapter 15.4.9 in Esper documentation
        reference.setMetadataOrigin(ConfigurationDBRef.MetadataOriginEnum.SAMPLE);
        reference.setLRUCache(Settings.DATABASE_CACHE);
        configuration.addDatabaseReference(Database.DATABASE_NAME, reference);

        service = EPServiceProviderManager.getDefaultProvider(configuration);
        administrator = service.getEPAdministrator();
        runtime = service.getEPRuntime();

        listeners = new ArrayList();
        queries = new ArrayList();
        types = new ArrayList();

    }

    /**
     * Register an new unique event type.
     * Parses an existing Event and generates a schema
     * that Esper understands
     *
     * @param event
     */
    public static void addEvent(Event event)
    {
        List classifiers = new ArrayList<Class>();
        List<String> labels = new ArrayList<String>();

        for (Variable variable : event.getVariables())
        {
            labels.add(variable.getName());

            if (variable.hasValue())
            {
                classifiers.add(variable.getValue().getClass());
            } else if (variable.hasRange())
            {
                classifiers.add(Double.class);
            } else
            {
                classifiers.add(Object.class);
            }

        }

        administrator.getConfiguration().addEventType(
                event.getName(),
                labels.toArray(new String[labels.size()]),
                classifiers.toArray()
        );

        types.add(event.getName());
    }

    /**
     * Register a new EPL compliant query.
     */
    public static void addQuery(String name, String query,Context context)
    {
        EPStatement statement = administrator.createEPL(query);
        Listener listener = new Listener("Listener-" + name,context);
        statement.addListener(listener);

        queries.add(query);
        listeners.add(listener);
    }

    /**
     * Resets the engine.
     * Detaches all listeners and queries.
     */
    public static void reset()
    {
        service.removeAllStatementStateListeners();

        service.removeAllServiceStateListeners();

        administrator.stopAllStatements();

        administrator.destroyAllStatements();

        for (String type : types)
        {
            administrator.getConfiguration().removeEventType(type, false);
        }

        service.destroy();

        listeners.clear();
        queries.clear();
        types.clear();


        setup();

        System.gc();
    }

    public static ArrayList<String> getQueries()
    {
        return queries;
    }

    public static ArrayList<Listener> getListeners()
    {
        return listeners;
    }

    public static EPRuntime getRuntime()
    {
        return runtime;
    }

    public static ArrayList<String> getTypes()
    {
        return types;
    }
}
