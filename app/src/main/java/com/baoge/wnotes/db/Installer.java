package com.baoge.wnotes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "installer")
public class Installer {
    @Id(autoincrement = true)
    private Long id;

    private String city;
    private String name;
    @Generated(hash = 2072995938)
    public Installer(Long id, String city, String name) {
        this.id = id;
        this.city = city;
        this.name = name;
    }
    @Generated(hash = 182293252)
    public Installer() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
     

}
