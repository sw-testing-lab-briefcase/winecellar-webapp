package com.cellar.wine.bootstrap;

import com.cellar.wine.models.Producer;
import com.cellar.wine.models.Wine;
import com.cellar.wine.services.ProducerService;
import com.cellar.wine.services.WineService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final WineService wineService;
    private final ProducerService producerService;

    public DataLoader(WineService wineService, ProducerService producerService) {
        this.wineService = wineService;
        this.producerService = producerService;
    }

    @Override
    public void run(String... args) throws Exception {

        Producer producer1 = new Producer();
        producer1.setName("Mark West");
        producer1.setCountry("USA");
        producer1.setAppellation("Napa Valley");
        producerService.save(producer1);

        Producer producer2 = new Producer();
        producer2.setName("Big Boi");
        producer2.setCountry("France");
        producer2.setAppellation("Burgundy");
        producerService.save(producer2);

        Producer producer3 = new Producer();
        producer3.setName("Producer 3");
        producerService.save(producer3);

        Producer producer4 = new Producer();
        producer4.setName("Producer 4");
        producerService.save(producer4);

        System.out.println("Loaded producers...");

        Wine wine1 = new Wine();
        wine1.setAppellation("Willamette Valley");
        wine1.setCountry("USA");
        wine1.setName("Brickhouse");
        wine1.setVarietal("Pinot");
        wine1.setVintage("2015");
        //may need this in the wine controller to set the producer to that wine
        wine1.setProducer(producer1);
        wine1.setProducer(producer3);
        wineService.save(wine1);

        Wine wine2 = new Wine();
        wine2.setAppellation("Walla Walla");
        wine2.setCountry("USA");
        wine2.setName("Holla Holla");
        wine2.setVarietal("Malbec");
        wine2.setVintage("2016");
        wine2.setProducer(producer2);
        wine2.setProducer(producer4);
        wineService.save(wine2);

        //may need this in the producer controller to get its wines
        producer1.getWines().add(wine1);
        producer2.getWines().add(wine2);
        producer3.getWines().add(wine1);
        producer4.getWines().add(wine2);

        System.out.println("Loaded wines...");
    }
}
