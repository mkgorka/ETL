/*
  @author Anna Munk
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Product {
    private int id;
    private String type;
    private String brandName;
    private String model;
    private String comment;
    private List<Review> reviews;

    Product(int id, String type, String brandName, String model, String comment, List<Review> reviews){
        this.id = id;
        this.type = type;
        this.brandName = brandName;
        this.model = model;
        this.comment = comment;
        this.reviews = reviews;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("Typ: ").append(type).append("\n");
        str.append("Marka: ").append(brandName).append("\n");
        str.append("Model: ").append(model).append("\n");
        str.append("Uwagi: ").append(comment).append("\n\n");

        for(Review r: reviews){
            str.append(r.toString());
        }
        str.append("\n");
        str.append("--------------------------------------\n");

        return str.toString();
    }

    int getId() {
        return this.id;
    }

    String getType(){
        return type;
    }

    void setType(String type){
        this.type = type;
    }

    String getBrandName(){
        return brandName;
    }

    void setBrandName(String brandName){
        this.brandName = brandName;
    }

    String getModel(){
        return model;
    }

    void setModel(String model){
        this.model = model;
    }

    String getComment(){
        return comment;
    }

    void setComment(String comment){
        this.comment = comment;
    }

    List<Review> getAllReviews(){
        return reviews;
    }

    Review getReview(int index){
        return this.reviews.get(index);
    }

    void setAllReviews(List<Review> rev){
        this.reviews.addAll(rev);
    }

    void setReview(Review review, int index){
        this.reviews.set(index, review);
    }

    boolean saveReviewsToTxt(){
        boolean succeededRevSave;
        int numbOfRevSaved = 0;
        boolean allRevSaved = false;

        for(Review r : this.reviews){
            succeededRevSave = r.saveToTxtFile("product_" + Integer.toString(this.id));

            if(succeededRevSave)
                numbOfRevSaved++;
        }

        if(numbOfRevSaved == this.reviews.size())
            allRevSaved = true;

        return allRevSaved;
    }

    boolean saveToCsvFile(){

        String headerReviews = "id,Autor,Opinia,Czy poleca,Opinia przydatna, Opinia nieprzydatna, Liczba gwiazdek, Data wystawienia, Zalety, Wady";
        String COMMA = ",";
        String NEW_LINE = "\n";
        String QUOTATION_MARKS = "\"";
        File directory = new File("product_" + Integer.toString(this.id));
        FileWriter fileWriter;
        boolean succeeded = true;

        if(!directory.exists()){
            directory.mkdir();
        }

        try{
            fileWriter = new FileWriter( directory + File.separator + "product_" + this.id + ".csv");

            fileWriter.append("id: ").append(COMMA).append(Integer.toString(this.id));
            fileWriter.append(NEW_LINE);
            fileWriter.append("typ: ").append(COMMA).append(QUOTATION_MARKS).append(this.type).append(QUOTATION_MARKS);
            fileWriter.append(NEW_LINE);
            fileWriter.append("marka: ").append(COMMA).append(QUOTATION_MARKS).append(this.brandName).append(QUOTATION_MARKS);
            fileWriter.append(NEW_LINE);
            fileWriter.append("model: ").append(COMMA).append(QUOTATION_MARKS).append(this.model).append(QUOTATION_MARKS);
            fileWriter.append(NEW_LINE);
            fileWriter.append("uwagi: ").append(COMMA).append(QUOTATION_MARKS).append(this.comment).append(QUOTATION_MARKS);

            fileWriter.append(NEW_LINE);
            fileWriter.append(NEW_LINE);

            fileWriter.append(headerReviews);
            fileWriter.append(NEW_LINE);
            for(Review rev : reviews){

                fileWriter.append(Integer.toString(rev.getId()) );
                fileWriter.append(COMMA);
                fileWriter.append(QUOTATION_MARKS).append(rev.getAuthor()).append(QUOTATION_MARKS);
                fileWriter.append(COMMA);
                fileWriter.append(QUOTATION_MARKS).append(rev.getReviewBody().replace("\"", "\'")).append(QUOTATION_MARKS);
                fileWriter.append(COMMA);
                fileWriter.append(QUOTATION_MARKS).append(rev.getRecommendation()).append(QUOTATION_MARKS);
                fileWriter.append(COMMA);
                fileWriter.append( Integer.toString(rev.getUsefulnessYes()) );
                fileWriter.append(COMMA);
                fileWriter.append( Integer.toString(rev.getUsefulnessNo()) );
                fileWriter.append(COMMA);
                fileWriter.append( Double.toString(rev.getStars()) );
                fileWriter.append(COMMA);
                fileWriter.append(QUOTATION_MARKS).append(rev.getDatetime()).append(QUOTATION_MARKS);
                fileWriter.append(COMMA);
                fileWriter.append(QUOTATION_MARKS).append(rev.prosToString()).append(QUOTATION_MARKS);
                fileWriter.append(COMMA);
                fileWriter.append(QUOTATION_MARKS).append(rev.consToString()).append(QUOTATION_MARKS);
                fileWriter.append(NEW_LINE);
            }

            fileWriter.flush();
            fileWriter.close();

        }catch(IOException e){
            succeeded = false;
            System.out.println(e.getMessage());
        }

        return succeeded;
    }
}
