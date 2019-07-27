package model.Scrapper;

/*
 * Created by Sharon on 01/04/2018.
 */

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface Scrapper {

    public void scrap(String description, Document document) throws IOException;
    public String[] getUrls();

}
