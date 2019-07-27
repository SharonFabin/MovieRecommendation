package model.Scrapper;

/*
 * Created by Sharon on 24/09/2017.
 */

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class RecipeData extends Data {

    private String name;
    private ArrayList<String> ingredients;
    private ArrayList<String> recipes;

    public RecipeData(Document document) {
        super(document);
        ingredients = new ArrayList<>();
        recipes = new ArrayList<>();
    }

    //public

    public void findData() {
        findName();
        findIngredients();
    }

    public void findRecipes(){
        Elements recipesE = document.select("section").select("img");
        for(Element e:recipesE){
            recipes.add(e.parent().html());
        }
    }

    private void findName() {
        this.name = document.select("h1").text();
    }

    private void findIngredients() {
        String[] tags = {"p", "h1", "h2", "h3", "span"};
        String[] keyWords = {"רכיבים","מצרכים"};
        String[] listTags = {"p", "li"};
        Elements elements;
        for (String tag : tags) {
            for (String key : keyWords) {
                for (String listTag : listTags) {
                    elements = document.select(tag + ":contains(" + key + ")");
                    if (elements != null && elements.first() != null) {
                        elements = elements.first().parent().select(listTag);
                        for (Element e : elements) ingredients.add(e.text());
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getRecipes() {
        return recipes;
    }
}
