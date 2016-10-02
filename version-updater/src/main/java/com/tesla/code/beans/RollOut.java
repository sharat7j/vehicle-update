package com.tesla.code.beans;

import jdk.nashorn.internal.runtime.logging.Logger;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class RollOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 300)
    private String description;


    public RollOut(String json) {
        JSONObject jsonObject = new JSONObject(json);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
