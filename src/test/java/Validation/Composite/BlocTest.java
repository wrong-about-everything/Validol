package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import com.spencerwi.either.Either;
import org.junit.Test;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static org.junit.Assert.*;

public class BlocTest {
    @Test
    public void success() throws Exception {
        Result<Team> result =
            new Bloc<Team>(
                List.of(
                    () -> new Named<>("vasya", Either.right("belov")),
                    () -> new Named<>("fedya", Either.right(7)),
                    () -> new Named<>("jenya", Either.right(false))
//                    () ->
//                        new Bloc<>(
//                            List.of(
//                                () -> new Named<>("vasya", Either.right("belov")),
//                                () -> new Named<>("fedya", Either.right(7)),
//                                () -> new Named<>("jenya", Either.right(false))
//                            )
//                        )
//                            .result()
                ),
                Team.class
            )
                .result();

        assertTrue(result.isSuccessful());
//        assertEquals("vasiliev", result.value());
    }

    private class Team
    {
        private String vasya;
        private Integer fedya;
        private Boolean jenya;

        public Team()
        {
            this.vasya = "vasya";
            this.fedya = 666;
            this.jenya = false;
        }

        public Team(String vasya, Integer fedya, Boolean jenya)
        {
            this.vasya = vasya;
            this.fedya = fedya;
            this.jenya = jenya;
        }

        public String vasya()
        {
            return this.vasya;
        }

        public Integer fedya()
        {
            return this.fedya;
        }

        public Boolean jenya()
        {
            return this.jenya;
        }
    }

    @Test
    public void test1()
    {
        new A().vasya();
    }

    private class A
    {
        private void vasya()
        {
//            final Gson gson = new Gson();
//            final Collection<Integer> integersToJson = new ArrayList<Integer>();
//            integersToJson.add(100);
//            integersToJson.add(90);
//            integersToJson.add(85);
//
//            //Serialization
//            System.out.print("marks:" + gson.toJson(integersToJson));

            //De-serialization
            Class listType = (Class)((ParameterizedType) new ArrayList<SpiderMan>() {}.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

            System.out.println(listType);
//
//            Type listType2 = new TypeToken<Collection<Integer>>(){}.getType();
//            System.out.println(listType2);
//            final Collection<Integer> integersFromJson = gson.fromJson(gson.toJson(integersToJson), listType);
//            System.out.println("marks:" +integersFromJson);
        }
    }

    @Test
    public void test2()
    {
        new Voodoo().chill();
    }

    private final class Voodoo {
        void chill() {
            // Here I'd like to get the Class-Object 'SpiderMan'
            System.out.println(new ArrayList<SpiderMan>() {}.getClass().getGenericSuperclass());
//            System.out.println(
//                ((ParameterizedType) aListWithSomeType
//                    .getClass()
//                        .getGenericSuperclass()
//                )
//                    .getActualTypeArguments()[0]
//            );
        }
    }

    private class SpiderMan {
    }
}
