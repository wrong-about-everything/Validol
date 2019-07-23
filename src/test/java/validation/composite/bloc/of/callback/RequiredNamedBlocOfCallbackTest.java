package validation.composite.bloc.of.callback;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.Validatable;
import validation.composite.VFunction;
import validation.composite.bloc.of.named.Team;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.result.Named;
import validation.result.Result;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RequiredNamedBlocOfCallbackTest
{
    @Test
    public void success() throws Throwable
    {
        Result<Team> result =
            new RequiredNamedBlocOfCallback<>(
                "team",
                this.json(),
                new VFunction<JsonElement, Validatable<T>>() {
                    @Override
                    public Validatable<T> apply(JsonElement argument) throws Throwable {
                        return null;
                    }
                }
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("belov", result.value().raw().vasya());
        assertEquals(Integer.valueOf(7), result.value().raw().fedya());
        assertEquals(Map.of("id", 245), result.value().raw().tolya());
        assertEquals(false, result.value().raw().jenya());
    }

    @Test
    public void someFieldsFailed() throws Throwable
    {
        Result<Team> result =
            new NamedBlocOfNameds<>(
                "team",
                List.of(
                    () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                    () -> new Named<>("fedya", Either.left("Ooops")),
                    () -> new Named<>("tolya", Either.left("Woooooooops")),
                    () -> new Named<>("jenya", Either.right(new Present<>(false)))
                ),
                Team.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            Map.of(
                "fedya", "Ooops",
                "tolya", "Woooooooops"
            ),
            result.error()
        );
    }
}
