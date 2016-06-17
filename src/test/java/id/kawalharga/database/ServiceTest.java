package id.kawalharga.database;

import id.kawalharga.model.CommodityInput;
import id.kawalharga.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
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
        instance = Service.getInstance("src/test/resources/config.properties");
    }

    @Test
    public void testService() {
        try {
            Map<String, Long> regionMap = instance.getRegionMap();
            Map<String, Long> commodityMap = instance.getCommodityMap();
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

    @Test
    public void testComodityToString() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm");
            Date date = sdf.parse("11-06-2016 12:00");
            User user = new User(0l, "user", "User", "rumah", "911", "12345", "user@email.com");
            CommodityInput commodityInput = new CommodityInput(0l, user, "bawang merah", "pasar", 19999, -6.239879, 106.862344, 0l, null, date);
            String expected = "bawang merah dijual seharga Rp19.999,00/kg di pasar (-6.239879, 106.862344) dilaporkan oleh User pada Sabtu, 11 Jun 2016 12:00";
            Assert.assertEquals(expected, commodityInput.toString());
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetInputsToBePosted() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            Date date = sdf.parse("11-06-2016");
            List<CommodityInput> list = instance.getInputsToBePosted(date, 10, "post_fb");
            assert list != null;
            list = instance.getInputsToBePosted(date, 10, "post_tw");
            assert list != null;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }
}
