package model;/*
 * Created by Sharon on 31/03/2018.
 */

import model.Scrapper.JSoupCrawler;
import model.Scrapper.Scrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MovieScrapper implements Scrapper {

    private Map<String,String> movieUrls;
    private Map<String,String> imgUrls;
    private String defaultImgUrl;

    public MovieScrapper(String url, String[] movies, String defaultImgUrl) {
        this.movieUrls = new HashMap<>();
        this.imgUrls = new HashMap<>();
        String[] urls = new String[movies.length];
        for(int i=0;i<urls.length;i++){
            System.out.println(movies[i]);
            urls[i] = url+Arrays.stream(movies[i].replaceAll("\"","").split(" ")).collect(Collectors.joining("+"));
            this.movieUrls.put(movies[i],urls[i]);
        }
        this.defaultImgUrl=defaultImgUrl;
    }

    @Override
    public void scrap(String description, Document document) throws IOException {
        String link="";
        String imgUrl = defaultImgUrl;
        Element element=null;
        Elements elements = document.select("td.result_text");
        if(elements!=null) elements = elements.select("a[href]");
        if(elements!=null) element = elements.first();
        if(element!=null) link=element.attr("href");
        if(link!="") {
            document = Jsoup.connect("http://www.imdb.com" + link).get();
            imgUrl = document.select("div.poster").select("img").attr("src");
        }
        imgUrls.put(description,imgUrl);
    }

    public String getImageUrl(String movie) {
        return imgUrls.get(movieUrls.get(movie));
    }

    public String[] getUrls(){ return this.movieUrls.values().toArray(new String[0]);}

    public void setDefaultImgUrl(String url){
        this.defaultImgUrl=url;
    }
}
