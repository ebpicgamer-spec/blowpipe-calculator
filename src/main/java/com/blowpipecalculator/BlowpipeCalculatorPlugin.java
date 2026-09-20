package com.blowpipecalculator;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
    name = "Blowpipe Calculator",
    description = "Calculate Toxic blowpipe dart and Zulrah scale requirements",
    tags = {"blowpipe", "zulrah", "scales", "darts", "calculator"}
)
public class BlowpipeCalculatorPlugin extends Plugin
{
    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private BlowpipeCalculatorConfig config;

    private NavigationButton navButton;
    private BlowpipeCalculatorPanel panel;

    @Provides
    BlowpipeCalculatorConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BlowpipeCalculatorConfig.class);
    }

    @Override
    protected void startUp()
    {
        panel = new BlowpipeCalculatorPanel(config);

        BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");

        navButton = NavigationButton.builder()
            .tooltip("Blowpipe Calculator")
            .icon(icon)
            .priority(8)
            .panel(panel)
            .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown()
    {
        clientToolbar.removeNavigation(navButton);
        navButton = null;
        panel = null;
    }
}
