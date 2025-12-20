package com.example.moviehub.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ParseIntSafeTest {

    @Test
    void parseIntSafe_handlesNullAndBadInput() throws Exception {
        SyncService svc = new SyncService(null, null, null, null);
        Method m = SyncService.class.getDeclaredMethod("parseIntSafe", String.class);
        m.setAccessible(true);

        assertNull(m.invoke(svc, (String) null));
        assertNull(m.invoke(svc, "abc"));
        assertEquals(123, m.invoke(svc, "123"));
    }
}
