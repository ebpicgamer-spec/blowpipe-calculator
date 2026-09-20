package com.blowpipecalculator;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("blowpipecalculator")
public interface BlowpipeCalculatorConfig extends Config
{
    @ConfigItem(
        keyName = "ammoSaveRate",
        name = "Ammo save rate",
        description = "Select the ammo-saving effect you use with the Toxic blowpipe",
        position = 0
    )
    default AmmoSaveRate ammoSaveRate()
    {
        return AmmoSaveRate.ASSEMBLER;
    }
}
