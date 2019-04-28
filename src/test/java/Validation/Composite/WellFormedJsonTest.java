package Validation.Composite;

import Validation.Leaf.Named;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

class WellFormedJsonTest
{
    @Test
    public void wellFormedJson()
    {
        assertTrue(
            (new WellFormedJson(
                new Named<String>("vasya", "invalid json", true)
            ))
                .result()
                    .isSuccessful()
        );
    }
}
