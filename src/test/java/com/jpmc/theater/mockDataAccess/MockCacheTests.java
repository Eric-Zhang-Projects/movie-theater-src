package com.jpmc.theater.mockDataAccess;

import com.jpmc.theater.TestDataBase;
import com.jpmc.theater.model.Showing;
import com.jpmc.theater.provider.LocalDateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

public class MockCacheTests extends TestDataBase {

    private static final LocalDateProvider localDateProvider = LocalDateProvider.singleton();

    @BeforeEach
    public void setup(){
        MockCache.clear();
    }

    @Test
    public void cacheGet_test(){
        List<Showing> actual;

        //Test get null from empty cache
        actual = MockCache.get(localDateProvider.currentDate());
        assertNull(actual);

        //Test get from cache with entry for current date
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache hit, returns non-null schedule. Return schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(schedule);
            actual = MockCache.get(localDateProvider.currentDate());
            assertEquals(schedule, actual);
        }
    }

    @Test
    public void cachePut_test(){
        assertNull(MockCache.get(localDateProvider.currentDate()));

        MockCache.put(localDateProvider.currentDate(), schedule);

        assert(!MockCache.get(localDateProvider.currentDate()).isEmpty());
    }
}
