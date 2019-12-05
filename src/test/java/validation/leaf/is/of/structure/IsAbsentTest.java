package validation.leaf.is.of.structure;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.is.NamedStub;
import validation.leaf.is.absent.IsAbsent;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

// doc: makes sure no such field exists
public class IsAbsentTest
{
    @Test
    public void isPresent() throws Exception
    {
        IsAbsent<String> named =
            new IsAbsent<>(
                new NamedStub<>(
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
    public void isAbsent() throws Exception
    {
        IsAbsent<?> named =
            new IsAbsent<>(
                new NamedStub<>(
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
