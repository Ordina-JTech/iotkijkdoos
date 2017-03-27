package nl.ordina.kijkdoos.view.control;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static nl.ordina.kijkdoos.view.control.ControlDiscoBallFragment.DiscoBallColor;
import static org.junit.Assert.assertEquals;

/**
 * Created by coenhoutman on 07/03/2017.
 */
@RunWith(Parameterized.class)
public class DiscoBallColorTest {

    @Parameterized.Parameters(name = "{index}: nearest of {0} is {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {getColor(255, 0, 0), DiscoBallColor.RED },
                {getColor(255, 10, 10), DiscoBallColor.RED },
                {getColor(255, 20, 20), DiscoBallColor.RED },
                {getColor(255, 30, 30), DiscoBallColor.RED },
                {getColor(255, 40, 40), DiscoBallColor.RED },

                {getColor(0, 255, 00), DiscoBallColor.GREEN},
                {getColor(10, 255, 10), DiscoBallColor.GREEN},
                {getColor(20, 255, 20), DiscoBallColor.GREEN},
                {getColor(30, 255, 30), DiscoBallColor.GREEN},
                {getColor(40, 255, 40), DiscoBallColor.GREEN},

                {getColor(0, 0, 255), DiscoBallColor.BLUE },
                {getColor(10, 10, 255), DiscoBallColor.BLUE },
                {getColor(20, 20, 255), DiscoBallColor.BLUE },
                {getColor(30, 30, 255), DiscoBallColor.BLUE },
                {getColor(40, 40, 255), DiscoBallColor.BLUE },


                {getColor(255, 255, 0), DiscoBallColor.YELLOW },
                {getColor(255, 255, 50), DiscoBallColor.YELLOW },
                {getColor(255, 255, 100), DiscoBallColor.YELLOW },
                {getColor(255, 255, 120), DiscoBallColor.YELLOW }
        });
    }

    @Parameterized.Parameter
    public int pickedColor;

    @Parameterized.Parameter(1)
    public DiscoBallColor expectedDiscoBallColor;

    private static int getColor(int red, int green, int blue) {
        return ((red&0x0ff)<<16)|((green&0x0ff)<<8)|(blue&0x0ff);
    }

    @Test
    public void testGetNearestDiscoBallColor() throws Exception {
        DiscoBallColor discoBallColor = DiscoBallColor.getNearest(pickedColor);
        assertEquals(expectedDiscoBallColor, discoBallColor);
    }
}