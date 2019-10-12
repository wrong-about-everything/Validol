package validation.leaf.is.of.value.booolean;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;
import static org.junit.Assert.*;

public class IsFalseTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsFalse named =
            new IsFalse(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsFalse named = new IsFalse(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithPresentFalseValue() throws Throwable
    {
        IsFalse named = new IsFalse(new Named<>("vasya", Either.right(new Present<>(false))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void failedWithPresentTrueValue() throws Throwable
    {
        IsFalse named = new IsFalse(new Named<>("vasya", Either.right(new Present<>(true))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }
}
