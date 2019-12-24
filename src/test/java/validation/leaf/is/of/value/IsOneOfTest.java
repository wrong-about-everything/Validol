package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.oneof.IsOneOf;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.util.List;

import static org.junit.Assert.*;

final public class IsOneOfTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                ),
                List.of(1)
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new NamedStub<>("vasya", Either.right(new Absent<>())),
                List.of(25)
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithIntegerValueAbsentInList() throws Exception
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new NamedStub<>("vasya", Either.right(new Present<>(777))),
                List.of(888, 999)
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This list must be one of the following: 888, 999.", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithStringValuePresentInList() throws Exception
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new NamedStub<>("vasya", Either.right(new Present<>("vasya"))),
                List.of("fedya", "vasya", "vitya")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("vasya", named.result().value().raw());
    }
}
