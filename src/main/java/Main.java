import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<Item> items = new ArrayList<Item>();
    public static final String URL = "https://www.olx.ua/chv/q-macbook/?search%5Border%5D=filter_float_price%3Adesc";
    public static final String PATH = "C:\\Users\\Andrii\\Desktop\\Macbooks.xls";
    public static void main(String[] args) {
        try {
            parse();
        } catch (IOException e) {
            System.out.println("ERROR" + e.getMessage());
        }
        

        try {
            write(items);
        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
        }

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

    private static void write(List<Item> items) throws IOException, BiffException, WriteException {
       File file = new File(PATH);
        WritableWorkbook workbook = Workbook.createWorkbook(file);
        WritableSheet sheet = workbook.createSheet("Sheet0",0);
        sheet.setColumnView(0,5000);
        sheet.setColumnView(1,25);
        sheet.setColumnView(2,5000);

        WritableCellFormat cellHead = new WritableCellFormat();
        WritableCellFormat cellName = new WritableCellFormat();
        WritableCellFormat cellOrder = new WritableCellFormat();

        WritableFont head = new WritableFont(WritableFont.ARIAL,25,WritableFont.BOLD);
        WritableFont name = new WritableFont(WritableFont.TAHOMA,14,WritableFont.BOLD);
        WritableFont order = new WritableFont(WritableFont.TAHOMA,14,WritableFont.NO_BOLD);

        cellHead.setFont(head);
        cellName.setFont(name);
        cellOrder.setFont(order);

        sheet.addCell(new Label(0,0,"Title",cellHead));
        sheet.addCell(new Label(1,0,"Price",cellHead));
        sheet.addCell(new Label(2,0,"Link",cellHead));

        for (int r=0;r<items.size();r++){
            Item item = items.get(r);
            String[] objects = {item.getTitle(),item.getPrice(),item.getUrl()};
            for (int c=0;c<3;c++){
                if (c == 0 ){
                    Label label = new Label(c,r+1,objects[c],cellName);
                    sheet.addCell(label);
                }else{
                    Label label = new Label(c,r+1,objects[c],cellOrder);
                    sheet.addCell(label);
                }
            }
        }

        workbook.write();
        workbook.close();


    }
}
