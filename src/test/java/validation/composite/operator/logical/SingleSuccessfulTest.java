package validation.composite.operator.logical;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;
import validation.ErrorStub;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.operator.logical.xor.SingleSuccessful;
import validation.leaf.as.type.AsBoolean;
import validation.leaf.as.type.AsInteger;
import validation.leaf.as.type.AsString;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.required.Required;
import validation.result.Result;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

final public class SingleSuccessfulTest
{
    @Test
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

        Result<?> result =
            new UnnamedBlocOfNameds<SomeTestStructure>(
                List.of(
                    new SingleSuccessful(
                        "global",
                        new ErrorStub("Only one of the fields must be present"),
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
        assertEquals(
            Map.of(
                "global", Map.of(
                    "message", "Only one of the fields must be present",
                    "code", 123
                )
            ),
            result.error().value()
        );
    }

    @Test
    public void testTwoFieldsAreSuccessful() throws Exception
    {
        JsonElement json =
            new Gson().toJsonTree(
                Map.of(
                    "first_field", "vasya",
                    "third_field", 777,
                    "unrelated", "Rinse"
                )
            );

        Result<SomeTestStructure> result =
            new UnnamedBlocOfNameds<SomeTestStructure>(
                List.of(
                    new SingleSuccessful(
                        "global",
                        new ErrorStub("Only one of the fields must be present"),
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
        assertEquals(
            Map.of(
                "global", Map.of(
                    "message", "Only one of the fields must be present",
                    "code", 123
                )
            ),
            result.error().value()
        );
    }

    @Test
    public void testOnlyOneFieldIsSuccessful() throws Exception
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
                    new SingleSuccessful(
                        "global",
                        new ErrorStub("Only one of the fields must be present"),
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
            new SomeTestStructure(777, "Rinse").thirdField(),
            result.value().raw().thirdField()
        );
    }

    @Test
    public void testNoneOfTheFieldsAreSuccessful() throws Exception
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
                    new SingleSuccessful(
                        "global",
                        new ErrorStub("Only one of the fields must be present"),
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
        assertEquals(
            Map.of(
                "global", Map.of(
                    "message", "Only one of the fields must be present",
                    "code", 123
                )
            ),
            result.error().value()
        );
    }
}
