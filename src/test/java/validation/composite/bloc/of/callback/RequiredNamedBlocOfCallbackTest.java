package validation.composite.bloc.of.callback;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.result.Named;
import validation.result.Result;
import validation.result.value.Present;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequiredNamedBlocOfCallbackTest
{
    @Test
    public void success() throws Exception
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
    public void someFieldsFailed() throws Exception
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
