package model;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/*
 * Created by Sharon on 30/03/2018.
 */
public class CsvParserTest {

    private CsvParser csvParser;


    @Test
    public void readTest() throws Exception{
        csvParser = new CsvParser("TestResources/book.csv");
        List<String> actual = csvParser.readCsv();
        List<String> expected = List.of("6,5,4","2,3,3","6,5,1","3,5,1");
        assertThat(actual.subList(1,actual.size()),is(expected));
    }

    @Test
    public void parseTest() throws Exception {
        csvParser = new CsvParser("TestResources/book.csv");
        Map<Integer,Double> expected = Map.of(5,6.0,3,3.0);
        Map<Integer,Double> actual = csvParser.mapCsv(1,2,(x,y)->x+y);
        assertThat(actual,is(expected));
    }

    @Test
    public void averageTest() throws Exception {
        csvParser = new CsvParser("TestResources/book.csv");
        Map<Integer,Point> actual = csvParser.mapAverageCsv();
        Map<Integer,Point> expected = Map.of(5,new Point(2,3),3,new Point(3,1));
        actual.forEach((key,val) -> {
            assertEquals(val.getD1(),expected.get(key).getD1(),0.001);
            assertEquals(val.getD2(),expected.get(key).getD2(),0.001);
        });
    }

    @Test
    public void ratingsSortTest() throws Exception {
        csvParser = new CsvParser("resources/ratings.csv");
        List<Integer> actual = csvParser.bestIdsOf(100);
        Map<Integer,Point> map = csvParser.mapAverageCsv();
        //actual.forEach(System.out::println);
        for(int i=1;i<actual.size();i++){
            assertTrue(map.get(actual.get(i-1)).getD1()>=map.get(actual.get(i)).getD1());
        }
    }

    @Test
    public void testMovieTitles() throws Exception{
        csvParser = new CsvParser("resources/movies.csv");
        Map<Integer,String> movies = csvParser.mapMovies(0,1);
        movies.forEach((key,val)-> System.out.printf("Key: %d\tVal: %s\n",key,val));
        assertNotNull(movies);
    }

    @Test
    public void testBestTitles() throws Exception {
        csvParser = new CsvParser("resources/ratings.csv");
        List<Integer> actual = csvParser.bestIdsOf(100);
        csvParser = new CsvParser("resources/movies.csv");
        Map<Integer,String> map = csvParser.mapMovies(0,1);
        actual.forEach(i-> System.out.printf("Name: %s\n",map.get(i)));
        assertNotNull(actual);
    }
}