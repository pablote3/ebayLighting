package com.rossotti.ebay.model.inventory.inventoryItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class Aspects {
    @JsonProperty("Brand")
    private List<String> brand = null;
    @JsonProperty("Type")
    private List<String> type = null;
    @JsonProperty("Recording Definition")
    private List<String> recordingDefinition = null;
    @JsonProperty("Optical Zoom")
    private List<String> opticalZoom = null;
    @JsonProperty("Media Format")
    private List<String> mediaFormat = null;
    @JsonProperty("Storage Type")
    private List<String> storageType = null;
}