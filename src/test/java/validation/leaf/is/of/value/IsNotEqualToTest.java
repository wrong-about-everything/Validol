package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import static org.junit.Assert.*;

public class IsNotEqualToTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                ),
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new Named<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentFalseValue() throws Throwable
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new Named<>("vasya", Either.right(new Present<>(false))),
                false
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must not be equal to false.", named.result().error());
    }

    @Test
    public void failedWithPresentIntegerValue() throws Throwable
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new Named<>("vasya", Either.right(new Present<>(777))),
                777
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must not be equal to 777.", named.result().error());
    }

    @Test
    public void successfulWithPresentStringValue() throws Throwable
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new Named<>("vasya", Either.right(new Present<>("vasya"))),
                "fedya"
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("vasya", named.result().value().raw());
    }
}
