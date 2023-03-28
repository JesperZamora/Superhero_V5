package com.superhero.superhero_v5.DTO;

import java.util.List;

public class SuperheroPowersDTO {
    private int heroId;
    private String heroName;
    private List<String> heroPowerList;

    public SuperheroPowersDTO(int heroId, String heroName, List<String> heroPowerList) {
        this.heroId = heroId;
        this.heroName = heroName;
        this.heroPowerList = heroPowerList;
    }

    public String getHeroName() {
        return heroName;
    }

    public int getHeroId() {
        return heroId;
    }

    public List<String> getHeroPower() {
        return heroPowerList;
    }

    public void addHeroPower(String superpower) {
        heroPowerList.add(superpower);
    }
}
