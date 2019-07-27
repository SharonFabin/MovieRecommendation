package model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<Integer, Double> userRatings; //<movieID,Rating> my Model
    private User bestUser, tmpUser;

    public Model() {
        userRatings = new HashMap<>();
    }

    /**
     * Updates current user's ratings matrix.
     *
     * @param userRatings ratings matrix to update.
     */
    public void updateRatings(Map<Integer, Double> userRatings) {
        this.userRatings = userRatings;
    }

    /**
     * Finds most fit user from csv file to user's ratings.
     *
     * @param filePath path to csv file.
     * @return user from csv file that matches to user's ratings.
     * @throws Exception
     */
    public User readUserToUser(String filePath) throws Exception {
        bestUser = new User(1);
        tmpUser = bestUser;
        Files.lines(Paths.get(filePath))
                .skip(1)
                .map(line -> line.split(","))
                .forEach(this::processLine);
        checkBestUser(tmpUser.getId());
        return bestUser;
    }

    private void processLine(String[] line) {
        int id = Integer.parseInt(line[0]);
        int movieId = Integer.parseInt(line[1]);
        double rating = Double.parseDouble(line[2]);
        if (tmpUser.getId() != id) checkBestUser(id);
        if (userRatings.containsKey(movieId)) tmpUser.putMovieRating(movieId, rating);
        tmpUser.putMovies(movieId);

    }

    private void checkBestUser(int id) {
        tmpUser.setCorrelation(calculatePearson(tmpUser));
        if (Math.abs(tmpUser.getCorrelation()) > Math.abs(bestUser.getCorrelation())) bestUser = tmpUser;
        tmpUser = new User(id);
    }


    /**
     * Calculate pearson correlation of current user to other user.
     *
     * @param user user to check correlation with.
     * @return pearson correlation between current user and other user.
     */
    public double calculatePearson(User user) {
        Map<Integer, Double> movieRatings = user.getMovieRatings();
        double ans = 0;
        int numOfmovies = movieRatings.size();
        double myUserRating;
        double userRating;
        double multiXY = 0;
        double sumX = 0;
        double sumY = 0;
        double squareX = 0;
        double squareY = 0;
        if (numOfmovies > 2) {
            for (int id : movieRatings.keySet()) {
                myUserRating = userRatings.get(id);
                userRating = movieRatings.get(id);
                sumX += myUserRating;
                sumY += userRating;
                squareX += (Math.pow(myUserRating, 2));
                squareY += (Math.pow(userRating, 2));
                multiXY += (userRating * myUserRating);
            }
            ans = ((numOfmovies * multiXY) - (sumX * sumY)) /
                    (Math.sqrt(numOfmovies * squareX - Math.pow(sumX, 2)) * Math.sqrt(numOfmovies * squareY - Math.pow(sumY, 2)));
        }
        return ans;
    }
}