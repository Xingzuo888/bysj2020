package com.example.bysj2020.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author : wxz
 * Time   : 2020/03/25
 * Desc   :
 */
@Entity
public class SearchHistoryBean {

    @Id(autoincrement = true)
    private long id;

    private String name;

    public SearchHistoryBean(String name) {
        this.name = name;
    }

    @Generated(hash = 732891995)
    public SearchHistoryBean(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
