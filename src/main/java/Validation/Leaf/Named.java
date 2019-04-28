package Validation.Leaf;

import Validation.Result.Result;
import Validation.Validatable;

public class Named<T> implements Validatable<T>
{
    private String name;
    private T value;
    private Boolean successful;

    public Named(String name, T value, Boolean successful)
    {
        this.name = name;
        this.value = value;
        this.successful = successful;
    }

    public Result<T> result()
    {
        return new Validation.Result.Named<>(this.name, this.value, this.successful);
    }
}
