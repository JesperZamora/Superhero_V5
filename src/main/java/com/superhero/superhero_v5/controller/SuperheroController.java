package com.superhero.superhero_v5.controller;

import com.superhero.superhero_v5.DTO.SuperheroFormDTO;
import com.superhero.superhero_v5.DTO.SuperheroPowersDTO;
import com.superhero.superhero_v5.repository.ISuperheroRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/superhero")
@Controller
public class SuperheroController {
    ISuperheroRepository iSuperheroRepository;

    public SuperheroController(ApplicationContext context, @Value("${superhero.repository.impl}") String impl){
        iSuperheroRepository = (ISuperheroRepository) context.getBean(impl);
    }


    @GetMapping()
    public String getHeroInformation(Model model) {
        List<SuperheroFormDTO> heroList = iSuperheroRepository.getHeroInfo();
        model.addAttribute("heroList", heroList);
        return "index";
    }

    @GetMapping("/superpower/{name}")
    public String getHeroPowers(@PathVariable String name, Model model){
        SuperheroPowersDTO superheroPowers = iSuperheroRepository.getHeroPower(name);
        if(superheroPowers != null){
            model.addAttribute("heroId", superheroPowers.getHeroId());
            model.addAttribute("name", superheroPowers.getHeroName());
            model.addAttribute("powers", superheroPowers.getHeroPower());
        }
        return "powers";
    }

    @GetMapping("/superpower/delete/{heroId}")
    public String deleteSuperpower(@PathVariable int heroId) {
        iSuperheroRepository.deletePower(heroId);
        return "redirect:/superhero";
    }

    @GetMapping("/create")
    public String createSuperhero(Model model){
        SuperheroFormDTO superheroForm = new SuperheroFormDTO();
        model.addAttribute("superheroForm", superheroForm);

        List<String> cityList = iSuperheroRepository.getCities();
        model.addAttribute("cityList", cityList);

        List<String> powerList = iSuperheroRepository.getPowers();
        model.addAttribute("powerList", powerList);

        return "addSuperhero";
    }

    @PostMapping("/add")
    public String createSuperhero(@ModelAttribute SuperheroFormDTO superhero){
        iSuperheroRepository.addSuperHero(superhero);
        return "redirect:/superhero";
    }


    @GetMapping("/update/{id}")
    public String updateSuperhero(@PathVariable int id, Model model){
        List<SuperheroFormDTO> superheroList = iSuperheroRepository.getHeroInfo();
        for(SuperheroFormDTO superheroFound : superheroList){
            if(superheroFound.getId() == id){
                model.addAttribute("superheroFound", superheroFound);
            }
        }

        List<String> cityList = iSuperheroRepository.getCities();
        model.addAttribute("cityList", cityList);

        return "updateSuperhero";
    }

    @PostMapping("/update")
    public String updateSuperhero(@ModelAttribute SuperheroFormDTO superhero){
        iSuperheroRepository.editHero(superhero);
        return "redirect:/superhero";
    }

    @GetMapping("/delete/{heroId}")
    public String deleteSuperhero(@PathVariable int heroId){
        iSuperheroRepository.delete(heroId);
        return "redirect:/superhero";
    }

}
