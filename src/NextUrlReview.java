import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Proces Extract
 * @author Zuzanna Gil
 */

class NextUrlReview {
    private UrlReview getURL;
    private StringBuilder contentBuilder = new StringBuilder();
    private WebClient webClientNext;


    NextUrlReview(UrlReview getURL) {
        this.getURL = getURL;
    }

    /**
     * pobieranie kodu HTML wszystkich kolejnych stron z opiniami dla danego produktu
     */

    String getHTMLCodeNext() {
        if (getURL.getPageCount() > 1) {
            for (int iterator = 2; iterator <= getURL.getPageCountUp(); iterator++) {
                try {
                    webClientNext = new WebClient();
                    webClientNext.setCssErrorHandler(new SilentCssErrorHandler());
                    webClientNext.getOptions().setJavaScriptEnabled(false);
                    HtmlPage currentPage = webClientNext.getPage("https://www.ceneo.pl/" + getURL.getProductCode() + "/opinie-" + iterator);
                    WebResponse response = currentPage.getWebResponse();
                    String htmlCodeNext = response.getContentAsString();
                    contentBuilder.append("https://www.ceneo.pl/").append(getURL.getProductCode()).append("/opinie-").append(iterator);
                    contentBuilder.append("\r\n");
                    contentBuilder.append(htmlCodeNext);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return contentBuilder.toString();
    }
}
