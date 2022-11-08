package com.rossotti.lighting.ebay.model.inventory.offer;

import java.util.List;
import lombok.Getter;

@Getter
public class Offers {
    private Integer total;
    private Integer size;
    private String href;
    private Integer limit;
    private final List<Offer> offers = null;
}