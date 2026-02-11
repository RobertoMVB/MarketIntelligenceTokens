package com.hypr.marketIntelligenceTokens.analysis;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class TicketStatsAnalyzer {

    public static void analyze(List<TransactionModel> dataset, Path outputDir) throws IOException {
        if (dataset.isEmpty()) {
            System.out.println("Dataset vazio.");
            return;
        }

        int n = dataset.size();

        // Lista de GMVs ordenada
        List<BigDecimal> gmvs = dataset.stream()
                .map(TransactionModel::getGmv)
                .sorted()
                .toList();

        // Média
        BigDecimal total = gmvs.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal mean = total.divide(
                BigDecimal.valueOf(n),
                4,
                RoundingMode.HALF_UP
        );

        // Mediana
        BigDecimal median;
        if (n % 2 == 0) {
            median = gmvs.get(n / 2 - 1)
                    .add(gmvs.get(n / 2))
                    .divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);
        } else {
            median = gmvs.get(n / 2);
        }

        // Desvio padrão
        BigDecimal variance = gmvs.stream()
                .map(v -> v.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(n), 6, RoundingMode.HALF_UP);

        BigDecimal stdDev = sqrt(variance, 6);

        BigDecimal min = gmvs.get(0);
        BigDecimal max = gmvs.get(gmvs.size() - 1);

        // LOG
        System.out.println("=== Ticket Médio e Dispersão ===");
        System.out.println("Transações: " + n);
        System.out.println("Ticket médio: " + mean.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Mediana: " + median.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Desvio padrão: " + stdDev.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Mínimo: " + min.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Máximo: " + max.setScale(2, RoundingMode.HALF_UP));

        // JSON
        String json = """
        {
          "transactions": %d,
          "meanTicket": %.2f,
          "medianTicket": %.2f,
          "standardDeviation": %.2f,
          "minTicket": %.2f,
          "maxTicket": %.2f
        }
        """.formatted(
                n,
                mean.setScale(2, RoundingMode.HALF_UP),
                median.setScale(2, RoundingMode.HALF_UP),
                stdDev.setScale(2, RoundingMode.HALF_UP),
                min.setScale(2, RoundingMode.HALF_UP),
                max.setScale(2, RoundingMode.HALF_UP)
        );

        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve("ticket_stats.json"), json);
    }

    // Raiz quadrada via Newton-Raphson (BigDecimal)
    private static BigDecimal sqrt(BigDecimal value, int scale) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));
        MathContext mc = new MathContext(scale, RoundingMode.HALF_UP);

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = value.divide(x0, mc).add(x0).divide(BigDecimal.valueOf(2), mc);
        }
        return x1;
    }

    private TicketStatsAnalyzer() {}
}
