package model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ModelTest {

    private Model model;

    @Test
    public void pearsonTest() {
        model = new Model();
        User user = new User(1);
        user.putMovieRating(0,0.5);
        user.putMovieRating(1,1);
        user.putMovieRating(2,2);

        Map<Integer,Double> userRatings = new HashMap<>();
        userRatings.put(0,0.5);
        userRatings.put(1,0.7);
        userRatings.put(2,2.0);
        model.updateRatings(userRatings);

        assertEquals(0.977951,model.calculatePearson(user),0.0001);

    }

    @Test
    public void readUserToUserTest() throws Exception{
        model = new Model();
        User user = new User(0);
        user.putMovieRating(0,1);
        user.putMovieRating(1,1);
        user.putMovieRating(2,5);
        user.putMovieRating(3,5);
        model.updateRatings(user.getMovieRatings());

        User bestUser = model.readUserToUser("TestResources/modelCsvTest.csv");
        System.out.println(bestUser.getId());
        assertEquals(3,bestUser.getId());
    }

    @Test
    public void readUserToUserTest2() throws Exception{
        /*
        211,848,5.0,1460811636
        211,969,5.0,1460810939
        211,999,4.0,1460811608
         */
        model = new Model();
        User user = new User(0);
        user.putMovieRating(848,5.0);
        user.putMovieRating(969,5.0);
        user.putMovieRating(999,2.0);
        model.updateRatings(user.getMovieRatings());
        User bestUser = model.readUserToUser("resources/ratings.csv");
        assertEquals(211,bestUser.getId());
    }
}