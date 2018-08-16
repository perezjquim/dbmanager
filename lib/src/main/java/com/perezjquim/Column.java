package com.perezjquim;

public class Column
{
    private String name;
    private boolean isPrimary;
    private boolean notNull;
    private String type;

    public Column(String name, String type, boolean notNull, boolean isPrimary)
    {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.isPrimary = isPrimary;
    }

    @Override
    public String toString()
    {
        return "`" + name + "`" + " " + type + " " + ((notNull) ? "NOT NULL" : "") + " " + ((isPrimary) ? "PRIMARY KEY" : "");
    }
}
