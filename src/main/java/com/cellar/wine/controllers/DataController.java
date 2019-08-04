package com.cellar.wine.controllers;

import com.cellar.wine.models.Area;
import com.cellar.wine.models.BarrelComponent;
import com.cellar.wine.models.Bottle;
import com.cellar.wine.models.Country;
import com.cellar.wine.models.GenericTastingNotes;
import com.cellar.wine.models.Grape;
import com.cellar.wine.models.GrapeComponent;
import com.cellar.wine.models.Producer;
import com.cellar.wine.models.Region;
import com.cellar.wine.models.Review;
import com.cellar.wine.models.Wine;
import com.cellar.wine.models.Wishlist;
import com.cellar.wine.security.User;
import com.cellar.wine.security.UserService;
import com.cellar.wine.services.BottleService;
import com.cellar.wine.services.CountryService;
import com.cellar.wine.services.RegionService;
import com.cellar.wine.services.ReviewService;
import com.cellar.wine.services.TastingNotesService;
import com.cellar.wine.services.WishlistService;
import com.cellar.wine.ui.AbstractKeyUI;
import com.cellar.wine.ui.AgingUI;
import com.cellar.wine.ui.AreaUI;
import com.cellar.wine.ui.BarrelUI;
import com.cellar.wine.ui.BarrelUISorter;
import com.cellar.wine.ui.CountryUI;
import com.cellar.wine.ui.GrapeUI;
import com.cellar.wine.ui.GrapeUISorter;
import com.cellar.wine.ui.ProducerUI;
import com.cellar.wine.ui.RegionUI;
import com.cellar.wine.ui.WineUI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/d")
public class DataController {

    private static final String MODEL_ATTRIBUTE_COUNTRIES = "countries";
    private static final String MODEL_ATTRIBUTE_COUNTRY = "country";
    private static final String MODEL_ATTRIBUTE_REGIONS = "regions";
    private static final String MODEL_ATTRIBUTE_REGION = "region";
    private static final String MODEL_ATTRIBUTE_AREAS = "areas";
    private static final String MODEL_ATTRIBUTE_AREA = "area";
    private static final String MODEL_ATTRIBUTE_PRODUCERS = "producers";
    private static final String MODEL_ATTRIBUTE_PRIMARY_GRAPES = "primaryGrapes";
    private static final String MODEL_ATTRIBUTE_PRODUCER = "producer";
    private static final String MODEL_ATTRIBUTE_WINES = "wines";
    private static final String MODEL_ATTRIBUTE_WINE = "wine";
    private static final String MODEL_ATTRIBUTE_WINEGRAPES = "winegrapes";
    private static final String MODEL_ATTRIBUTE_BOTTLE = "mybottle";
    private static final String MODEL_ATTRIBUTE_REVIEW = "myreview";
    private static final String MODEL_ATTRIBUTE_TASTINGNOTES = "mytastingnotes";
    private static final String MODEL_ATTRIBUTE_WISHLIST = "mywishlist";
    
    private CountryService countryService;
    private RegionService regionService;
    private UserService userService;
    private BottleService bottleService;
    private ReviewService reviewService;
    private TastingNotesService tastingNotesService; 
    private WishlistService wishlistService;

    public DataController(CountryService countryService, RegionService regionService,
                          UserService userService, BottleService bottleService,
                          ReviewService reviewService, TastingNotesService tastingNotesService,
                          WishlistService wishlistService) {
        this.countryService = countryService;
        this.regionService = regionService;
        this.userService = userService;
        this.bottleService = bottleService;
        this.reviewService = reviewService;
        this.tastingNotesService = tastingNotesService;
        this.wishlistService = wishlistService;
    }

    @GetMapping("/")
    public String dataRootGet(Model model) {
        Set<Country> countries = countryService.findWithRegions();
        model.addAttribute(MODEL_ATTRIBUTE_COUNTRIES, getCountryUIs(countries));
        return "country/countryList";
    }

    @GetMapping("/{country}")
    public String dataCountryGet(@PathVariable String country, Model model) {
        Country c = null;

        if (guard(country)) {
            return "redirect:/";
        }

        c = countryService.findByLowerCaseName(AbstractKeyUI.fromKey(country));
        if (c == null) {
            return "redirect:/";
        }
        if (c.getRegions() == null || c.getRegions().size() == 0) {
            return "redirect:/";
        }
        
        model.addAttribute(MODEL_ATTRIBUTE_COUNTRY, getCountryUI(c));
        model.addAttribute(MODEL_ATTRIBUTE_REGIONS, getRegionUIs(c.getRegions()));
        return "country/countryDetails";
    }

    @GetMapping("/{country}/{region}")
    public String dataRegionGet(@PathVariable String country, @PathVariable String region, Model model) {
        Country c = null;
        Region r = null;

        if (guard(country) || guard(region)) {
            return "redirect:/";
        }

        c = countryService.findByLowerCaseName(AbstractKeyUI.fromKey(country));
        if (c == null) {
            return "redirect:/";
        }
        
        r = regionService.findByLowerCaseName(AbstractKeyUI.fromKey(region), c.getId());
        if (r == null) {
            return "redirect:/";
        }

        model.addAttribute(MODEL_ATTRIBUTE_COUNTRY, getCountryUI(c));
        model.addAttribute(MODEL_ATTRIBUTE_REGION, getRegionUI(r));
        model.addAttribute(MODEL_ATTRIBUTE_AREAS, getAreaUIs(r.getAreas()));
        return "region/regionDetails";
    }

