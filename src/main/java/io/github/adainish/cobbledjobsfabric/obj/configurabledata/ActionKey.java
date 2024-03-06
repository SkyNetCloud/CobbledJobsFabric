package io.github.adainish.cobbledjobsfabric.obj.configurabledata;

import java.util.Arrays;
import java.util.List;

public class ActionKey
{
    public String actionKey = "";
    public String prettyString = "DEFAULT";
    public List<String> description = Arrays.asList("&7Base Worth %amount%$", "&7Base Exp: %exp%");
    public int baseWorth = 0;
    public int baseExperience = 0;

    public ActionKey()
    {

    }
}
