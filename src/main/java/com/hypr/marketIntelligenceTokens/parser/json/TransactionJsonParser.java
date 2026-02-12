package com.hypr.marketIntelligenceTokens.parser.json;

import com.hypr.marketIntelligenceTokens.dto.transaction.TransactionDTO;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TransactionJsonParser {
    private final Set<String> chavesAcesso = ConcurrentHashMap.newKeySet();
    public List<TransactionDTO> process(List<TransactionDTO> transacoes) {
        return transacoes.parallelStream()
                .filter(t -> chavesAcesso.add(t.getReceipt().getAccessKey()))
                .collect(Collectors.toList());
    }
}
