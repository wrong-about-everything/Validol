package Validation.Result;

public interface Result<T>
{
    public Boolean isSuccessful();

    public T value() throws Exception;

    /**
     * @// TODO: 6/18/19 Fix signature. Method should return Object.
     * It could be String, like in Required class.
     * In case of Bloc class, it can return List<String>, List<List<String>>, or mixed.
     * Hence, return type is just Object, which is just enough:
     * everything is supposed to do in case of error is turn it to json and output.
     */
    public Object error() throws Exception;

    public Boolean isNamed();

    public String name() throws Exception;
}
