package model;/*
 * Created by Sharon on 30/03/2018.
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class CsvParser {

    private String filePath;

    public CsvParser(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(String filePath){
        this.filePath=filePath;
    }

    /**
     * @return List of lines of csv file.
     * @throws Exception
     */
    public List<String> readCsv() throws Exception {
        return Files.lines(Paths.get(this.filePath)).collect(Collectors.toList());
    }

    /**
     * @param keyColumn     key column in csv file.
     * @param valueColumn   value column in csv file.
     * @param mergeFunction merge function when keys match.
     * @return Map of ID (Integer) corresponding to Value (Double).
     * @throws Exception
     */
    public Map<Integer, Double> mapCsv(int keyColumn, int valueColumn, BinaryOperator<Double> mergeFunction) throws Exception {
        return Files.lines(Paths.get(this.filePath))
                .skip(1)
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        (String[] key) -> Integer.parseInt(key[keyColumn]),
                        value -> Double.parseDouble(value[valueColumn]),
                        mergeFunction));
    }

    /**
     * Maps movies IDs to movies names.
     *
     * @param keyColumn   key column in csv file.
     * @param valueColumn value column in csv file.
     * @return Map of ID (Integer) corresponding to Value (String).
     * @throws Exception
     */
    public Map<Integer, String> mapMovies(int keyColumn, int valueColumn) throws Exception {
        return Files.lines(Paths.get(this.filePath))
                .skip(1)
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        (String[] key) -> Integer.parseInt(key[keyColumn]),
                        val -> val[valueColumn]));
    }

    /**
     * Maps movies IDs to their average rating and occurrences.
     *
     * @return Map of ID (Integer) corresponding to average rating and occurrences (Point).
     * @throws Exception
     */
    public Map<Integer, Point> mapAverageCsv() throws Exception {
        return Files.lines(Paths.get(this.filePath))
                .skip(1)
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        (String[] key) -> Integer.parseInt(key[1]),
                        value -> new Point(Double.parseDouble(value[2]), 1),
                        (p1, p2) -> new Point((p1.getD1() * p1.getD2() + p2.getD1()) / (p1.getD2() + 1), p1.getD2() + 1),
                        () -> new TreeMap<>(Comparator.naturalOrder())));
    }

    /**
     * Returns a list of top movie IDs.
     *
     * @param limit number of top IDs to return.
     * @return List of top <limit> IDs.
     * @throws Exception
     */
    public List<Integer> bestIdsOf(int limit) throws Exception {
        return this.mapAverageCsv()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(Point::getD1).reversed()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Gets most frequent rated movies in csv file.
     *
     * @param limit number of movie IDs to return.
     * @return List of <limit> most frequently rated movies IDs.
     * @throws Exception
     */
    public List<Integer> getFrequentMovies(int limit) throws Exception {
        return this.mapAverageCsv()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(Point::getD2).reversed()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


}
