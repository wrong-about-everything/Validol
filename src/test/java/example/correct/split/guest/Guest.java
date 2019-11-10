package example.correct.split.guest;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.leaf.IndexedValue;
import validation.leaf.Required;
import validation.leaf.as.type.AsString;
import validation.leaf.is.of.structure.IsJsonObject;
import validation.result.Result;

import java.util.List;

public class Guest implements Validatable<example.correct.bag.guest.Guest>
{
    private Validatable<example.correct.bag.guest.Guest> v;

    public Guest(JsonElement requestJsonObject) throws Exception
    {
        this.v =
            new FastFail<>(
                new IsJsonObject(
                    new Required(
                        new IndexedValue("guest", requestJsonObject)
                    )
                ),
                guestJsonObject ->
                    new NamedBlocOfNameds<>(
                        "guest",
                        List.of(
                            new AsString(
                                new Required(
                                    new IndexedValue("email", guestJsonObject)
                                )
                            ),
                            new AsString(
                                new Required(
                                    new IndexedValue("name", guestJsonObject)
                                )
                            )
                        ),
                        example.correct.bag.guest.Guest.class
                    )
            );
    }

    @Override
    public Result<example.correct.bag.guest.Guest> result() throws Exception
    {
        return this.v.result();
    }
}
