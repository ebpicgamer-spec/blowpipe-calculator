package com.blowpipecalculator;

public enum AmmoSaveRate
{
    NONE("None (0%)", 0.00),
    ATTRACTOR("Ava's attractor (60%)", 0.60),
    ACCUMULATOR("Ava's accumulator (72%)", 0.72),
    ASSEMBLER("Assembler / equivalent (80%)", 0.80);

    private final String displayName;
    private final double saveRate;

    AmmoSaveRate(String displayName, double saveRate)
    {
        this.displayName = displayName;
        this.saveRate = saveRate;
    }

    public double getSaveRate()
    {
        return saveRate;
    }

    public double getConsumptionRate()
    {
        return 1.0 - saveRate;
    }

    @Override
    public String toString()
    {
        return displayName;
    }
}
