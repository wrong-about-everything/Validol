package validation.Integration.NonCoincidingValidatableAndDataClass.ValidatableAndDataClassHaveDifferentStructure;

public class SampleRequestData
{
    private String key1;
    private Boolean key2;

    public SampleRequestData(String key1, Boolean key2)
    {
        this.key1 = key1;
        this.key2 = key2;
    }

    public String key1()
    {
        return this.key1;
    }

    public Boolean key2()
    {
        return this.key2;
    }
}
