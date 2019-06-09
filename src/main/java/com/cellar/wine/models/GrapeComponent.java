package com.cellar.wine.models;

import lombok.*;

import java.util.List;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grape_component")
public class GrapeComponent extends BaseEntity implements Comparable<GrapeComponent> {

    @Builder
    public GrapeComponent(Long id, Byte percentage, Grape grape, Wine wine) {
        super(id);
        this.percentage = percentage;
        this.grape = grape;
        this.wine = wine;
    }

    @Column(name = "percentage")
    private Byte percentage;

    @ManyToOne
    @JoinColumn(name = "grape_id", referencedColumnName = "id")
    private Grape grape;

    @ManyToOne
    @JoinColumn(name = "wine_id", referencedColumnName = "id")
    private Wine wine;

    @ManyToOne
    @JoinColumn(name = "maceration_id", referencedColumnName = "id")
    private Maceration maceration;

    @ManyToOne
    @JoinColumn(name = "fermentation_id", referencedColumnName = "id")
    private Fermentation fermentation;

    @OneToMany(mappedBy = "barrel")
    private List<BarrelComponent> barrelComponents;

    @Override
    public int compareTo(GrapeComponent gc) {
        int result = percentage.compareTo(gc.getPercentage());

        if (result == 0)
            return grape.getName().compareTo(gc.getGrape().getName());

        return result;
    }
}
