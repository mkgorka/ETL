import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Proces Extract
 * @author Zuzanna Gil
 */

class TxtFile {
    private String fullHtmlCode;
    private UrlReview getURL;
    private NextUrlReview getNextPages;
    private StringBuilder contentBuilderFinal = new StringBuilder();

    TxtFile(UrlReview getURL, NextUrlReview getNextPages) {
        this.getURL = getURL;
        this.getNextPages = getNextPages;
    }

    /**
     * tworzenie na dysku C pliku txt, w którym bêd¹ zapisane kody HTML wszystkich stron z opiniami
     */

    void createFile() {
        String path = "C:" + File.separator + "Ceneo" + File.separator + "HTML.txt";
        File f = new File(path);
        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ³¹czenie kodów HTML wszystkih stron z opiniami w jeden string
     */

    void bindStrings(){
        contentBuilderFinal.append(getURL.HTMLCode());
        contentBuilderFinal.append("\r\n");
        contentBuilderFinal.append(getNextPages.getHTMLCodeNext());
        fullHtmlCode = contentBuilderFinal.toString();
    }

    /**
     * zapisywanie do pliku txt
     */

    void writeToFile() {
        try {
            FileWriter fw = new FileWriter("C:\\Ceneo/HTML.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fullHtmlCode);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
