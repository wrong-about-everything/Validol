package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.spencerwi.either.Either;

import java.util.List;
import java.util.Map;

public class IndexedValue<T> implements Validatable<T>
{
    private String name;
    private Map<String, T> data;

    public IndexedValue(String name, Map<String, T> data)
    {
        this.name = name;
        this.data = data;
    }

    public Result<T> result()
    {
        if (!this.data.containsKey(this.name)) {
            return new Named<>(this.name, Either.left(String.format("Key %s does not exist", this.name)));
        }

        return new Named<>(this.name, Either.right(this.data.get(this.name)));
    }
}
