package com.blowpipecalculator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class BlowpipeCalculatorPanel extends PluginPanel
{
    private final BlowpipeCalculatorConfig config;
    private final JRadioButton dartsToScales = new JRadioButton("Darts → Scales", true);
    private final JRadioButton scalesToDarts = new JRadioButton("Scales → Darts");
    private final JLabel inputLabel = new JLabel("Darts:");
    private final JSpinner amount = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100));
    private final JLabel result = new JLabel("<html><b>Required scales:</b> 3,334</html>");

    public BlowpipeCalculatorPanel(BlowpipeCalculatorConfig config)
    {
        this.config = config;
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

        JLabel saveRate = new JLabel("Ammo save: " + config.ammoSaveRate());
        saveRate.setToolTipText("Change this in RuneLite's Blowpipe Calculator settings.");

        JButton calculate = new JButton("Calculate");
        calculate.addActionListener(e -> calculate());

        JPanel center = new JPanel(new GridLayout(4, 1, 0, 8));
        center.setBackground(ColorScheme.DARK_GRAY_COLOR);
        center.add(inputPanel);
        center.add(saveRate);
        center.add(calculate);
        center.add(result);

        dartsToScales.addActionListener(e -> updateMode());
        scalesToDarts.addActionListener(e -> updateMode());

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

        if (dartsToScales.isSelected())
        {
            long scales = BlowpipeCalculator.scalesForDarts(value, config.ammoSaveRate());
            result.setText(String.format("<html><b>Required scales:</b> %,d</html>", scales));
        }
        else
        {
            long darts = BlowpipeCalculator.dartsForScales(value, config.ammoSaveRate());
            result.setText(String.format("<html><b>Required darts:</b> %,d</html>", darts));
        }
    }
}
