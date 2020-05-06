package validation.composite.operator.logical.atleastoneissuccessful;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Ignore;
import org.junit.Test;
import validation.ErrorStub;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.operator.logical.or.AtLeastOneIsSuccessful;
import validation.leaf.as.type.AsBoolean;
import validation.leaf.as.type.AsInteger;
import validation.leaf.as.type.AsString;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.required.Required;
import validation.result.Result;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

final public class AtLeastOneIsSuccessfulTest
{
    @Test
    @Ignore
    public void testAllFieldsAreSuccessful() throws Exception
    {

        JsonElement json =
            new Gson().toJsonTree(
                Map.of(
                    "first_field", "vasya",
                    "second_field", false,
                    "third_field", 777,
                    "unrelated", "Rinse"
                )
            );

        Result<SomeTestStructure> result =
            new UnnamedBlocOfNameds<SomeTestStructure>(
                List.of(
                    new AtLeastOneIsSuccessful(
                        "global",
                        new ErrorStub("At least one of the fields must be present"),
                        new AsString(
                            new Required(
                                new IndexedValue("first_field", json)
                            )
                        ),
                        new AsBoolean(
                            new Required(
                                new IndexedValue("second_field", json)
                            )
                        ),
                        new AsInteger(
                            new Required(
                                new IndexedValue("third_field", json)
                            )
                        )
                    ),
                    new AsString(
                        new IndexedValue("unrelated", json)
                    )
                ),
                SomeTestStructure.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(
            Map.of(
                "first_field", "vasya",
                "second_field", false,
                "third_field", 777,
                "unrelated", "Rinse"
            ),
            result.value().raw().map()
        );
    }

    @Test
    @Ignore
    public void testTwoFieldsAreSuccessful() throws Exception
    {
        JsonElement json =
            new Gson().toJsonTree(
                Map.of(
                    "second_field", false,
                    "third_field", 777,
                    "unrelated", "Rinse"
                )
            );

        Result<SomeTestStructure> result =
            new UnnamedBlocOfNameds<SomeTestStructure>(
                List.of(
                    new AtLeastOneIsSuccessful(
                        "global",
                        new ErrorStub("At least one of the fields must be present"),
                        new AsString(
                            new Required(
                                new IndexedValue("first_field", json)
                            )
                        ),
                        new AsBoolean(
                            new Required(
                                new IndexedValue("second_field", json)
                            )
                        ),
                        new AsInteger(
                            new Required(
                                new IndexedValue("third_field", json)
                            )
                        )
                    ),
                    new AsString(
                        new IndexedValue("unrelated", json)
                    )
                ),
                SomeTestStructure.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(
            Map.of(
                "second_field", false,
                "third_field", 777,
                "unrelated", "Rinse"
            ),
            result.value().raw().map()
        );
    }

    @Test
    @Ignore
    public void testOneFieldIsSuccessful() throws Exception
    {
        JsonElement json =
            new Gson().toJsonTree(
                Map.of(
                    "third_field", 777,
                    "unrelated", "Rinse"
                )
            );

        Result<SomeTestStructure> result =
            new UnnamedBlocOfNameds<SomeTestStructure>(
                List.of(
                    new AtLeastOneIsSuccessful(
                        "global",
                        new ErrorStub("At least one of the fields must be present"),
                        new AsString(
                            new Required(
                                new IndexedValue("first_field", json)
                            )
                        ),
                        new AsBoolean(
                            new Required(
                                new IndexedValue("second_field", json)
                            )
                        ),
                        new AsInteger(
                            new Required(
                                new IndexedValue("third_field", json)
                            )
                        )
                    ),
                    new AsString(
                        new IndexedValue("unrelated", json)
                    )
                ),
                SomeTestStructure.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(
            Map.of(
                "third_field", 777,
                "unrelated", "Rinse"
            ),
            result.value().raw().map()
        );
    }

    @Test
    @Ignore
    public void testNoneOfTheFieldsAreSucessful() throws Exception
    {
        JsonElement json =
            new Gson().toJsonTree(
                Map.of(
                    "unrelated", "Rinse"
                )
            );

        Result<SomeTestStructure> result =
            new UnnamedBlocOfNameds<SomeTestStructure>(
                List.of(
                    new AtLeastOneIsSuccessful(
                        "global",
                        new ErrorStub("At least one of the fields must be present"),
                        new AsString(
                            new Required(
                                new IndexedValue("first_field", json)
                            )
                        ),
                        new AsBoolean(
                            new Required(
                                new IndexedValue("second_field", json)
                            )
                        ),
                        new AsInteger(
                            new Required(
                                new IndexedValue("third_field", json)
                            )
                        )
                    ),
                    new AsString(
                        new IndexedValue("unrelated", json)
                    )
                ),
                SomeTestStructure.class
            )
                .result();

        assertFalse(result.isSuccessful());
    }
}
