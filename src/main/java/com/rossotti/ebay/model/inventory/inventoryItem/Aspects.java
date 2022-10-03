package com.rossotti.ebay.model.inventory.inventoryItem;

import java.util.List;
import lombok.Getter;

@Getter
public class Aspects {
    private List<String> brand = null;
    private List<String> type = null;
    private List<String> recordingDefinition = null;
    private List<String> opticalZoom = null;
    private List<String> mediaFormat = null;
    private List<String> storageType = null;
}