/*
 * My-Wine-Cellar, copyright 2019
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 */

package info.mywinecellar.security.model;

import info.mywinecellar.model.Bottle;
import info.mywinecellar.model.GenericTastingNotes;
import info.mywinecellar.model.Review;
import info.mywinecellar.model.Tasted;
import info.mywinecellar.model.Wishlist;
import info.mywinecellar.util.BottleSorter;
import info.mywinecellar.util.GenericTastingNotesSorter;
import info.mywinecellar.util.ReviewSorter;
import info.mywinecellar.util.TastedSorter;
import info.mywinecellar.util.WishlistSorter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name = "username")
    @NotEmpty(message = "*Please provide your name")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Collection<Authority> authorities;

    @OneToMany(mappedBy = "user")
    private List<Bottle> bottles;

    @OneToMany(mappedBy = "user")
    private List<GenericTastingNotes> genericTastingNotes;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<Tasted> tasted;

    @OneToMany(mappedBy = "user")
    private List<Wishlist> wishlist;

    public List<Bottle> getBottles() {
        Collections.sort(bottles, new BottleSorter());
        return bottles;
    }

    public List<GenericTastingNotes> getGenericTastingNotes() {
        Collections.sort(genericTastingNotes, new GenericTastingNotesSorter());
        return genericTastingNotes;
    }

    public List<Review> getReviews() {
        Collections.sort(reviews, new ReviewSorter());
        return reviews;
    }

    public List<Tasted> getTasted() {
        Collections.sort(tasted, new TastedSorter());
        return tasted;
    }

    public List<Wishlist> getWishlist() {
        Collections.sort(wishlist, new WishlistSorter());
        return wishlist;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}