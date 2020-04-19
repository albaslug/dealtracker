package com.sidenis.dealtracker;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.service.CounterpartyService;
import com.sidenis.dealtracker.util.DbCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DealTrackerApplicationTests {

    @Autowired
    private CounterpartyService counterpartyService;

    @Autowired
    private DbCleaner dbCleaner;

    @AfterEach
    public void cleanDb() {
        dbCleaner.clearAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createAndSelectEqualityTest() {

        // when
        Counterparty c1 = counterpartyService.createCounterparty("test");
        Counterparty c2 = counterpartyService.getCounterparty("test");

        // then
        assertEquals(c1, c2);
    }

}
