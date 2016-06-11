package id.kawalharga.model;

/**
 * Created by yohanesgultom on 11/06/16.
 */
public class User {
    private Long id;
    private String username;
    private String name;
    private String address;
    private String phone;
    private String postalCode;
    private String email;

    public User(Long id, String username, String name, String address, String phone, String postalCode, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.postalCode = postalCode;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("{ id:%d, username:%s, name:%s, email:%s }", this.id, this.username, this.name, this.email);
    }
}
