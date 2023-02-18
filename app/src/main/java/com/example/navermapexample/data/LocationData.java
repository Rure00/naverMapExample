package com.example.navermapexample.data;

public class LocationData {

    private final String name;
    private final String roadAddress;
    private final String jibunAddress;
    private final String category;
    private final String description;
    private final String mapX;
    private final String mapY;

    public LocationData(String name, String roadAddress, String jibunAddress, String category, String description, String mapX, String mapY) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.category = category;
        this.description = description;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public String getName() {
        return name;
    }
    public String getRoadAddress() {
        return roadAddress;
    }
    public String getJibunAddress() {
        return jibunAddress;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public String getMapX() {
        return mapX;
    }
    public String getMapY() { return mapY; }
}
