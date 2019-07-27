package model;

import model.Scrapper.JSoupCrawler;
import model.Scrapper.Scrapper;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.List;

import static org.junit.Assert.*;

/*
 * Created by Sharon on 31/03/2018.
 */
public class MovieScrapperTest {

    @Test
    public void urlsTest() {
        String[] movies = {"iron man","hello world"};
        MovieScrapper movieScrapper = new MovieScrapper("http://www.imdb.com/find?ref_=nv_sr_fn&q=",movies,"jack.jpg");
        JSoupCrawler crawler = new JSoupCrawler(movieScrapper,true);
        crawler.crawl();
        for (int i = 0; i < movies.length; i++) {
            System.out.println(movieScrapper.getImageUrl(movies[i]));
        }
        assertNotNull(movieScrapper);
    }
}