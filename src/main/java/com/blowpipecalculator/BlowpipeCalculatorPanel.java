package com.blowpipecalculator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class BlowpipeCalculatorPanel extends PluginPanel
{
    private static final String CONFIG_GROUP = "blowpipecalculator";
    private static final String AMMO_SAVE_KEY = "ammoSaveRate";

    private final ConfigManager configManager;
    private final BlowpipeCalculatorPlugin plugin;
    private final JRadioButton dartsToScales = new JRadioButton("Darts → Scales", true);
    private final JRadioButton scalesToDarts = new JRadioButton("Scales → Darts");
    private final JLabel inputLabel = new JLabel("Darts:");
    private final JSpinner amount = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100));
    private final JComboBox<AmmoSaveRate> ammoSaveRate = new JComboBox<>(AmmoSaveRate.values());
    private final JLabel result = new JLabel();
    private final JLabel estimateNote = new JLabel("Based on average ammo-saving rates.");
    private final JSpinner startingDarts = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100));
    private final JSpinner startingScales = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100));
    private final JButton pauseButton = new JButton("Pause");
    private final JLabel trackerStatus = new JLabel("Enter starting supplies and press Start.");

    public BlowpipeCalculatorPanel(BlowpipeCalculatorConfig config, ConfigManager configManager,
        BlowpipeCalculatorPlugin plugin)
    {
        this.configManager = configManager;
        this.plugin = plugin;

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel modePanel = new JPanel(new GridLayout(2, 1));
        modePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        modePanel.setBorder(BorderFactory.createTitledBorder("Calculation"));

        ButtonGroup group = new ButtonGroup();
        group.add(dartsToScales);
        group.add(scalesToDarts);
        modePanel.add(dartsToScales);
        modePanel.add(scalesToDarts);

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        inputPanel.add(inputLabel, BorderLayout.WEST);
        inputPanel.add(amount, BorderLayout.CENTER);

        JPanel ammoPanel = new JPanel(new BorderLayout(8, 0));
        ammoPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        JLabel capeSlotLabel = new JLabel("Cape slot ⓘ");
        capeSlotLabel.setToolTipText("<html>Select the ammo-saving item in your cape slot.<br>Dizana's quiver only saves ammo after being upgraded<br>with an Ava's assembler.</html>");
        ammoPanel.add(capeSlotLabel, BorderLayout.WEST);
        ammoSaveRate.setSelectedItem(config.ammoSaveRate());
        ammoPanel.add(ammoSaveRate, BorderLayout.CENTER);

        estimateNote.setForeground(ColorScheme.LIGHT_GRAY_COLOR);

        JPanel center = new JPanel(new GridLayout(4, 1, 0, 6));
        center.setBackground(ColorScheme.DARK_GRAY_COLOR);
        center.add(inputPanel);
        center.add(ammoPanel);
        center.add(result);
        center.add(estimateNote);

        dartsToScales.addActionListener(e -> updateMode());
        scalesToDarts.addActionListener(e -> updateMode());

        ChangeListener amountListener = e -> calculate();
        amount.addChangeListener(amountListener);

        ammoSaveRate.addActionListener(e ->
        {
            AmmoSaveRate selected = (AmmoSaveRate) ammoSaveRate.getSelectedItem();
            if (selected != null)
            {
                configManager.setConfiguration(CONFIG_GROUP, AMMO_SAVE_KEY, selected.name());
                calculate();
            }
        });

        JPanel trackerPanel = buildTrackerPanel(config.showTrackerOverlay());

        add(modePanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(trackerPanel, BorderLayout.SOUTH);

        calculate();
    }

    private JPanel buildTrackerPanel(boolean showOverlayByDefault)
    {
        JPanel trackerPanel = new JPanel(new BorderLayout(0, 6));
        trackerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        trackerPanel.setBorder(BorderFactory.createTitledBorder("Live attack tracker"));

        JPanel amounts = new JPanel(new GridLayout(2, 2, 6, 4));
        amounts.setBackground(ColorScheme.DARK_GRAY_COLOR);
        amounts.add(new JLabel("Starting darts:"));
        amounts.add(startingDarts);
        amounts.add(new JLabel("Starting scales:"));
        amounts.add(startingScales);

        JButton startButton = new JButton("Start tracking");
        JButton resetButton = new JButton("Reset");
        pauseButton.setEnabled(false);
        resetButton.setEnabled(true);

        JPanel actions = new JPanel(new GridLayout(2, 2, 4, 4));
        actions.setBackground(ColorScheme.DARK_GRAY_COLOR);
        actions.add(startButton);
        actions.add(pauseButton);
        actions.add(resetButton);

        JCheckBox showOverlay = new JCheckBox("Show in-game overlay", showOverlayByDefault);
        showOverlay.setBackground(ColorScheme.DARK_GRAY_COLOR);
        showOverlay.setForeground(Color.WHITE);
        actions.add(showOverlay);

        trackerPanel.add(amounts, BorderLayout.NORTH);
        trackerPanel.add(actions, BorderLayout.CENTER);
        trackerStatus.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        trackerPanel.add(trackerStatus, BorderLayout.SOUTH);

        startButton.addActionListener(e ->
        {
            long darts = ((Number) startingDarts.getValue()).longValue();
            long scales = ((Number) startingScales.getValue()).longValue();
            AmmoSaveRate selected = (AmmoSaveRate) ammoSaveRate.getSelectedItem();
            if (selected == null)
            {
                selected = AmmoSaveRate.ASSEMBLER;
            }

            plugin.startTracking(darts, scales, selected);
            pauseButton.setText("Pause");
            pauseButton.setEnabled(true);
            trackerStatus.setText("Tracking blowpipe attack animations.");
        });

        pauseButton.addActionListener(e ->
        {
            plugin.togglePauseTracking();
            pauseButton.setText(plugin.isPaused() ? "Resume" : "Pause");
            trackerStatus.setText(plugin.isPaused() ? "Tracking paused." : "Tracking blowpipe attack animations.");
        });

        resetButton.addActionListener(e ->
        {
            plugin.resetTracking();
            pauseButton.setText("Pause");
            pauseButton.setEnabled(false);
            trackerStatus.setText("Enter starting supplies and press Start.");
        });

        showOverlay.addActionListener(e -> plugin.setTrackerOverlayVisible(showOverlay.isSelected()));

        return trackerPanel;
    }

    private void updateMode()
    {
        inputLabel.setText(dartsToScales.isSelected() ? "Darts:" : "Scales:");
        calculate();
    }

    private void calculate()
    {
        long value = ((Number) amount.getValue()).longValue();
        AmmoSaveRate selected = (AmmoSaveRate) ammoSaveRate.getSelectedItem();

        if (selected == null)
        {
            selected = AmmoSaveRate.ASSEMBLER;
        }

        if (dartsToScales.isSelected())
        {
            long scales = BlowpipeCalculator.scalesForDarts(value, selected);
            result.setText(String.format("<html><b>Estimated scales:</b> %,d</html>", scales));
        }
        else
        {
            long darts = BlowpipeCalculator.dartsForScales(value, selected);
            result.setText(String.format("<html><b>Estimated darts:</b> %,d</html>", darts));
        }
    }
}
