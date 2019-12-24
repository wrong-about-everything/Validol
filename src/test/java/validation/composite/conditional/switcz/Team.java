package validation.composite.conditional.switcz;

import java.util.Map;

final public class Team
{
    private String vasya;
    private Integer fedya;
    private Map<String, Object> tolya;
    private Boolean jenya;

    public Team(String vasya, Integer fedya, Map<String, Object> tolya, Boolean jenya)
    {
        this.vasya = vasya;
        this.fedya = fedya;
        this.tolya = tolya;
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

    public Map<String, Object> tolya()
    {
        return this.tolya;
    }

    public Boolean jenya()
    {
        return this.jenya;
    }
}
