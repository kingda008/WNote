package com.baoge.wnotes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "departmeng")
public class Department {
    @Id(autoincrement = true)
    private Long id;

    private String city;
    private String hospital;
    private String name;
    @Generated(hash = 1129734147)
    public Department(Long id, String city, String hospital, String name) {
        this.id = id;
        this.city = city;
        this.hospital = hospital;
        this.name = name;
    }
    @Generated(hash = 355406289)
    public Department() {
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
    public String getHospital() {
        return this.hospital;
    }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


}
