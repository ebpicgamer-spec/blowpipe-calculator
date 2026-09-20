package com.blowpipecalculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlowpipeCalculatorTest
{
    @Test
    public void testDartsToScalesForAllCapeSlots()
    {
        assertEquals(667, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.NONE));
        assertEquals(1667, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.ATTRACTOR));
        assertEquals(2381, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.ACCUMULATOR));
        assertEquals(3334, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.ASSEMBLER));
        assertEquals(3334, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.QUIVER));
    }

    @Test
    public void testScalesToDartsForAllCapeSlots()
    {
        assertEquals(1500, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.NONE));
        assertEquals(600, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.ATTRACTOR));
        assertEquals(419, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.ACCUMULATOR));
        assertEquals(300, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.ASSEMBLER));
        assertEquals(300, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.QUIVER));
    }

    @Test
    public void testZeroInputs()
    {
        for (AmmoSaveRate rate : AmmoSaveRate.values())
        {
            assertEquals(0, BlowpipeCalculator.scalesForDarts(0, rate));
            assertEquals(0, BlowpipeCalculator.dartsForScales(0, rate));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDartsRejected()
    {
        BlowpipeCalculator.scalesForDarts(-1, AmmoSaveRate.ASSEMBLER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeScalesRejected()
    {
        BlowpipeCalculator.dartsForScales(-1, AmmoSaveRate.ASSEMBLER);
    }
}
