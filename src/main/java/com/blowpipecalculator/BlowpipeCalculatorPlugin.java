package com.blowpipecalculator;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
    name = "Blowpipe Calculator",
    description = "Calculate Toxic blowpipe supplies and track estimated attacks remaining",
    tags = {"blowpipe", "zulrah", "scales", "darts", "calculator", "tracker"}
)
public class BlowpipeCalculatorPlugin extends Plugin
{
    // OSRS Toxic blowpipe attack animation ID.
    private static final int BLOWPIPE_ATTACK_ANIMATION = 5061;
    private static final String CONFIG_GROUP = "blowpipecalculator";
    private static final String OVERLAY_CONFIG_KEY = "showTrackerOverlay";

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private BlowpipeCalculatorConfig config;

    @Inject
    private ConfigManager configManager;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    private NavigationButton navButton;
    private BlowpipeCalculatorPanel panel;
    private BlowpipeTrackerOverlay trackerOverlay;

    private boolean tracking;
    private boolean paused;
    private boolean trackerOverlayVisible;
    private boolean lastTickWasBlowpipeAttack;
    private long startingAttackEstimate;
    private long dartsAttackEstimate;
    private long scalesAttackEstimate;
    private long attacksMade;

    @Provides
    BlowpipeCalculatorConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BlowpipeCalculatorConfig.class);
    }

    @Override
    protected void startUp()
    {
        trackerOverlay = new BlowpipeTrackerOverlay(this);
        setTrackerOverlayVisible(config.showTrackerOverlay());

        panel = new BlowpipeCalculatorPanel(config, configManager, this);

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
        if (trackerOverlay != null)
        {
            overlayManager.remove(trackerOverlay);
        }

        if (navButton != null)
        {
            clientToolbar.removeNavigation(navButton);
        }

        navButton = null;
        panel = null;
        trackerOverlay = null;
        tracking = false;
        paused = false;
        trackerOverlayVisible = false;
        lastTickWasBlowpipeAttack = false;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (client.getLocalPlayer() == null)
        {
            lastTickWasBlowpipeAttack = false;
            return;
        }

        boolean blowpipeAttack = client.getLocalPlayer().getAnimation() == BLOWPIPE_ATTACK_ANIMATION;

        if (tracking && !paused && blowpipeAttack && !lastTickWasBlowpipeAttack)
        {
            attacksMade++;
        }

        lastTickWasBlowpipeAttack = blowpipeAttack;
    }

    void startTracking(long darts, long scales, AmmoSaveRate ammoSaveRate)
    {
        dartsAttackEstimate = BlowpipeCalculator.attacksForDarts(darts, ammoSaveRate);
        scalesAttackEstimate = BlowpipeCalculator.attacksForScales(scales);
        startingAttackEstimate = Math.min(dartsAttackEstimate, scalesAttackEstimate);
        attacksMade = 0;
        tracking = true;
        paused = false;
        lastTickWasBlowpipeAttack = false;
    }

    void togglePauseTracking()
    {
        if (tracking)
        {
            paused = !paused;
        }
    }

    void resetTracking()
    {
        tracking = false;
        paused = false;
        startingAttackEstimate = 0;
        dartsAttackEstimate = 0;
        scalesAttackEstimate = 0;
        attacksMade = 0;
        lastTickWasBlowpipeAttack = false;
    }

    void setTrackerOverlayVisible(boolean visible)
    {
        trackerOverlayVisible = visible;
        configManager.setConfiguration(CONFIG_GROUP, OVERLAY_CONFIG_KEY, visible);

        if (trackerOverlay == null)
        {
            return;
        }

        if (visible)
        {
            overlayManager.add(trackerOverlay);
        }
        else
        {
            overlayManager.remove(trackerOverlay);
        }
    }

    boolean isTracking()
    {
        return tracking;
    }

    boolean isPaused()
    {
        return paused;
    }

    long getAttacksMade()
    {
        return attacksMade;
    }

    long getAttacksRemaining()
    {
        return Math.max(0, startingAttackEstimate - attacksMade);
    }

    String getLimitingSupply()
    {
        if (!tracking)
        {
            return "—";
        }

        if (dartsAttackEstimate < scalesAttackEstimate)
        {
            return "Darts";
        }

        if (scalesAttackEstimate < dartsAttackEstimate)
        {
            return "Scales";
        }

        return "Balanced";
    }

    String getTrackerStatus()
    {
        if (!tracking)
        {
            return "Not tracking";
        }

        return paused ? "Paused" : "Tracking";
    }
}
