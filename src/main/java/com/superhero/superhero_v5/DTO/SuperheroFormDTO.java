package com.superhero.superhero_v5.DTO;


import java.util.List;

public class SuperheroFormDTO {
    private int id;
    private String heroName;
    private String realName;
    private String creationYear;
    private String city;
    private List<String> powerList;

    public SuperheroFormDTO(int id, String heroName, String realName, String creationYear, String city) {
        this.id = id;
        this.heroName = heroName;
        this.realName = realName;
        this.creationYear = creationYear;
        this.city = city;
    }

    public SuperheroFormDTO(){}

    public List<String> getPowerList() {
        return powerList;
    }

    public void setPowerList(List<String> powerList) {
        this.powerList = powerList;
    }

    public void add(String power) {
        powerList.add(power);
    }


    public int getId() {
        return id;
    }

    public String getHeroName() {
        return heroName;
    }

    public String getRealName() {
        return realName;
    }

    public String getCreationYear() {
        return creationYear;
    }

    public String getCity() {
        return city;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setCreationYear(String creationYear) {
        this.creationYear = creationYear;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
