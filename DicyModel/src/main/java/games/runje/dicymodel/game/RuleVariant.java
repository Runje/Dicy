package games.runje.dicymodel.game;

/**
 * Created by Thomas on 01.09.2015.
 */
public enum RuleVariant
{
    Baden_Baden("Baden Baden"), Basel("Basel"), Las_Vegas("Las Vegas"), Macao("Macao"), Atlantic_City("Atlantic City"), Monte_Carlo("Monte Carlo");

    private String text;

    RuleVariant(String text)
    {
        this.text = text;
    }

    public static RuleVariant getEnum(String value)
    {
        for (RuleVariant v : values())
            if (v.getText().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }

    @Override
    public String toString()
    {
        return text;
    }

    public String getText()
    {
        return text;
    }
}
