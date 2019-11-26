package validation.leaf.is.of.value.booolean;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

public class IsFalseTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
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
    public void successfulWithAbsentValue() throws Exception
    {
        IsFalse named = new IsFalse(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithPresentFalseValue() throws Exception
    {
        IsFalse named = new IsFalse(new Named<>("vasya", Either.right(new Present<>(false))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void failedWithPresentTrueValue() throws Exception
    {
        IsFalse named = new IsFalse(new Named<>("vasya", Either.right(new Present<>(true))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }
}
