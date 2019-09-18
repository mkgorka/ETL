import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Proces Extract
 * @author Zuzanna Gil
 */

class UrlReview {

    private int productCode;
    private String reviewCount ="0";
    private StringBuilder contentBuilder = new StringBuilder();
    private WebClient webClient;
    private int pageCountUp;

    int getProductCode() {
        return productCode;
    }
    int getPageCountUp() {
        return pageCountUp;
    }
    UrlReview(int productCode) {
        this.productCode = productCode;
    }

    /**
     * pobieranie kodu HTML pierwszej strony z opiniami dla danego produktu, znalezienie w kodzie iloœci opinii
     */

    String HTMLCode(){
        try {
            webClient = new WebClient();
            webClient.setCssErrorHandler(new SilentCssErrorHandler());
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage currentPage = webClient.getPage("https://www.ceneo.pl/" + productCode + "#tab=reviews");
            WebResponse response = currentPage.getWebResponse();
            String htmlCode = response.getContentAsString();
            contentBuilder.append("https://www.ceneo.pl/").append(productCode).append("#tab=reviews");
            contentBuilder.append("\r\n");
            contentBuilder.append(htmlCode);
            String[] splitHTMLCode;
            splitHTMLCode = htmlCode.split("<");
            for (String aSplitHTMLCode : splitHTMLCode) {
                aSplitHTMLCode.trim();
                if (aSplitHTMLCode.startsWith("span itemprop=\"reviewCount\">")) {
                    reviewCount = aSplitHTMLCode.substring(28);
                }
            }
            } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    /**
     * obliczenie iloœci stron z opiniami na podstawie iloœci opinii
     */

    int getPageCount(){
        if(Integer.parseInt(reviewCount)!=0) {
            int reviewCountInt = Integer.parseInt(reviewCount);
            double pageCount = reviewCountInt * 0.1;
            pageCountUp = (int) Math.ceil(pageCount);
            return pageCountUp;
        }
        return 0;
    }
}
