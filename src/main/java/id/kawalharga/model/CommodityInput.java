package id.kawalharga.model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

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
        Locale locale = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm", locale);
        Currency idr = Currency.getInstance("IDR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setCurrency(idr);
        return String.format("%s dijual seharga %s/kg di (%f, %f) dilaporkan oleh %s pada %s", this.name, nf.format(this.price), this.geo.lat, this.geo.lng, this.user.getName(), sdf.format(this.createdAt));
    }
}
