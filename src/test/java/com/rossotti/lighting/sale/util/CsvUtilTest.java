package com.rossotti.lighting.sale.util;

import com.rossotti.lighting.sale.model.Item;
import com.rossotti.lighting.sale.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CsvUtilTest {
    @Test
    void loadObjectList() {
        List<User> items = CsvUtil.loadObjectList(User.class, "data/sale/users.csv");
        assertThat(items, is(notNullValue()));
        assertThat(items.size(), is(2));
        assertThat((items.get(0).getUsername()), is("john"));
    }
    @Test
    void loadObjectList2() {
        List<Item> items = CsvUtil.loadObjectList(Item.class, "data/sale/windwardExport.csv");
        assertThat(items, is(notNullValue()));
        assertThat(items.size(), is(3));
        assertThat(items.get(0).getItemNumber(), is("LL15525"));
        assertThat(items.get(0).getPartNumber(), is("1360-196C"));
        assertThat(items.get(0).getPicture(), is(""));
        assertThat(items.get(0).getLookupWords(), is("EBAY"));
        assertThat(items.get(0).getCatLocation(), is("CEILING MEDALLION"));
    }
}
