package com.cellar.wine.models;

import javax.persistence.*;

@Entity
@Table(name = "wine")
public class Wine extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "producer_id", nullable = false)
    private Producer producer;

    @Column(name = "name")
    private String name;

    @Column(name = "vintage")
    private String vintage;

    @Column(name = "varietal")
    private String varietal;

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public String getVarietal() {
        return varietal;
    }

    public void setVarietal(String varietal) {
        this.varietal = varietal;
    }

    @Override
    public String toString() {
        return name;
    }
}
