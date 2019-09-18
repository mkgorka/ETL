/*
  Proces Transform
  @author Anna Munk
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

class Transform {

    static Product run(int productId){

        Product product = null;
        List<Review> reviews;

        try {
            // Laduje HTML z pliku
            String pathName = "C:\\Ceneo/HTML.txt";
            File input = new File(pathName);

            if (! input.exists()) {
                throw new FileNotFoundException("Nie odnaleziono pliku Ÿród³owego z HTML: " + pathName);
            }else{
                Document doc = Jsoup.parse(input, "UTF-8", "");

                reviews = loadReviews(doc);
                product = loadProduct(productId, doc, reviews);
            }

        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("Produkt nie zosta³ za³adowany poprawnie");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return product;
    }

    private static Product loadProduct(int productId, Document doc, List<Review> reviews){

        Elements typeParts;
        StringBuilder type = new StringBuilder();
        String[] brandModelParts;
        String brandName;
        StringBuilder model = new StringBuilder();
        String comment;

        //Odczytywanie typu urzadzenia
        typeParts = doc.select("nav.breadcrumbs").first().select("span.breadcrumb a");
        for (int i = 1; i < typeParts.size(); i++) {
            if (i == 1)
                type = new StringBuilder(typeParts.get(i).text());
            else
                type.append("/").append(typeParts.get(i).text());
        }

        //Odczytywanie marki i modelu. Marka jest pierwszym wyrazem pobranego napisu, pozosta³e stanowi¹ model urz¹dzenia
        brandModelParts = doc.select("strong.js_searchInGoogleTooltip").first().text().split("\\s+");

        brandName = brandModelParts[0];

        for (int i = 1; i < brandModelParts.length; i++) {
            model.append(brandModelParts[i]).append(" ");
        }

        //Odczytywanie komentarza
        comment = doc.select("div.ProductSublineTags").first().text();

        return new Product(productId, type.toString(), brandName, model.toString(), comment, reviews);
    }

    private static List<Review> loadReviews(Document doc){
        //Wczytanie wszystkich recenzji
        Elements reviewsHtml = doc.select("ol.product-reviews li.review-box");

        List<Review> reviews = new ArrayList<>();

        String author;
        String recommendation;
        int usefulnessYes;
        int usefulnessNo;
        int id;
        float stars;
        String reviewBody;
        String datetime;
        List<String> pros;
        List<String> cons;

        for (Element singleRevHtml : reviewsHtml) {
            id = Integer.parseInt(singleRevHtml.select("button.vote-yes").attr("data-review-id"));
            author = singleRevHtml.select("div.reviewer-name-line").text();
            reviewBody = singleRevHtml.select("div.content-wide-col p.product-review-body").first().text();
            reviewBody = reviewBody.replace("\"", "`");
            reviewBody = reviewBody.replace("\'", "`");
            recommendation = singleRevHtml.select("div.product-review-summary em").text();

            usefulnessYes = Integer.parseInt(singleRevHtml.select("button.vote-yes span").text());
            usefulnessNo = Integer.parseInt(singleRevHtml.select("button.vote-no span").text());

            stars = Float.parseFloat(singleRevHtml.select("span.review-score-count").text().split("/")[0].replace(",", "."));
            datetime = singleRevHtml.select("span.review-time time").attr("datetime");

            cons = singleRevHtml.select("div.cons-cell ul li").eachText();
            pros = singleRevHtml.select("div.pros-cell ul li").eachText();

            reviews.add(new Review(id, author, reviewBody, recommendation, usefulnessYes, usefulnessNo, stars, datetime, pros, cons));
        }

        return reviews;
    }
}

