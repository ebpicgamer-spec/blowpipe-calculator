package com.blowpipecalculator;

public final class BlowpipeCalculator
{
    private static final double SCALES_PER_ATTACK = 2.0 / 3.0;

    private BlowpipeCalculator()
    {
    }

    public static long scalesForDarts(long darts, AmmoSaveRate ammoSaveRate)
    {
        if (darts < 0)
        {
            throw new IllegalArgumentException("Darts cannot be negative");
        }

        if (darts == 0)
        {
            return 0;
        }

        double attacks = darts / ammoSaveRate.getConsumptionRate();
        return (long) Math.ceil(attacks * SCALES_PER_ATTACK);
    }

    public static long dartsForScales(long scales, AmmoSaveRate ammoSaveRate)
    {
        if (scales < 0)
        {
            throw new IllegalArgumentException("Scales cannot be negative");
        }

        if (scales == 0)
        {
            return 0;
        }

        double attacks = scales / SCALES_PER_ATTACK;
        return Math.round(attacks * ammoSaveRate.getConsumptionRate());
    }

    public static long attacksForDarts(long darts, AmmoSaveRate ammoSaveRate)
    {
        if (darts < 0)
        {
            throw new IllegalArgumentException("Darts cannot be negative");
        }

        return (long) Math.floor(darts / ammoSaveRate.getConsumptionRate());
    }

    public static long attacksForScales(long scales)
    {
        if (scales < 0)
        {
            throw new IllegalArgumentException("Scales cannot be negative");
        }

        return (long) Math.floor(scales / SCALES_PER_ATTACK);
    }

    public static long attacksAvailable(long darts, long scales, AmmoSaveRate ammoSaveRate)
    {
        return Math.min(attacksForDarts(darts, ammoSaveRate), attacksForScales(scales));
    }
}
