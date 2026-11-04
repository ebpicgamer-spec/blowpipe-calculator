package com.blowpipecalculator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class BlowpipeTrackerOverlay extends OverlayPanel
{
    private final BlowpipeCalculatorPlugin plugin;

    BlowpipeTrackerOverlay(BlowpipeCalculatorPlugin plugin)
    {
        super(plugin);
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(Overlay.PRIORITY_HIGH);
        getPanelComponent().setPreferredSize(new Dimension(220, 0));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        getPanelComponent().getChildren().add(
            LineComponent.builder().left("Blowpipe Tracker").build());

        if (!plugin.isTracking())
        {
            getPanelComponent().getChildren().add(
                LineComponent.builder().left("Not tracking").right("Start in sidebar").build());
        }
        else
        {
            getPanelComponent().getChildren().add(
                LineComponent.builder().left("Attacks remaining").right(
                    String.format("%,d", plugin.getAttacksRemaining())).build());
            getPanelComponent().getChildren().add(
                LineComponent.builder().left("Attacks recorded").right(
                    String.format("%,d", plugin.getAttacksMade())).build());
            getPanelComponent().getChildren().add(
                LineComponent.builder().left("Limiting supply").right(plugin.getLimitingSupply()).build());
            getPanelComponent().getChildren().add(
                LineComponent.builder().left("Status").right(plugin.getTrackerStatus()).build());
        }

        return super.render(graphics);
    }
}
