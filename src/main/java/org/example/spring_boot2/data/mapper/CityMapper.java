package org.example.spring_boot2.data.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.spring_boot2.data.bean.City;

/**
 * @author lifei
 */
public interface CityMapper {
    /**
     * getCity
     *
     * @param id id
     * @return City
     */
    @Select("select id, name, state, country from city_tbl where id=#{id}")
    City getCity(Integer id);

    /**
     * insertCity
     *
     * @param city city
     */
    @Insert("insert into city_tbl(`name`, `state`, `country`) values (#{name}, #{state}, #{country});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertCity(City city);
}
