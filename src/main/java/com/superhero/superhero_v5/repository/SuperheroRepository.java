package com.superhero.superhero_v5.repository;

import com.superhero.superhero_v5.DTO.HeroRealCreationYearDTO;
import com.superhero.superhero_v5.DTO.SuperheroFormDTO;
import com.superhero.superhero_v5.DTO.SuperheroPowersDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("superhero_db")
public class SuperheroRepository implements ISuperheroRepository{
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user;
    @Value("${spring.datasource.password}")
    String pwd;


    public List<SuperheroFormDTO> getHeroInfo(){
        List<SuperheroFormDTO> heroList = new ArrayList<>();

        try(Connection con = DriverManager.getConnection(url,user,pwd)) {
            String SQL = "SELECT * FROM superhero";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while(rs.next()) {
                heroList.add(new SuperheroFormDTO(
                        rs.getInt("superhero_id"),
                        rs.getString("hero_name"),
                        rs.getString("real_name"),
                        rs.getString("creation_year"),
                        rs.getString("city_id")
                ));
            }
            return heroList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SuperheroPowersDTO getHeroPower(String name){
        SuperheroPowersDTO superheroPower = null;

        try(Connection con = DriverManager.getConnection(url,user,pwd)) {
            String SQL = "SELECT superhero.superhero_id, hero_name, superpower FROM superhero JOIN superpower JOIN superheropower ON superpower.superpower_id = superheropower.superpower_id AND superhero.superhero_id = superheropower.superhero_id AND hero_name = ? ";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1,name);
            ResultSet rs = pstmt.executeQuery();

            String currentName = "";

            while(rs.next()) {
                int heroId = rs.getInt("superhero_id");
                String heroName = rs.getString("hero_name");
                String superpower = rs.getString("superpower");

                if(currentName.equals(heroName)) {
                    superheroPower.addHeroPower(superpower);

                } else  {
                    superheroPower = new SuperheroPowersDTO(heroId, heroName, new ArrayList<>(List.of(superpower)));
                    currentName = heroName;
                }
            }
            return superheroPower;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCities(){
        List<String> cities = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(url,user,pwd)) {
            String SQL = "SELECT * FROM city";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while(rs.next()){
                String city = rs.getString("city");
                cities.add(city);
            }
            return cities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPowers(){
        List<String> powers = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(url,user,pwd)) {
            String SQL = "SELECT * FROM superpower";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while(rs.next()){
                String power = rs.getString("superpower");
                powers.add(power);
            }
            return powers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSuperHero(SuperheroFormDTO form) {
        try (Connection con = DriverManager.getConnection(url, user, pwd)) {
            // ID's
            int cityId = 0;
            int heroId = 0;
            List<Integer> powerIDs = new ArrayList<>();

            // find city_id
            String SQL1 = "select city_id from city where city = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL1);
            pstmt.setString(1, form.getCity());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cityId = rs.getInt("city_id");
            }

            // insert row in superhero table
            String SQL2 = "insert into superhero (hero_name, real_name, creation_year, city_id) values(?, ?, ?, ?);";
            pstmt = con.prepareStatement(SQL2, Statement.RETURN_GENERATED_KEYS); // return autoincremented key
            pstmt.setString(1, form.getHeroName());
            pstmt.setString(2, form.getRealName());
            pstmt.setString(3, form.getCreationYear());
            pstmt.setInt(4, cityId);
            int rows = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                heroId = rs.getInt(1);
            }


            // find power_ids
            String SQL3 = "select superpower_id from superpower where superpower = ?;";
            pstmt = con.prepareStatement(SQL3);

            for (String power : form.getPowerList()) {
                pstmt.setString(1, power);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    powerIDs.add(rs.getInt("superpower_id"));
                }
            }

            // insert entries in superhero_powers join table
            String SQL4 = "insert into superheropower values (?,?);";
            pstmt = con.prepareStatement(SQL4);

            for (int i = 0; i < powerIDs.size(); i++) {
                pstmt.setInt(1, heroId);
                pstmt.setInt(2, powerIDs.get(i));
                rows = pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editHero(SuperheroFormDTO form){
        try(Connection con = DriverManager.getConnection(url,user,pwd)){
            // update hero_name, real_name, creation_year, city_id (see SQL2)
            String SQL1 = "UPDATE superhero SET hero_name=?, real_name=?, creation_year=?, city_id=? WHERE superhero_id = ?;";
            PreparedStatement pstmt1 = con.prepareStatement(SQL1);
            pstmt1.setString(1, form.getHeroName());
            pstmt1.setString(2, form.getRealName());
            pstmt1.setString(3, form.getCreationYear());
            pstmt1.setInt(5, form.getId());

            // Get city as String from SuperheroFormDTO instance and get the city_id from database
            String SQL2 = "SELECT city_id FROM city WHERE city = ?";
            PreparedStatement pstmt2 = con.prepareStatement(SQL2);
            pstmt2.setString(1, form.getCity());
            ResultSet rs = pstmt2.executeQuery();
            if(rs.next()){
                pstmt1.setInt(4,rs.getInt("city_id"));
            }
            pstmt1.executeUpdate();

            // Make an update function

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int heroId){
        try(Connection con = DriverManager.getConnection(url,user,pwd)) {
            String SQL = "DELETE FROM superheropower WHERE superhero_id = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1,heroId);
            pstmt.executeUpdate();

            String SQL2 = "DELETE FROM superhero WHERE superhero_id = ?";
            PreparedStatement pstmt2 = con.prepareStatement(SQL2);
            pstmt2.setInt(1,heroId);
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePower(int heroId) {
        try(Connection con = DriverManager.getConnection(url,user,pwd)) {
            String SQL = "DELETE FROM superheropower WHERE superpower_id = ? AND superhero_id = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1,1002);
            pstmt.setInt(2,heroId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
