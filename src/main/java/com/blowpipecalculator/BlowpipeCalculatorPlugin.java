package com.blowpipecalculator;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
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
        if (icon == null)
        {
            icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = icon.createGraphics();
            graphics.setColor(new Color(0, 180, 150));
            graphics.fillOval(2, 2, 12, 12);
            graphics.dispose();
        }

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
        if (navButton != null)
        {
            clientToolbar.removeNavigation(navButton);
        }
        navButton = null;
        panel = null;
    }
}
