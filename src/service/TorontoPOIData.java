package service;

import domain.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class providing sample POI data for Toronto area.
 * Used by MockPlacesService for demo/testing purposes.
 *
 * @author CPS731 Team 20
 */
public class TorontoPOIData {

    // Toronto downtown coordinates (approximate)
    private static final double TORONTO_LAT = 43.6532;
    private static final double TORONTO_LNG = -79.3832;

    /**
     * Get all sample POIs for Toronto area.
     *
     * @return List of 15 sample POIs covering various categories
     */
    public static List<POI> getSamplePOIs() {
        List<POI> pois = new ArrayList<>();

        // ========== RESTAURANTS ==========

        POI pasta = new POI();
        pasta.setId("poi_001");
        pasta.setName("Pasta Palace");
        pasta.setLocation(new LatLng(43.6529, -79.3849));
        pasta.setCategory("restaurant");
        pasta.setRating(4.5f);
        pasta.setPriceLevel("$$");
        pasta.setTags(Arrays.asList("italian", "pasta", "indoor", "dinner"));
        pasta.setOpenNow(true);
        pasta.setAddress("123 Queen St W, Toronto");
        pois.add(pasta);

        POI sushi = new POI();
        sushi.setId("poi_002");
        sushi.setName("Sushi Haven");
        sushi.setLocation(new LatLng(43.6545, -79.3805));
        sushi.setCategory("restaurant");
        sushi.setRating(4.7f);
        sushi.setPriceLevel("$$$");
        sushi.setTags(Arrays.asList("japanese", "sushi", "indoor", "lunch", "dinner"));
        sushi.setOpenNow(true);
        sushi.setAddress("456 King St E, Toronto");
        pois.add(sushi);

        POI burger = new POI();
        burger.setId("poi_003");
        burger.setName("Burger Barn");
        burger.setLocation(new LatLng(43.6510, -79.3890));
        burger.setCategory("restaurant");
        burger.setRating(4.2f);
        burger.setPriceLevel("$");
        burger.setTags(Arrays.asList("american", "burgers", "casual", "family-friendly"));
        burger.setOpenNow(true);
        burger.setAddress("789 Spadina Ave, Toronto");
        pois.add(burger);

        POI bistro = new POI();
        bistro.setId("poi_004");
        bistro.setName("French Bistro");
        bistro.setLocation(new LatLng(43.6570, -79.3780));
        bistro.setCategory("restaurant");
        bistro.setRating(4.6f);
        bistro.setPriceLevel("$$$$");
        bistro.setTags(Arrays.asList("french", "fine-dining", "romantic", "dinner"));
        bistro.setOpenNow(false); // Closed right now
        bistro.setAddress("321 Yonge St, Toronto");
        pois.add(bistro);

        // ========== MUSEUMS ==========

        POI rom = new POI();
        rom.setId("poi_005");
        rom.setName("Royal Ontario Museum");
        rom.setLocation(new LatLng(43.6677, -79.3948));
        rom.setCategory("museum");
        rom.setRating(4.8f);
        rom.setPriceLevel("$$");
        rom.setTags(Arrays.asList("history", "culture", "educational", "indoor", "family-friendly"));
        rom.setOpenNow(true);
        rom.setAddress("100 Queen's Park, Toronto");
        pois.add(rom);

        POI ago = new POI();
        ago.setId("poi_006");
        ago.setName("Art Gallery of Ontario");
        ago.setLocation(new LatLng(43.6536, -79.3925));
        ago.setCategory("museum");
        ago.setRating(4.7f);
        ago.setPriceLevel("$$");
        ago.setTags(Arrays.asList("art", "culture", "educational", "indoor"));
        ago.setOpenNow(true);
        ago.setAddress("317 Dundas St W, Toronto");
        pois.add(ago);

        // ========== PARKS ==========

        POI highPark = new POI();
        highPark.setId("poi_007");
        highPark.setName("High Park");
        highPark.setLocation(new LatLng(43.6465, -79.4637));
        highPark.setCategory("park");
        highPark.setRating(4.9f);
        highPark.setPriceLevel("$");
        highPark.setTags(Arrays.asList("outdoor", "nature", "hiking", "family-friendly", "scenic"));
        highPark.setOpenNow(true);
        highPark.setAddress("1873 Bloor St W, Toronto");
        pois.add(highPark);

        POI trinity = new POI();
        trinity.setId("poi_008");
        trinity.setName("Trinity Bellwoods Park");
        trinity.setLocation(new LatLng(43.6476, -79.4190));
        trinity.setCategory("park");
        trinity.setRating(4.6f);
        trinity.setPriceLevel("$");
        trinity.setTags(Arrays.asList("outdoor", "picnic", "casual", "dog-friendly"));
        trinity.setOpenNow(true);
        trinity.setAddress("790 Queen St W, Toronto");
        pois.add(trinity);

        // ========== CAFES ==========

        POI coffeeCulture = new POI();
        coffeeCulture.setId("poi_009");
        coffeeCulture.setName("Coffee Culture");
        coffeeCulture.setLocation(new LatLng(43.6520, -79.3810));
        coffeeCulture.setCategory("cafe");
        coffeeCulture.setRating(4.3f);
        coffeeCulture.setPriceLevel("$");
        coffeeCulture.setTags(Arrays.asList("coffee", "breakfast", "wifi", "casual"));
        coffeeCulture.setOpenNow(true);
        coffeeCulture.setAddress("234 Adelaide St E, Toronto");
        pois.add(coffeeCulture);

        POI beanScene = new POI();
        beanScene.setId("poi_010");
        beanScene.setName("Bean Scene");
        beanScene.setLocation(new LatLng(43.6555, -79.3870));
        beanScene.setCategory("cafe");
        beanScene.setRating(4.4f);
        beanScene.setPriceLevel("$");
        beanScene.setTags(Arrays.asList("coffee", "pastries", "wifi", "cozy"));
        beanScene.setOpenNow(true);
        beanScene.setAddress("567 College St, Toronto");
        pois.add(beanScene);

        // ========== SHOPPING ==========

        POI eatonCentre = new POI();
        eatonCentre.setId("poi_011");
        eatonCentre.setName("CF Toronto Eaton Centre");
        eatonCentre.setLocation(new LatLng(43.6544, -79.3807));
        eatonCentre.setCategory("shopping");
        eatonCentre.setRating(4.5f);
        eatonCentre.setPriceLevel("$$");
        eatonCentre.setTags(Arrays.asList("shopping", "indoor", "mall", "family-friendly"));
        eatonCentre.setOpenNow(true);
        eatonCentre.setAddress("220 Yonge St, Toronto");
        pois.add(eatonCentre);

        // ========== ENTERTAINMENT ==========

        POI cnTower = new POI();
        cnTower.setId("poi_012");
        cnTower.setName("CN Tower");
        cnTower.setLocation(new LatLng(43.6426, -79.3871));
        cnTower.setCategory("entertainment");
        cnTower.setRating(4.8f);
        cnTower.setPriceLevel("$$$");
        cnTower.setTags(Arrays.asList("landmark", "scenic", "tourist", "family-friendly"));
        cnTower.setOpenNow(true);
        cnTower.setAddress("290 Bremner Blvd, Toronto");
        pois.add(cnTower);

        POI aquarium = new POI();
        aquarium.setId("poi_013");
        aquarium.setName("Ripley's Aquarium");
        aquarium.setLocation(new LatLng(43.6424, -79.3860));
        aquarium.setCategory("entertainment");
        aquarium.setRating(4.7f);
        aquarium.setPriceLevel("$$");
        aquarium.setTags(Arrays.asList("aquarium", "educational", "indoor", "family-friendly"));
        aquarium.setOpenNow(true);
        aquarium.setAddress("288 Bremner Blvd, Toronto");
        pois.add(aquarium);

        // ========== BARS ==========

        POI rooftopBar = new POI();
        rooftopBar.setId("poi_014");
        rooftopBar.setName("Sky Lounge");
        rooftopBar.setLocation(new LatLng(43.6490, -79.3820));
        rooftopBar.setCategory("bar");
        rooftopBar.setRating(4.4f);
        rooftopBar.setPriceLevel("$$$");
        rooftopBar.setTags(Arrays.asList("bar", "rooftop", "nightlife", "scenic"));
        rooftopBar.setOpenNow(false); // Closed during day
        rooftopBar.setAddress("88 King St W, Toronto");
        pois.add(rooftopBar);

        POI brewPub = new POI();
        brewPub.setId("poi_015");
        brewPub.setName("Steam Whistle Brewing");
        brewPub.setLocation(new LatLng(43.6395, -79.3860));
        brewPub.setCategory("bar");
        brewPub.setRating(4.5f);
        brewPub.setPriceLevel("$$");
        brewPub.setTags(Arrays.asList("brewery", "beer", "tours", "casual"));
        brewPub.setOpenNow(true);
        brewPub.setAddress("255 Bremner Blvd, Toronto");
        pois.add(brewPub);

        return pois;
    }

    /**
     * Get Toronto downtown coordinates for geocoding.
     *
     * @return LatLng for Toronto downtown
     */
    public static LatLng getTorontoDowntown() {
        return new LatLng(TORONTO_LAT, TORONTO_LNG);
    }
}
