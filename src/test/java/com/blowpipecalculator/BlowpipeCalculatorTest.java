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
        assertEquals(420, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.ACCUMULATOR));
        assertEquals(300, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.ASSEMBLER));
        assertEquals(300, BlowpipeCalculator.dartsForScales(1000, AmmoSaveRate.QUIVER));
    }

    @Test
    public void testAttackEstimateUsesTheLimitingSupply()
    {
        assertEquals(1170, BlowpipeCalculator.attacksAvailable(234, 1700, AmmoSaveRate.ASSEMBLER));
        assertEquals(234, BlowpipeCalculator.attacksAvailable(234, 1700, AmmoSaveRate.NONE));
        assertEquals(835, BlowpipeCalculator.attacksAvailable(234, 1700, AmmoSaveRate.ACCUMULATOR));
        assertEquals(0, BlowpipeCalculator.attacksAvailable(0, 1700, AmmoSaveRate.ASSEMBLER));
        assertEquals(0, BlowpipeCalculator.attacksAvailable(234, 0, AmmoSaveRate.ASSEMBLER));
    }

    @Test
    public void testScalesCanBeTheLimitingSupply()
    {
        assertEquals(150, BlowpipeCalculator.attacksAvailable(1000, 100, AmmoSaveRate.ASSEMBLER));
    }

    @Test
    public void testZeroInputs()
    {
        for (AmmoSaveRate rate : AmmoSaveRate.values())
        {
            assertEquals(0, BlowpipeCalculator.scalesForDarts(0, rate));
            assertEquals(0, BlowpipeCalculator.dartsForScales(0, rate));
            assertEquals(0, BlowpipeCalculator.attacksForDarts(0, rate));
        }

        assertEquals(0, BlowpipeCalculator.attacksForScales(0));
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

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDartsRejectedForAttackEstimate()
    {
        BlowpipeCalculator.attacksForDarts(-1, AmmoSaveRate.ASSEMBLER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeScalesRejectedForAttackEstimate()
    {
        BlowpipeCalculator.attacksForScales(-1);
    }
}
