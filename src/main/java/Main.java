import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<Item> items = new ArrayList<Item>();
    public static final String URL = "https://www.olx.ua/chv/q-macbook/?search%5Border%5D=filter_float_price%3Adesc";
    public static void main(String[] args) {
        try {
            parse();
        } catch (IOException e) {
            System.out.println("ERROR + "+ e.getMessage());
        }

        int count = 0;
        for (Item item : items){
            System.out.println(item.getTitle() + "\n" + item.getPrice() + "\n" + item.getUrl() + "\n--------------------------\n");
            count++;
        }

        //System.out.println("Всего обявлений - " + count);

    }

    private static void parse() throws IOException {
        List<String> titles = new ArrayList<String>();
        List<String> prices = new ArrayList<String>();
        List<String> urls = new ArrayList<String>();

        Document html = Jsoup.connect(URL).get();
        Elements tiles_a = html.getElementsByAttributeValue("class","marginright5 link linkWithHash detailsLink");
        Elements prices_h = html.getElementsByAttributeValue("class","price");

        for (Element e : tiles_a){
            urls.add(e.attr("href"));
            titles.add(e.child(0).text());
        }

        for (Element e : prices_h){
            prices.add(e.child(0).text());
        }

        if (titles.size() == prices.size() && prices.size()==urls.size()){
            for (int i=0;i<titles.size();i++){
                Item item = new Item(titles.get(i),urls.get(i),prices.get(i));
                items.add(item);
            }
        }else
            System.out.println("DAMN");
    }
}
