package com.rossotti.lighting.sale.util;

import com.rossotti.lighting.sale.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CsvUtilTest {
    @Test
    void loadObjectList() {
        List<User> items = CsvUtil.loadObjectList(User.class, "data/sale/users-with-header.csv");
        assertThat(items, is(notNullValue()));
        assertThat(items.size(), is(2));
        assertThat((items.get(0).getUsername()), is("john"));
    }
}
