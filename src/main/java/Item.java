public class Item {
    private String title;
    private String url;
    private String price;

    public Item(String title, String url, String price) {
        this.title = title;
        this.url = url;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPrice() {
        return price;
    }
}
