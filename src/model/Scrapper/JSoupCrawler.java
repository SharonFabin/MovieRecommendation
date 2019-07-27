package model.Scrapper;

/*
 * Created by Sharon on 24/09/2017.
 */

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JSoupCrawler implements Crawler {

    private final int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    private ExecutorService threadPool;
    private ArrayList<String> errors;
    private HashMap<String, Future<Document>> documents;
    private String[] urls;
    private Scrapper scrapper;
    public DoubleProperty progressProperty;

    public JSoupCrawler(Scrapper scrapper,boolean cached) {
        initialize(cached);
        this.scrapper=scrapper;
        this.urls=this.scrapper.getUrls();
        progressProperty=new SimpleDoubleProperty(0);
    }

    private void initialize(boolean cached){
        if (cached) threadPool = Executors.newCachedThreadPool();
        else threadPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        documents = new HashMap<>();
        errors = new ArrayList<>();
    }

    @Override
    public void crawl() {
        Arrays.stream(urls).forEach((url)-> documents.put(url, threadPool.submit(() -> Jsoup.connect(url).get())));
        double progressSize = 1.0/documents.size();
        documents.entrySet().forEach(e -> {
            try{
                this.scrapper.scrap(e.getKey(),e.getValue().get());
                progressProperty.set(progressProperty.getValue()+progressSize);
            }catch (Exception ex){
                errors.add(ex.toString());
                ex.printStackTrace();
            }
        });
        errors.forEach(System.out::println);
    }

    public void setUrls(String[] urls){
        this.urls=urls;
    }

}
