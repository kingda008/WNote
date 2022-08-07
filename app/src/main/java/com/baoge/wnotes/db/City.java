package com.baoge.wnotes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "city")
public class City {
    @Id(autoincrement = true)
    private Long id;

    private String name;

    @Generated(hash = 1518855127)
    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 750791287)
    public City() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
    
}
