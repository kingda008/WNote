package com.baoge.wnotes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity(nameInDb = "hospital")
public class Hospital {
    @Id(autoincrement = true)
    private Long id;

    private String city;
    private String name;
    @Generated(hash = 764167096)
    public Hospital(Long id, String city, String name) {
        this.id = id;
        this.city = city;
        this.name = name;
    }
    @Generated(hash = 1301721268)
    public Hospital() {
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
