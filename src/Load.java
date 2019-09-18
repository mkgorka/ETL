/**
 * Proces Load
 * @author Magdalena Górka
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Load {

    private Connection con = null;

    void connect() {

        try {

            //Podlaczenie bazy danych

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://munk.nazwa.pl:3306/munk_HD",
                    "munk_HD", "HurtownieDanych2017");

        } catch (ClassNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

    }

    int loadProduct(Product product) {

        int numberOfInsertedProd = 0;

        if(!isProductRegistered(product.getId()) ) {

            //Zaladowanie danych produktu
            try {

                Statement stmt = con.createStatement();
                numberOfInsertedProd = stmt.executeUpdate("INSERT INTO `Products` ( `id`, `type`, `brand_name`, `model_number`, `comments`)" +
                        "VALUES ('" + product.getId() + "', '" + product.getType() + "', '" + product.getBrandName() + "','" + product.getModel() + "','" + product.getComment() + "')");

            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("NullPointerException" + e.getMessage());
            }
        }

        return numberOfInsertedProd;
    }

    List<Product> selectProducts() {

        // Wczytanie wczeœniej za³adowanych danych dla Products
        List<Product> products = new ArrayList<>();
        List<Review> reviews;

        try {

            String selectProducts = "SELECT * FROM Products";
            Statement stmt = con.createStatement();

            ResultSet result = stmt.executeQuery(selectProducts);
            while (result.next()) {

                int id = result.getInt("id");
                String type = result.getString("type");
                String brand_name = result.getString("brand_name");
                String model = result.getString("model_number");
                String comment = result.getString("comments");

                reviews = selectReviewsForProduct(id);
                products.add(new Product(id, type, brand_name, model, comment, reviews));
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException " + e.getMessage());
        }

        return products;
    }

    int loadReviews(Product product) {

        int numberOfInsertedRev = 0;
        List<Review> reviews = product.getAllReviews();

        //Zapytanie do bazy danych, zaladowanie danych
        try {
            for(Review review : reviews) {

                if (!isReviewRegistered(review.getId())) {

                    Statement stmt = con.createStatement();
                    numberOfInsertedRev += stmt.executeUpdate("INSERT INTO `Reviews` ( `id_rew`, `id_prod`, `author`, `summary`, `pros`, `cons`, `stars_number`, `issue_date`, `recommendation`, `usefulness_yes`, `usefulness_no`)" +
                            "VALUES ('" + review.getId() + "','" + product.getId() + "','" + review.getAuthor() + "','" + review.getReviewBody() + "','" + review.prosToString() + "','" + review.consToString() + "'," +
                            "'" + review.getStars() + "','" + review.getDatetime() + "', '" + review.getRecommendation() + "','" + review.getUsefulnessYes() + "','" + review.getUsefulnessNo() + "')");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException" + e.getMessage());
        }

        return numberOfInsertedRev;
    }

    public List<Review> selectReviews() {

        //Wybranie za³adowanych danych dla Reviews

        List<Review> reviews = new ArrayList<>();
        Review rev;

        try{
            String selectReviews = "SELECT * FROM Reviews";
            Statement stmt = con.createStatement();

            ResultSet result = stmt.executeQuery(selectReviews);

            while (result.next()) {
                int IdRew = result.getInt("id_rew");
                String author = result.getString("author");
                String reviewBody = result.getString("summary");
                String recommendation = result.getString("recommendation");
                String pros = result.getString("pros");
                String cons = result.getString("cons");
                double stars = result.getDouble("stars_number");
                String datetime = result.getString("issue_date");
                int usefulnessYes = result.getInt("usefulness_yes");
                int usefulnessNo = result.getInt("usefulness_no");

                rev = new Review(IdRew, author, reviewBody, recommendation, usefulnessYes, usefulnessNo, stars,  datetime);
                rev.setProsFromString(pros);
                rev.setConsFromString(cons);
                reviews.add(rev);
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException" + e.getMessage());
        }

        return reviews;
    }

    private List<Review> selectReviewsForProduct(Integer productId) {

        //Wybranie za³adowanych recenzji dla produktu o id = productId

        List<Review> reviews = new ArrayList<>();
        Review rev;

        try{
            String selectReviews = "SELECT * FROM Reviews WHERE id_prod = " + productId;
            Statement stmt = con.createStatement();

            ResultSet result = stmt.executeQuery(selectReviews);

            while (result.next()) {
                int IdRew = result.getInt("id_rew");
                String author = result.getString("author");
                String reviewBody = result.getString("summary");
                String recommendation = result.getString("recommendation");
                String pros = result.getString("pros");
                String cons = result.getString("cons");
                double stars = result.getDouble("stars_number");
                String datetime = result.getString("issue_date");
                int usefulnessYes = result.getInt("usefulness_yes");
                int usefulnessNo = result.getInt("usefulness_no");

                rev = new Review(IdRew, author, reviewBody, recommendation, usefulnessYes, usefulnessNo, stars,  datetime);
                rev.setProsFromString(pros);
                rev.setConsFromString(cons);
                reviews.add(rev);

            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException" + e.getMessage());
        }

        return reviews;
    }

    private boolean isProductRegistered(int productId){
        int numbRow = 0;
        boolean isRegistered = false;

        try{
            String selectReviews = "SELECT COUNT(*) FROM Products WHERE id = " + productId;
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(selectReviews);

            while(rs.next()){
                numbRow = rs.getInt("count(*)");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        if(numbRow == 1)
            isRegistered = true;

        return isRegistered;
    }

    private boolean isReviewRegistered(int reviewId){
        int numbRow = 0;
        boolean isRegistered = false;

        try{
            String selectReviews = "SELECT COUNT(*) FROM Reviews WHERE id_rew = " + reviewId;
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(selectReviews);

            while(rs.next()){
                numbRow = rs.getInt("count(*)");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        if(numbRow == 1)
            isRegistered = true;

        return isRegistered;
    }

    int cleanDatabase(){

        int deletedRows = 0;

        try {
            String delete = "DELETE FROM Products";
            Statement stmt = con.createStatement();

            deletedRows = stmt.executeUpdate(delete);

        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Connection failed");
        }

        return deletedRows;
    }


}


