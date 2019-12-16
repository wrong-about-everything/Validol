package validation.leaf.is.of.value.concrete.number;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.lessthanorequal.zero.IsNegativeOrZero;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

public class IsNegativeOrZeroTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsNegativeOrZero named =
            new IsNegativeOrZero(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new NamedStub<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentPositiveValue() throws Exception
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new NamedStub<>("vasya", Either.right(new Present<>(0.05))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be zero or zero.", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithPresentZeroValue() throws Exception
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new NamedStub<>("vasya", Either.right(new Present<>(0))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(0, named.result().value().raw());
    }

    @Test
    public void successfulWithPresentNegativeValue() throws Exception
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new NamedStub<>("vasya", Either.right(new Present<>(-0.4))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(-0.4, named.result().value().raw());
    }
}
