package validation.leaf.is.of.value.equalto.booolean;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.equalto.booolean.trooe.IsTrue;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

final public class IsTrueTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsTrue named =
            new IsTrue(
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
        IsTrue named = new IsTrue(new NamedStub<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentFalseValue() throws Exception
    {
        IsTrue named = new IsTrue(new NamedStub<>("vasya", Either.right(new Present<>(false))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }

    @Test
    public void successfulWithPresentTrueValue() throws Exception
    {
        IsTrue named = new IsTrue(new NamedStub<>("vasya", Either.right(new Present<>(true))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(true, named.result().value().raw());
    }
}
