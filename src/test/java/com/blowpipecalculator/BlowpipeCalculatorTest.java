package com.blowpipecalculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlowpipeCalculatorTest
{
    @Test
    public void testAssemblerDartsToScales()
    {
        assertEquals(3334, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.ASSEMBLER));
    }

    @Test
    public void testAssemblerScalesToDarts()
    {
        assertEquals(999, BlowpipeCalculator.dartsForScales(3333, AmmoSaveRate.ASSEMBLER));
        assertEquals(1000, BlowpipeCalculator.dartsForScales(3334, AmmoSaveRate.ASSEMBLER));
    }

    @Test
    public void testNoAva()
    {
        assertEquals(667, BlowpipeCalculator.scalesForDarts(1000, AmmoSaveRate.NONE));
    }
}
