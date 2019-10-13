package validation.composite.bloc.of.unnamed.bag;

import java.util.List;

public class Items
{
    private List<Item> list;

    public Items(List<Item> list)
    {
        this.list = list;
    }

    public List<Item> list()
    {
        return this.list;
    }
}


