package com.tesla.code.beans;

import com.tesla.code.beans.request.RollOutRequest;

import javax.persistence.*;

/**
 * Bean defining an update RollOut
 */
@Entity
public class RollOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 300)
    private String description;
    private Long date_created;


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

    public Long getDate_created() {
        return date_created;
    }

    public void setDate_created(Long date_created) {
        this.date_created = date_created;
    }

    public static RollOut getRollOutFromRequest(RollOutRequest request) {
        if(request == null) {
            return null;
        }
        RollOut rollOut = new RollOut();
        rollOut.setName(request.getName());
        rollOut.setDescription(request.getDescription());
        return rollOut;
    }
}

