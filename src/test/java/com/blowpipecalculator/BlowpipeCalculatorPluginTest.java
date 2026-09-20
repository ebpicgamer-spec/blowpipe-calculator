package com.blowpipecalculator;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BlowpipeCalculatorPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(BlowpipeCalculatorPlugin.class);
        RuneLite.main(args);
    }
}
