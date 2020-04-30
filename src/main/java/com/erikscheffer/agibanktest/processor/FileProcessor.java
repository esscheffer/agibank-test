package com.erikscheffer.agibanktest.processor;

import com.erikscheffer.agibanktest.handler.FileHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FileProcessor {

    private final String COLUMN_SEPARATOR = "ç";
    private final String SALESMAN_ID = "001";
    private final String CLIENT_ID = "002";
    private final String SALE_ID = "003";

    private final FileHandler fileHandler;

    public FileProcessor(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public void processFile(File file) {
        List<String> lines = fileHandler.readFileLines(file);

        Map<String, List<String>> entries =
                lines.stream().collect(Collectors.groupingBy(entry -> entry.split(COLUMN_SEPARATOR)[0]));

        long totalClients = entries.get(CLIENT_ID).size();
        long totalSalesman = entries.get(SALESMAN_ID).size();
        SaleProcessReturn saleProcessReturn = processSalesList(entries.get(SALE_ID));

        fileHandler.writeToFile(
                Arrays.asList(String.valueOf(totalClients),
                        String.valueOf(totalClients),
                        saleProcessReturn.getMostExpensiveSaleId(),
                        saleProcessReturn.getWorstSalesman()),
                file.getName());
    }

    // Calculate the most expensive sale and worst salesman.
    // Both are calculated together to avoid having to pass through the sales list twice
    private SaleProcessReturn processSalesList(List<String> sales) {
        String mostExpensiveSaleId = "";
        BigDecimal mostExpensiveSaleTotal = BigDecimal.ZERO;
        Map<String, Long> salesmanSaleCount = new HashMap<>();

        for (String sale : sales) {
            // Get the sale parts
            String[] salesSplit = sale.split(COLUMN_SEPARATOR);
            String saleId = salesSplit[1];
            String saleItemsString = salesSplit[2];
            String salesmanName = salesSplit[3];

            // Get the sale total and check if it's bigger than the current know biggest
            BigDecimal itemsTotal = calculateSaleTotal(saleItemsString);
            if (itemsTotal.compareTo(mostExpensiveSaleTotal) > 0) {
                mostExpensiveSaleTotal = itemsTotal;
                mostExpensiveSaleId = saleId;
            }

            // Count the sale
            salesmanSaleCount.put(salesmanName, salesmanSaleCount.getOrDefault(salesmanName, 0L));
        }

        return new SaleProcessReturn(mostExpensiveSaleId, calculateWorstSalesman(salesmanSaleCount));
    }

    private BigDecimal calculateSaleTotal(String saleItemsString) {
        String[] items = saleItemsString.replace("[", "").replace("]", "").split(",");
        return Arrays.stream(items)
                .map(item -> {
                    String[] itemInfos = item.split("-");
                    return new BigDecimal(itemInfos[1]).multiply(new BigDecimal(itemInfos[2]));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String calculateWorstSalesman(Map<String, Long> salesmanSaleCount) {
        Optional<Map.Entry<String, Long>> maxEntry = salesmanSaleCount.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue());

        return maxEntry.get().getKey();
    }

    @AllArgsConstructor
    @Getter
    private static class SaleProcessReturn {
        private final String mostExpensiveSaleId;
        private final String worstSalesman;
    }
}
