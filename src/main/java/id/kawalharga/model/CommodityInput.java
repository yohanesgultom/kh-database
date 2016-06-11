package id.kawalharga.model;

import java.util.Date;

/**
 * Created by yohanesgultom on 03/06/16.
 */
public class CommodityInput {
    private Long id;
    private String name;
    private String location;
    private double price;
    private Geolocation geo;
    private User user;
    private String description;
    private Date createdAt;

    public CommodityInput(String name, String location, double price) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.geo = null;
        this.description = null;
        this.id = null;
        this.createdAt = new Date();
    }

    public CommodityInput(Long id, User user, String name, String location, double price, double lat, double lng, Long regionId, String desc, Date createdAt) {
        this(name, location, price);
        this.id = id;
        this.geo = new Geolocation(lat, lng, regionId);
        this.description = desc;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Geolocation getGeo() {
        return geo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("%s dijual seharga Rp %.2f/kg di %s (%f, %f) dilaporkan oleh %s pada %s", this.name, this.price, this.location, this.geo.lat, this.geo.lng, this.user.getName(), this.createdAt);
    }
}
