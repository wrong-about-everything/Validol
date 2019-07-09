package validation.result;

import validation.value.Value;

public interface Result<T>
{
    public Boolean isSuccessful();

    public Value<T> value() throws Exception;

    /**
     * It could be String, like in Required class.
     * In case of NamedBlocOfNameds class, it can return List<String>, List<List<String>>, or mixed.
     * Hence, return type is just Object, which is just enough:
     * everything that is supposed to do in case of error is turn it to json and output.
     */
    public Object error() throws Exception;

    public Boolean isNamed();

    public String name() throws Exception;
}
