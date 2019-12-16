package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.notequalto.IsNotEqualTo;
import validation.result.value.Absent;
import validation.result.value.Present;

import static org.junit.Assert.*;

public class IsNotEqualToTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                ),
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentFalseValue() throws Exception
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Present<>(false))),
                false
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be non-equal to false.", named.result().error().value().get("message"));
    }

    @Test
    public void failedWithPresentIntegerValue() throws Exception
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Present<>(777))),
                777
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be non-equal to 777.", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithPresentStringValue() throws Exception
    {
        IsNotEqualTo<?> named =
            new IsNotEqualTo<>(
                new NamedStub<>("vasya", Either.right(new Present<>("vasya"))),
                "fedya"
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("vasya", named.result().value().raw());
    }
}
