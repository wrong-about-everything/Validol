package validation.leaf.is;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.leaf.is.IsAbsent;
import validation.value.Absent;
import validation.value.Present;
import static org.junit.Assert.*;

public class IsAbsentTest
{
    @Test
    public void isPresent() throws Throwable
    {
        IsAbsent<String> named =
            new IsAbsent<>(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Present<>("vasya")
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("There should be no such field", named.result().error());
    }

    @Test
    public void isAbsent() throws Throwable
    {
        IsAbsent<?> named =
            new IsAbsent<>(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Absent<>()
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }
}
