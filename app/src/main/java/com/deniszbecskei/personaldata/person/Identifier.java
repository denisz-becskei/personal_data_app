package com.deniszbecskei.personaldata.person;

public class Identifier {
    private String use;
    private String type;
    private String system;
    private String value;
    private Period period;

    public Identifier() { }

    public void setUse(String use) {
        this.use = use;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getValue() {
        return value;
    }
}
