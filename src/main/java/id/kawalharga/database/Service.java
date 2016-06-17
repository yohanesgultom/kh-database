package id.kawalharga.database;

import id.kawalharga.model.CommodityInput;
import id.kawalharga.model.Geolocation;
import id.kawalharga.model.User;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * Created by yohanesgultom on 03/06/16.
 */
public class Service {

    final static Logger logger = Logger.getLogger(Service.class);

    private Connection connection;
    private Properties configProperties;
    private static Service instance;

    public static Service getInstance(String configFile) throws Exception {
        if (instance == null) {
            instance = new Service(configFile);
        }
        return instance;
    }

    private Service(String propFileName) throws Exception {
        this.loadConfigProperties(propFileName);
    }

    private void loadConfigProperties(String propFileName) throws Exception {
        if (this.configProperties == null) {
            this.configProperties = new Properties();
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(propFileName);
                this.configProperties.load(inputStream);
            } catch (FileNotFoundException e) {
                logger.error("Cannot find database properties file in src/test/resources/config.properties");
                throw e;
            } catch (Exception e) {
                throw e;
            } finally {
                if (inputStream != null)
                    inputStream.close();
            }
        }
    }

    public Connection connectToDatabase() throws Exception {
        String driver = this.configProperties.getProperty("dataSource.driverClassName");
        String url = this.configProperties.getProperty("dataSource.url");
        String user = this.configProperties.getProperty("dataSource.username");
        String pass = this.configProperties.getProperty("dataSource.password");
        Class.forName(driver);
        logger.debug("Opening database connection");
        this.connection = DriverManager.getConnection(url, user, pass);
        return this.connection;
    }

    public Integer getUserId(String username) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        Integer userId = null;
        this.connectToDatabase();
        try {
            stmt = this.connection.createStatement();
            String sql = String.format("select id from auth_user where username = '%s'", username);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                userId = rs.getInt("id");
                //System.out.println(String.format("Got user id for %s: %d", username, userId));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        this.closeDatabaseConnection();
        return userId;
    }

    public User getUser(String username) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        User user = null;
        this.connectToDatabase();
        try {
            stmt = this.connection.createStatement();
            String sql = String.format("select id, username, nama, alamat, nohp, kodepos, email from auth_user where username = '%s'", username);
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                user = new User(rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("nohp"),
                        rs.getString("kodepos"),
                        rs.getString("email"));
                logger.info("user retrieved: " + user.toString());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        this.closeDatabaseConnection();
        return user;
    }

    HashMap<String, Geolocation> getGeolocationByName(List<CommodityInput> inputList) throws Exception {
        HashMap<String, Geolocation> locationMap = new HashMap<String, Geolocation>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        this.connectToDatabase();
        try {
            pstmt = this.connection.prepareStatement("select id, name, geolocation from region where upper(name) = ?");
            for (CommodityInput input : inputList) {
                String upcased = input.getLocation().toUpperCase();
                Geolocation location = locationMap.get(upcased);
                if (location == null) {
                    pstmt.setString(1, upcased);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String[] locationArr = rs.getString("geolocation").split(",");
                        double lat = Double.parseDouble(locationArr[0]);
                        double lng = Double.parseDouble(locationArr[1]);
                        locationMap.put(rs.getString("name").toUpperCase(), new Geolocation(lat, lng, rs.getInt("id")));
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.closeDatabaseConnection();
        return locationMap;
    }

    private HashMap<String, Integer> getComodityMap(List<CommodityInput> inputList) throws Exception {
        HashMap<String, Integer> commodityMap = new HashMap<String, Integer>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        this.connectToDatabase();
        try {
            pstmt = this.connection.prepareStatement("select id, name from comodity where upper(name) = ?");
            for (CommodityInput input : inputList) {
                String upcased = input.getName().toUpperCase();
                Integer id = commodityMap.get(upcased);
                if (id == null) {
                    pstmt.setString(1, upcased);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        commodityMap.put(rs.getString("name").toUpperCase(), rs.getInt("id"));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.closeDatabaseConnection();
        return commodityMap;
    }

    public void batchInsertCommodityInput(List<CommodityInput> inputList) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        java.util.Date now = new java.util.Date();
        this.connectToDatabase();
        try {
            pstmt = this.connection.prepareStatement("insert into comodity_input values("
                    + "(select nextval('hibernate_sequence')),"
                    + "0,"
                    + "0,"
                    + "?," // comodity id
                    + "?," // date created
                    + "0,"
                    + "?," // last updated
                    + "?," // lat
                    + "?," // lng
                    + "?," // price
                    + "?," // region id
                    + "?," // user id
                    + "null,"
                    + "null,"
                    + "?" // description
                    + ")");
            for (CommodityInput input : inputList) {
                Long commodityId = input.getId();
                Geolocation geo = input.getGeo();
                if (commodityId != null && geo != null) {
                    pstmt.setLong(1, commodityId);
                    pstmt.setDate(2, new java.sql.Date(now.getTime()));
                    pstmt.setDate(3, new java.sql.Date(now.getTime()));
                    pstmt.setDouble(4, geo.getLat());
                    pstmt.setDouble(5, geo.getLng());
                    pstmt.setInt(6, new Double(input.getPrice()).intValue());
                    pstmt.setLong(7, geo.getId());
                    pstmt.setLong(8, input.getUser().getId());
                    pstmt.setString(9, input.getDescription());
                    try {
                        logger.debug(pstmt.toString());
                        pstmt.execute();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        throw ex;
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.closeDatabaseConnection();
    }

    private HashMap<String, Long> getMapValue(String tableName) throws Exception {
        HashMap<String, Long> regionMap = new HashMap<String, Long>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        this.connectToDatabase();
        try {
            pstmt = this.connection.prepareStatement("select id, upper(name) as name from " + tableName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                regionMap.put(rs.getString("name").toUpperCase(), rs.getLong("id"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.closeDatabaseConnection();
        return regionMap;
    }

    public HashMap<String, Long> getRegionMap() throws Exception {
        return this.getMapValue("region");
    }

    public HashMap<String, Long> getCommodityMap() throws Exception {
        return this.getMapValue("comodity");
    }

    public List<CommodityInput> getLatestCommodityInputs(java.util.Date date, int limit) throws Exception {
        List<CommodityInput> commodityInputList = new ArrayList<CommodityInput>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        this.connectToDatabase();
        try {
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(date);
            nextDate.add(Calendar.DATE, 1);
            pstmt = this.connection.prepareStatement("select " +
                    "i.id, " +
                    "c.name, " +
                    "r.id as location_id, " +
                    "r.name as location, " +
                    "i.price, " +
                    "i.amount, " +
                    "i.lat, " +
                    "i.lng, " +
                    "i.description, " +
                    "i.date_created, " +
                    "u.id as user_id," +
                    "u.nama as user_name," +
                    "u.username as user_username," +
                    "u.alamat as user_address," +
                    "u.nohp as user_phone," +
                    "u.kodepos as user_postal_code," +
                    "u.email as user_email " +
                    "from comodity_input i join auth_user u on i.user_id = u.id " +
                    "join comodity c on i.comodity_name_id = c.id " +
                    "join region r on i.region_id = r.id " +
                    "where i.date_created >= ? and i.date_created < ? limit ?");
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            pstmt.setDate(2, new java.sql.Date(nextDate.getTime().getTime()));
            pstmt.setInt(3, limit);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getLong("user_id"),
                        rs.getString("user_username"),
                        rs.getString("user_name"),
                        rs.getString("user_address"),
                        rs.getString("user_phone"),
                        rs.getString("user_postal_code"),
                        rs.getString("user_email"));
                CommodityInput commodityInput = new CommodityInput(
                        rs.getLong("id"),
                        user,
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getDouble("lat"),
                        rs.getDouble("lng"),
                        rs.getLong("location_id"),
                        rs.getString("description"),
                        rs.getDate("date_created")
                );
                commodityInputList.add(commodityInput);
                logger.debug("retrieved: " + commodityInput);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.closeDatabaseConnection();
        return commodityInputList;
    }

    public void closeDatabaseConnection() throws Exception {
        if (this.connection != null) {
            logger.debug("Closing db connection");
            this.connection.close();
        }
    }

}
