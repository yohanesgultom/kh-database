package id.kawalharga.model;

/**
 * Created by yohanesgultom on 03/06/16.
 */
public class Geolocation {
    public long id;
    public double lat;
    public double lng;

    public Geolocation(double lat, double lng, long id) {
        this.lat = lat;
        this.lng = lng;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.lat, this.lng);
    }
}
