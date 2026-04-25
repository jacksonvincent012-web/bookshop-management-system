public class Book extends Product {
    private String author;
    private String borrower;
    private String dueDate;

    public Book(String id, String title, String author, String borrower, String dueDate) {
        super(id, title);
        this.author = author;
        this.borrower = borrower;
        this.dueDate = dueDate;
    }

    public String getAuthor() { return author; }
    public String getBorrower() { return borrower; }
    public String getDueDate() { return dueDate; }

    @Override
    public String displayDetails() {
        String status = (borrower == null || borrower.isEmpty())
            ? "Available"
            : "Borrowed by " + borrower + (dueDate != null && !dueDate.isEmpty() ? " | Due: " + dueDate : "");
        return "ID: " + getId() + " | Title: " + getTitle() + " | Author: " + author + " | " + status;
    }
}