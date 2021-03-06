package example.correct.split.items;

import com.google.gson.JsonElement;
import example.correct.bag.items.item.Item;
import validation.ErrorStub;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.bloc.of.unnameds.NamedBlocOfUnnameds;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.required.Required;
import validation.leaf.as.type.AsInteger;
import validation.result.Result;
import validation.result.error.Error;

import java.util.List;
import java.util.Map;

final public class Items implements Validatable<example.correct.bag.items.Items>
{
    private Validatable<example.correct.bag.items.Items> v;

    public Items(JsonElement requestJsonObject) throws Exception
    {
        this.v =
            new FastFail<>(
                new Required(
                    new IndexedValue("items", requestJsonObject)
                )
                ,
                itemsJsonElement ->
                    new NamedBlocOfUnnameds<>(
                        "items",
                        itemsJsonElement,
                        item ->
                            new UnnamedBlocOfNameds<>(
                                List.of(
                                    new AsInteger(
                                        new Required(
                                            new IndexedValue("id", item),
                                            () -> Map.of("code", "vasya", "message", "Item id is really required!")
                                        )
                                    )
                                ),
                                Item.class
                            ),
                        example.correct.bag.items.Items.class
                    )
            );
    }

    @Override
    public Result<example.correct.bag.items.Items> result() throws Exception
    {
        return this.v.result();
    }
}
