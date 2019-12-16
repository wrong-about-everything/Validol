package validation.leaf.is.of.value.concrete.number;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.lessthan.zero.IsNegative;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

public class IsNegativeTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsNegative named =
            new IsNegative(
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
        IsNegative named = new IsNegative(new NamedStub<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentZeroValue() throws Exception
    {
        IsNegative named = new IsNegative(new NamedStub<>("vasya", Either.right(new Present<>(0))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be zero.", named.result().error());
    }

    @Test
    public void failedWithPresentPositiveValue() throws Exception
    {
        IsNegative named = new IsNegative(new NamedStub<>("vasya", Either.right(new Present<>(0.05))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be zero.", named.result().error());
    }

    @Test
    public void failedWithPresentNegativeValue() throws Exception
    {
        IsNegative named = new IsNegative(new NamedStub<>("vasya", Either.right(new Present<>(-0.05))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(-0.05, named.result().value().raw());
    }
}
