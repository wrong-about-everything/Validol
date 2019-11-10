package example.correct.split.items;

import com.google.gson.JsonElement;
import example.correct.bag.items.item.Item;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.bloc.of.unnameds.NamedBlocOfUnnameds;
import validation.leaf.IndexedValue;
import validation.leaf.Required;
import validation.leaf.as.type.AsInteger;
import validation.result.Result;

import java.util.List;

public class Items implements Validatable<example.correct.bag.items.Items>
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
                                            new IndexedValue("id", item)
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