    @GetMapping("/{country}/{region}/{area}")
    public String dataAreaGet(@PathVariable String country, @PathVariable String region,
                              @PathVariable String area, Model model) {
        Country c = null;
        Region r = null;
        Area a = null;

        if (guard(country) || guard(region) || guard(area)) {
            return "redirect:/";
        }

        c = countryService.findByLowerCaseName(AbstractKeyUI.fromKey(country));
        if (c == null) {
            return "redirect:/";
        }
        
        r = regionService.findByLowerCaseName(AbstractKeyUI.fromKey(region), c.getId());
        if (r == null) {
            return "redirect:/";
        }

        for (Area ar : r.getAreas()) {
            if (AbstractKeyUI.toKey(ar.getName()).equals(area)) {
                a = ar;
                break;
            }
        }
        if (a == null) {
            return "redirect:/";
        }
        
        model.addAttribute(MODEL_ATTRIBUTE_COUNTRY, getCountryUI(c));
        model.addAttribute(MODEL_ATTRIBUTE_REGION, getRegionUI(r));
        model.addAttribute(MODEL_ATTRIBUTE_AREA, getAreaUI(a));
        model.addAttribute(MODEL_ATTRIBUTE_PRODUCERS, getProducerUIs(a.getProducers()));
        model.addAttribute(MODEL_ATTRIBUTE_PRIMARY_GRAPES, getGrapeUIs(a.getPrimaryGrapes()));
        return "area/areaDetails";
    }

    @GetMapping("/{country}/{region}/{area}/{producer}")
    public String dataProducerGet(@PathVariable String country, @PathVariable String region,
                                  @PathVariable String area, @PathVariable String producer, Model model) {
        Country c = null;
        Region r = null;
        Area a = null;
        Producer p = null;

        if (guard(country) || guard(region) || guard(area) || guard(producer)) {
            return "redirect:/";
        }

        c = countryService.findByLowerCaseName(AbstractKeyUI.fromKey(country));
        if (c == null) {
            return "redirect:/";
        }
        
        r = regionService.findByLowerCaseName(AbstractKeyUI.fromKey(region), c.getId());
        if (r == null) {
            return "redirect:/";
        }

        for (Area ar : r.getAreas()) {
            if (AbstractKeyUI.toKey(ar.getName()).equals(area)) {
                a = ar;
                break;
            }
        }
        if (a == null) {
            return "redirect:/";
        }

        for (Producer pr : a.getProducers()) {
            if (AbstractKeyUI.toKey(pr.getName()).equals(producer)) {
                p = pr;
                break;
            }
        }
        if (p == null) {
            return "redirect:/";
        }
        
        model.addAttribute(MODEL_ATTRIBUTE_COUNTRY, getCountryUI(c));
        model.addAttribute(MODEL_ATTRIBUTE_REGION, getRegionUI(r));
        model.addAttribute(MODEL_ATTRIBUTE_AREA, getAreaUI(a));
        model.addAttribute(MODEL_ATTRIBUTE_PRODUCER, getProducerUI(p));
        model.addAttribute(MODEL_ATTRIBUTE_WINES, getWineUIs(p.getWines()));
        return "producer/producerDetails";
    }

