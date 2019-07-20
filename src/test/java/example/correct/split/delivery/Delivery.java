package example.correct.split.delivery;

import com.google.gson.JsonElement;
import example.correct.split.delivery.courier.Courier;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.switcz.Specific;
import validation.composite.switcz.SwitchTrue;
import validation.leaf.IndexedValue;
import validation.leaf.Required;
import validation.result.Result;
import java.util.List;

public class Delivery implements Validatable<example.correct.bag.delivery.Delivery>
{
    private Validatable<example.correct.bag.delivery.Delivery> v;

    public Delivery(JsonElement requestJsonObject)
    {
        this.v =
            new FastFail<>(
                new Required(
                    new IndexedValue("delivery", requestJsonObject)
                ),
                deliveryJsonElement ->
                    new SwitchTrue<>(
                        "delivery",
                        List.of(
                            new Specific<>(
                                () -> true,
                                new Courier(deliveryJsonElement)
                            )
                        )
                    )
            );
    }

    @Override
    public Result<example.correct.bag.delivery.Delivery> result() throws Throwable
    {
        return this.v.result();
    }
}
