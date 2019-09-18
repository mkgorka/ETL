/*
  @author Anna Munk
 */

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

public class Review {
    private int id;
    private String author = "anonim";
    private String reviewBody;
    private String recommendation;
    private int usefulnessYes;
    private int usefulnessNo;
    private double stars;
    private String datetime;
    private List<String> pros;
    private List<String> cons;


    Review(int id, String author, String reviewBody, String recommendation, int usefulnessYes, int usefulnessNo, double stars, String datetime, List<String> pros, List<String> cons){
        this.id = id;
        this.author = author;
        this.reviewBody = reviewBody;
        this.recommendation = recommendation;
        this.usefulnessYes = usefulnessYes;
        this.usefulnessNo = usefulnessNo;
        this.stars = stars;
        this.datetime = datetime;

        if(pros == null)
            this.pros = new ArrayList<>();
        else
            this.pros = new ArrayList<>(pros);

        if(cons == null)
            this.cons = new ArrayList<>();
        else
            this.cons = new ArrayList<>(cons);
    }

    Review(int id, String author, String reviewBody, String recommendation, int usefulnessYes, int usefulnessNo, double stars, String datetime){
        this.id = id;
        this.author = author;
        this.reviewBody = reviewBody;
        this.recommendation = recommendation;
        this.usefulnessYes = usefulnessYes;
        this.usefulnessNo = usefulnessNo;
        this.stars = stars;
        this.datetime = datetime;

        this.pros = new ArrayList<>();
        this.cons = new ArrayList<>();
    }

    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("|Autor| ").append(this.author).append("\n");
        str.append("|Polecenie| ").append(this.recommendation).append("\n");
        str.append("|Opinia przydatna| ").append(this.usefulnessYes).append(" |Nieprzydatna| ").append(this.usefulnessNo).append("\n");
        str.append("|Liczba gwiazdek| ").append(this.stars).append("\n");
        str.append("|Wystawiono| ").append(this.datetime).append("\n");
        str.append("|Opinia| ").append(this.reviewBody).append("\n");

        str.append("|Zalety| " + "\n");

        if(!pros.isEmpty()){
            for( String p : pros)
                str.append(p).append(", ");
        }

        str.append("\n");
        str.append("|Wady| \n");

        if(!cons.isEmpty()){
            for( String c : cons)
                str.append(c).append(", ");
        }
        str.append("\n");

        return str.toString();
    }

    int getId(){
        return this.id;
    }

    String getAuthor(){
        return author;
    }

    void setAuthor(String author){
        this.author = author;
    }

    String getReviewBody(){
        return reviewBody;
    }

    void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    String getRecommendation(){
        return recommendation;
    }

    void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    int getUsefulnessYes(){
        return usefulnessYes;
    }

    void setUsefulnessYes(int usefulnessYes){
        this.usefulnessYes = usefulnessYes;
    }

    int getUsefulnessNo(){
        return usefulnessNo;
    }

    public void setUsefulnessNo(int usefulnessNo) {
        this.usefulnessNo = usefulnessNo;
    }

    double getStars(){
        return stars;
    }

    public void setStars(float stars){
        this.stars = stars;
    }

    String getDatetime(){
        return datetime;
    }

    void setDatetime(String datetime){
        this.datetime = datetime;
    }

    String prosToString(){
        StringBuilder text = new StringBuilder();

        if(!this.pros.isEmpty()) {
            for (int i = 0; i < this.pros.size(); i++) {
                if(i > 0)
                    text.append(",").append(this.pros.get(i));
                else
                    text = new StringBuilder(this.pros.get(i));
            }
        }

        return text.toString();
    }

    void setProsFromString(String str){
        String[] prosParts = str.split(",");

        for(int i = 0; i < (prosParts.length-1); i++){
            this.pros.add( prosParts[i].trim());
        }
    }

    String consToString(){
        StringBuilder text = new StringBuilder();

        if(!this.cons.isEmpty()) {
            for (int i = 0; i < this.cons.size(); i++) {
                if(i > 0)
                    text.append(",").append(this.cons.get(i));
                else
                    text = new StringBuilder(this.cons.get(i));
            }
        }

        return text.toString();
    }

    void setConsFromString(String str){
        String[] consParts = str.split(",");

        for(int i = 0; i < (consParts.length-1); i++){
            this.cons.add( consParts[i].trim());
        }
    }

    boolean saveToTxtFile(String directoryName){

        File directory = new File(directoryName);
        boolean succeeded = true;

        if(!directory.exists()){
            directory.mkdir();
        }

        try{
            FileWriter fileWriter = new FileWriter( directory + File.separator + "review_" + this.id + ".txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(this.toString());
            printWriter.close();

        }catch(IOException e){
            succeeded = false;
            System.out.println(e.getMessage());
        }
        return succeeded;
    }

}
