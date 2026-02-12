package com.hypr.marketIntelligenceTokens.parser;

import com.hypr.marketIntelligenceTokens.model.ParsedTransaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

public class TransactionCsvParserTest {

    // Formato: "data",token,sku,gmv,quantity,brand
    private static final String VALID_LINE =
            "\"Feb 10, 2026, 01:30:00 PM\",token1,100,10,1,2";

    @Test
    public void testParseValidLine() {
        Optional<ParsedTransaction> parsed = TransactionCsvParser.parse(VALID_LINE);

        assertTrue(parsed.isPresent());

        ParsedTransaction t = parsed.get();
        assertEquals("token1",           t.token());
        assertEquals(Long.valueOf(100),  t.sku());
        assertEquals(new BigDecimal("10"), t.gmv());
        assertEquals(Integer.valueOf(1), t.quantity());
        assertEquals(Integer.valueOf(2), t.brand());
        assertTrue(t.complete());
    }

    @Test
    public void testParseIncompleteTransaction() {
        // gmv e quantity em branco → complete = false
        String line = "\"Feb 10, 2026, 01:30:00 PM\",token1,100,,,,";
        Optional<ParsedTransaction> parsed = TransactionCsvParser.parse(line);

        assertTrue(parsed.isPresent());
        assertFalse(parsed.get().complete());
    }

    @Test
    public void testParseInvalidLine() {
        Optional<ParsedTransaction> parsed = TransactionCsvParser.parse("invalid,line,format");
        assertFalse(parsed.isPresent());
    }

    @Test
    public void testParseLineWithoutQuotedDate() {
        // sem aspas na data → parser rejeita
        Optional<ParsedTransaction> parsed =
                TransactionCsvParser.parse("Feb 10, 2026, 01:30:00 PM,token1,100,10,1,2");
        assertFalse(parsed.isPresent());
    }
}