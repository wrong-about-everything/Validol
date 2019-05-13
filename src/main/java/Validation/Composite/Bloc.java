package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.spencerwi.either.Either;
import java.util.List;

public class Bloc<T> implements Validatable<T>
{
    private List<Validatable<?>> validatables;
    private Class<? extends T> type;

    public Bloc(List<Validatable<?>> validatables, Class<? extends T> type)
    {
        this.validatables = validatables;
        this.type = type;
    }

    public Result<T> result() throws Exception
    {
        // @todo: validate all validatables!
        // @todo: create non-strictly-type version of Bloc, returning Map<String, Object>
        return
            new Named<>(
                "vasya",
                Either.right(
                    // https://stackoverflow.com/questions/299998/instantiating-object-of-type-parameter
                    this.type.getConstructor().newInstance()
//                    List.of(new Object())
//                    this.validatables.stream().map(
//                        (current) -> {
//                            try {
//                                return current.result();
//                            } catch (Exception e) {
//                                return new Named<String>("vasya", Either.left(""));
//                            }
//                        }
//                    )
                )
            );
    }
}
