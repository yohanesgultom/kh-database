package id.kawalharga.database;

import id.kawalharga.model.CommodityInput;
import id.kawalharga.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yohanesgultom on 03/06/16.
 */
public class ServiceTest {

    Service instance;

    @Before
    public void setup() throws Exception {
        instance = Service.getInstance("/home/yohanesgultom/PantauHarga-config.properties");
    }

    @Test
    public void testService() {
        try {
            Map<String, Integer> regionMap = instance.getRegionMap();
            System.out.println(regionMap);
            assert(true);
        } catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }

    }

    @Test
    public void testGetUser() {
        try {
            User user = instance.getUser("admin");
            Assert.assertEquals("admin", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetLatestCommodityInputs() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            String dateInString = "12-12-2015";
            Date date = sdf.parse(dateInString);
            int limit = 3;
            List<CommodityInput> list = instance.getLatestCommodityInputs(date, limit);
            assert list.size() == limit;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }
}