    @GetMapping("/{country}/{region}/{area}/{producer}/{wine}/{vintage}/{size}")
    public String dataWineGet(@PathVariable String country, @PathVariable String region,
                              @PathVariable String area, @PathVariable String producer,
                              @PathVariable String wine, @PathVariable Integer vintage, @PathVariable Float size,
                              Model model, Principal principal) {
        Country c = null;
        Region r = null;
        Area a = null;
        Producer p = null;
        Wine w = null;
        User user = null;
        Bottle bottle = null;
        Review review = null;
        GenericTastingNotes tastingnotes = null;
        Wishlist wishlist = null;

        if (guard(country) || guard(region) || guard(area) || guard(producer) ||
            guard(wine) || guard(vintage) || guard(size)) {
            return "redirect:/";
        }

        c = countryService.findByLowerCaseName(AbstractKeyUI.fromKey(country));
        if (c == null) {
            return "redirect:/";
        }
        
        r = regionService.findByLowerCaseName(AbstractKeyUI.fromKey(region), c.getId());
        if (r == null) {
            return "redirect:/";
        }

        for (Area ar : r.getAreas()) {
            if (AbstractKeyUI.toKey(ar.getName()).equals(area)) {
                a = ar;
                break;
            }
        }
        if (a == null) {
            return "redirect:/";
        }

        for (Producer pr : a.getProducers()) {
            if (AbstractKeyUI.toKey(pr.getName()).equals(producer)) {
                p = pr;
                break;
            }
        }
        if (p == null) {
            return "redirect:/";
        }

        for (Wine wi : p.getWines()) {
            if (AbstractKeyUI.toKey(wi.getName()).equals(wine) &&
                wi.getVintage().equals(vintage) && wi.getSize().equals(size)) {
                w = wi;
                break;
            }
        }
        if (w == null) {
            return "redirect:/";
        }
        
        if (principal != null) {
            user = userService.findByUsername(principal.getName());
            bottle = bottleService.findByWine(user.getId(), w.getId());
            review = reviewService.findByWine(user.getId(), w.getId());
            tastingnotes = tastingNotesService.findByWine(user.getId(), w.getId());
            wishlist = wishlistService.findByWine(user.getId(), w.getId());
        }

        List<GrapeUI> winegrapes = new ArrayList<>();
        for (GrapeComponent gc : w.getGrapes()) {
            List<BarrelUI> barrels = new ArrayList<>();

            if (gc.getBarrelComponents() != null) {
                for (BarrelComponent bc : gc.getBarrelComponents()) {
                    barrels.add(new BarrelUI(bc.getPercentage(),
                                             bc.getBarrel().getName(), bc.getBarrel().getId(),
                                             bc.getSize(), new AgingUI(bc.getAging())
                                             ));
                }
            }

            Collections.sort(barrels, new BarrelUISorter());

            winegrapes.add(new GrapeUI(gc.getPercentage(),
                                       gc.getGrape().getName(), gc.getGrape().getId(),
                                       gc.getHarvestBegin(), gc.getHarvestEnd(),
                                       gc.getMaceration() != null ? gc.getMaceration().getDays() : null,
                                       gc.getMaceration() != null ? gc.getMaceration().getTemperature() : null,
                                       gc.getFermentation() != null ? gc.getFermentation().getDays() : null,
                                       gc.getFermentation() != null ? gc.getFermentation().getTemperature() : null,
                                       barrels
                                       ));
        }
        Collections.sort(winegrapes, new GrapeUISorter());

        model.addAttribute(MODEL_ATTRIBUTE_COUNTRY, getCountryUI(c));
        model.addAttribute(MODEL_ATTRIBUTE_REGION, getRegionUI(r));
        model.addAttribute(MODEL_ATTRIBUTE_AREA, getAreaUI(a));
        model.addAttribute(MODEL_ATTRIBUTE_PRODUCER, getProducerUI(p));
        model.addAttribute(MODEL_ATTRIBUTE_WINE, w);
        model.addAttribute(MODEL_ATTRIBUTE_WINEGRAPES, winegrapes);
        model.addAttribute(MODEL_ATTRIBUTE_BOTTLE, bottle);
        model.addAttribute(MODEL_ATTRIBUTE_REVIEW, review);
        model.addAttribute(MODEL_ATTRIBUTE_TASTINGNOTES, tastingnotes);
        model.addAttribute(MODEL_ATTRIBUTE_WISHLIST, wishlist);
        return "wine/wineDetails";
    }
    
    private boolean guard(Object o) {
        if (o == null)
            return true;

        if (o instanceof String) {
            String s = (String)o;
            return "".equals(s);
        }

        return false;
    }

    private List<CountryUI> getCountryUIs(Set<Country> countries) {
        List<CountryUI> result = new ArrayList<>();
        for (Country c : countries) {
            result.add(getCountryUI(c));
        }
        return result;
    }

    private CountryUI getCountryUI(Country c) {
        return new CountryUI(c);
    }

    private List<RegionUI> getRegionUIs(List<Region> regions) {
        List<RegionUI> result = new ArrayList<>();
        for (Region r : regions) {
            result.add(getRegionUI(r));
        }
        return result;
    }

    private RegionUI getRegionUI(Region r) {
        return new RegionUI(r);
    }

    private List<AreaUI> getAreaUIs(List<Area> areas) {
        List<AreaUI> result = new ArrayList<>();
        for (Area a : areas) {
            result.add(getAreaUI(a));
        }
        return result;
    }

    private AreaUI getAreaUI(Area a) {
        return new AreaUI(a);
    }

    private List<ProducerUI> getProducerUIs(List<Producer> producers) {
        List<ProducerUI> result = new ArrayList<>();
        for (Producer p : producers) {
            result.add(getProducerUI(p));
        }
        return result;
    }

    private ProducerUI getProducerUI(Producer p) {
        return new ProducerUI(p);
    }

    private List<GrapeUI> getGrapeUIs(List<Grape> grapes) {
        List<GrapeUI> result = new ArrayList<>();
        if (grapes != null) {
            for (Grape g : grapes) {
                result.add(getGrapeUI(g));
            }
            Collections.sort(result, new GrapeUISorter());
        }
        return result;
    }

    private GrapeUI getGrapeUI(Grape g) {
        return new GrapeUI(g);
    }

    private List<WineUI> getWineUIs(List<Wine> wines) {
        List<WineUI> result = new ArrayList<>();
        for (Wine w : wines) {
            result.add(getWineUI(w));
        }
        return result;
    }

    private WineUI getWineUI(Wine w) {
        return new WineUI(w);
    }
}