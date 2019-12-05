package validation.leaf.is.of.type;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.type.string.IsString;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsStringTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsString named =
            new IsString(
                new NamedStub<>(
                    "vasya",
                    Either.left("Wooops")
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void failedWithIncorrectStructure() throws Exception
    {
        IsString named =
            new IsString(
                new NamedStub<>(
                    "vasya",
                    Either.right(
                        new Present<>(
                            new Gson().toJsonTree(
                                List.of(
                                    Map.of("id", 1900),
                                    Map.of("id", 777)
                                ),
                                new TypeToken<List<Map<String, Object>>>() {}.getType()
                            )
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a json primitive.", named.result().error());
    }

    @Test
    public void failedWithNonString() throws Exception
    {
        IsString named =
            new IsString(
                new NamedStub<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive(777))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a string.", named.result().error());
    }

    @Test
    public void successfulWithPresentValue() throws Exception
    {
        IsString named = new IsString(new NamedStub<>("vasya", Either.right(new Present<>(new JsonPrimitive("hello, vasya")))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(new JsonPrimitive("hello, vasya"), named.result().value().raw());
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsString named = new IsString(new NamedStub<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }
}
