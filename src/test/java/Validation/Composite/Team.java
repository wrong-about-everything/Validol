package Validation.Composite;

class Team
{
    private String vasya;
    private Integer fedya;
    private Boolean jenya;

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
