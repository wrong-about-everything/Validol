package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.equalto.genericvalue.IsEqualTo;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

public class IsEqualToTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new NamedStub<>(
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
    public void successfulWithAbsentValue() throws Exception
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithPresentFalseValue() throws Exception
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Present<>(false))),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void successfulWithPresentIntegerValue() throws Exception
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Present<>(777))),
                777
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(777, named.result().value().raw());
    }

    @Test
    public void failedWithPresentStringValue() throws Exception
    {
        IsEqualTo<?> named =
            new IsEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Present<>("vasya"))),
                "fedya"
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be equal to fedya.", named.result().error());
    }
}
