package org.example.spring_boot2.data.service;

import org.example.spring_boot2.data.bean.City;
import org.example.spring_boot2.data.mapper.CityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lifei
 */
@Service
public class CityService {
    @Resource
    CityMapper cityMapper;

    public City getCityById(Integer id) {
        return cityMapper.getCity(id);
    }

    public void insertCity(City city) {
        cityMapper.insertCity(city);
    }
}
