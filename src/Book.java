public class Book {
    private String name;
    private String author;
    private Integer edition;
    private Integer available;

    public Book(String name, String author, Integer edition, Integer available) {
        this.name = name;
        this.author = author;
        this.edition = edition;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }


}
