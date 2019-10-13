package validation.composite.bloc.of.callback;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import example.correct.bag.guest.Guest;
import org.junit.Test;
import validation.Validatable;
import validation.composite.VFunction;
import validation.composite.bloc.of.named.Team;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Present;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RequiredNamedBlocOfCallbackTest
{
    @Test
    public void success() throws Throwable
    {
        Result<Integer> result =
            new RequiredNamedBlocOfCallback<>(
                "guest",
                this.json(),
                (element) -> () -> new Named<>("guest", Either.right(new Present<>(777)))
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(Integer.valueOf(777), result.value().raw());
    }

    @Test
    public void someFieldsFailed() throws Throwable
    {
        Result<Object> result =
            new RequiredNamedBlocOfCallback<>(
                "guest",
                this.json(),
                (element) -> () -> new Named<>("guest", Either.left("An error occured"))
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("An error occured", result.error());
    }

    private JsonElement json()
    {
        return
            new Gson().toJsonTree(
                Map.of(
                    "guest", Map.of(
                        "email", "samokhinvadim@gmail.com",
                        "name", "Vadim Samokhin"
                    )
                ),
                new TypeToken<HashMap<String, Object>>() {}.getType()
            );
    }
}
