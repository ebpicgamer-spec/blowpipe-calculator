package com.blowpipecalculator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
    private final JRadioButton dartsToScales = new JRadioButton("Darts → Scales", true);
    private final JRadioButton scalesToDarts = new JRadioButton("Scales → Darts");
    private final JLabel inputLabel = new JLabel("Darts:");
    private final JSpinner amount = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100));
    private final JComboBox<AmmoSaveRate> ammoSaveRate = new JComboBox<>(AmmoSaveRate.values());
    private final JLabel result = new JLabel();

    public BlowpipeCalculatorPanel(BlowpipeCalculatorConfig config, ConfigManager configManager)
    {
        this.configManager = configManager;

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
        ammoPanel.add(new JLabel("Ammo save:"), BorderLayout.WEST);
        ammoSaveRate.setSelectedItem(config.ammoSaveRate());
        ammoPanel.add(ammoSaveRate, BorderLayout.CENTER);

        JPanel center = new JPanel(new GridLayout(3, 1, 0, 8));
        center.setBackground(ColorScheme.DARK_GRAY_COLOR);
        center.add(inputPanel);
        center.add(ammoPanel);
        center.add(result);

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

        add(modePanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        calculate();
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
            result.setText(String.format("<html><b>Required scales:</b> %,d</html>", scales));
        }
        else
        {
            long darts = BlowpipeCalculator.dartsForScales(value, selected);
            result.setText(String.format("<html><b>Required darts:</b> %,d</html>", darts));
        }
    }
}
