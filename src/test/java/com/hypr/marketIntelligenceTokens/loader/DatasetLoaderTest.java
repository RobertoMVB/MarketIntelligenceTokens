package com.hypr.marketIntelligenceTokens.loader;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatasetLoaderTest {

    @Test
    public void testLoad() throws IOException {
        Path tempFile = Files.createTempFile("dataset", ".csv");
        Files.writeString(tempFile,
                "timestamp,token,sku,gmv,quantity,brand\n" +
                        "\"Feb 10, 2026, 01:30:00 PM\",token1,100,10,1,2\n" +
                        "\"Feb 11, 2026, 02:00:00 PM\",token2,101,20,2,3"
        );

        List<TransactionModel> transactions = DatasetLoader.load(tempFile);

        assertEquals(2, transactions.size());

        TransactionModel first = transactions.get(0);
        assertEquals("token1", first.getToken());
        assertEquals(Long.valueOf(100), first.getSku());
        assertEquals(new BigDecimal("10"), first.getGmv());
        assertEquals(Integer.valueOf(1), first.getQuantity());
        assertEquals(Integer.valueOf(2), first.getBrand());
        assertTrue(first.isComplete());

        TransactionModel second = transactions.get(1);
        assertEquals("token2", second.getToken());
        assertEquals(Long.valueOf(101), second.getSku());
        assertEquals(new BigDecimal("20"), second.getGmv());
        assertEquals(Integer.valueOf(2), second.getQuantity());
        assertEquals(Integer.valueOf(3), second.getBrand());
        assertTrue(second.isComplete());
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("dataset_empty", ".csv");
        Files.writeString(tempFile, "timestamp,token,sku,gmv,quantity,brand\n");

        List<TransactionModel> transactions = DatasetLoader.load(tempFile);

        assertEquals(0, transactions.size());
    }
}