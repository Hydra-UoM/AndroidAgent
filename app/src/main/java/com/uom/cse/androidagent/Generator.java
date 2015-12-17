package com.uom.cse.androidagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Event generator.
 * Responible for the production of data-items
 * based on a blueprint deonted by Task.
 */

public class Generator
{
    // Plain Java random
    private static Random random = new Random(Settings.SEED);
    // Creation sequence.
    private static long sequence;

    /**
     * Create a collection of data-items.
     * Each modeleded by an Event.
     * <p/>
     *
     * @param amount No. of data-items one wants to generate
     * @param event  The event type that should be modeled.
     * @return Collection of data-items - denoted by abount
     */
    public static Data make(Event event)
    {
        List<Variable> variables = event.getVariables();
      //  variables.get(variables.size() - 1).setValue(sequence++);
        event.setVariables(variables);

        return create(event);
    }

    public static ArrayList<Data> make(int amount, Event event)
    {
        ArrayList<Data> data = new ArrayList();

        for (int j = 0; j < amount; j++)
        {
            // Appends an unique creation seqence number.
            // Identified by a seperate attribute

            List<Variable> variables = event.getVariables();
            variables.get(variables.size() - 1).setValue(sequence++);
            event.setVariables(variables);

            data.add(create(event));
        }
        return data;
    }

    /**
     * Helper function for Data-item creation.
     * Parses a given Event type and generates
     * an Object[] representative of the event.
     *
     * @param event the event to model
     * @return a Data-item representation
     */
    public static Data create(Event event)
    {
        // Collection of event attributes
        List<Variable> variables = event.getVariables();
        // Data-item payload.
        Object[] dataset = new Object[variables.size()];

        /*
         * For each attribute, retain its value
         * and place it in the data-item payload
         */
        for (int i = 0; i < dataset.length; i++)
        {
            Variable variable = variables.get(i);
            Object value = variable.getValue();

            /*
             * Determines whether or not this
             * attribute is a range and should be
             * selected randomly.
             */
            if (variable.hasRange())
            {
                List range = variable.getRange();

                value = pickRandomNumber(
                        (Number) range.get(0),
                        (Number) range.get(1)
                );
            }

            dataset[i] = value;
        }

        return new Data(event.getName(), dataset);
    }

    /**
     * Draws a random number from the lower to the upper bound given
     *
     * @param min an integer ranging from 0 - max.int
     * @param max an integer ranging from lower - max.int
     * @return
     */
    private static double pickRandomNumber(Number min, Number max)
    {
        double dmin = min.doubleValue();
        double dmax = max.doubleValue();

        double value = dmin + (random.nextDouble() * ((dmax - dmin) + 1));

        if (dmin % 1 == 0 && dmax % 1 == 0)
            return (int) value;
        else
            return value;
    }

    /**
     * Default reset procedure.
     */
    public static void reset()
    {
        sequence = 1;
    }

}
