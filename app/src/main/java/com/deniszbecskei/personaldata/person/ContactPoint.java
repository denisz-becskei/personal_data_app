package com.deniszbecskei.personaldata.person;

public class ContactPoint {

    private String system;
    private String value;
    private String use;
    private PositiveInt rank;
    private Period period;

    public ContactPoint() { }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public PositiveInt getRank() {
        return rank;
    }

    public void setRank(PositiveInt rank) {
        this.rank = rank;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
