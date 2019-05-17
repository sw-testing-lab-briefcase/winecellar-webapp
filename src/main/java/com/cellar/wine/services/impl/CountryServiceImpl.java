package com.cellar.wine.services.impl;

import com.cellar.wine.models.Country;
import com.cellar.wine.repositories.CountryRepository;
import com.cellar.wine.services.CountryService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Set<Country> findAll() {
        Set<Country> countries = new HashSet<>();
        countryRepository.findAll().forEach(countries::add);
        return countries;
    }

    @Override
    public Country findById(Long aLong) {
        return countryRepository.findById(aLong).orElse(null);
    }

    @Override
    public Country save(Country object) {
        return countryRepository.save(object);
    }

    @Override
    public void delete(Country object) {
        countryRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        countryRepository.deleteById(aLong);
    }
}