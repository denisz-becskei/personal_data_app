package com.deniszbecskei.personaldata;

import com.deniszbecskei.personaldata.person.Person;

import java.util.HashMap;
import java.util.Map;

public class SearchItem {
    private Person person;

    public SearchItem() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Map<String, Object> toCRUD() {
        Map<String, Object> map = new HashMap<>();
        map.put("person", this.person);
        return map;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "person=" + person +
                '}';
    }
}
