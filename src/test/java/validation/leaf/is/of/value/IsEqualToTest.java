package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;
import static org.junit.Assert.*;

public class IsEqualToTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
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
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new Named<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithPresentFalseValue() throws Throwable
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new Named<>("vasya", Either.right(new Present<>(false))),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void successfulWithPresentIntegerValue() throws Throwable
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new Named<>("vasya", Either.right(new Present<>(777))),
                777
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(777, named.result().value().raw());
    }

    @Test
    public void failedWithPresentStringValue() throws Throwable
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new Named<>("vasya", Either.right(new Present<>("vasya"))),
                "fedya"
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be equal to fedya.", named.result().error());
    }
}
