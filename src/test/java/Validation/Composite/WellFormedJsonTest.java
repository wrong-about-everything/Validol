package Validation.Composite;

import Validation.Leaf.Named;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WellFormedJsonTest
{
    @Test
    public void invalidJson() throws Exception
    {
        assertFalse(
            (new WellFormedJson(
                new Named<>("vasya", Either.right("invalid json"))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void wellFormedJson() throws Exception
    {
        assertTrue(
            (new WellFormedJson(
                new Named<>("vasya", Either.right(this.json()))
            ))
                .result()
                    .isSuccessful()
        );
    }

    private String json()
    {
        List<String> target = new LinkedList<>();
        target.add("vasya");
        target.add("fedya");

        Gson gson = new Gson();
        return gson.toJson(target, new TypeToken<List<String>>() {}.getType());
    }
}
