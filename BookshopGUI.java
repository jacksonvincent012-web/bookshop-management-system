import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class BookshopGUI extends JFrame {

    private JTextField tfId, tfTitle, tfAuthor, tfBorrower, tfDate, tfSearch;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Book> books = new ArrayList<>();

    private final Color BG_DARK      = new Color(15,  17,  23);
    private final Color BG_PANEL     = new Color(19,  21,  30);
    private final Color BG_HEADER    = new Color(22,  24,  32);
    private final Color BG_ROW_ALT   = new Color(26,  29,  43);
    private final Color BG_FIELD     = new Color(26,  29,  43);
    private final Color ACCENT_GOLD  = new Color(201, 169, 110);
    private final Color ACCENT_BLUE  = new Color(122, 168, 212);
    private final Color COLOR_TEXT   = new Color(232, 230, 223);
    private final Color COLOR_MUTED  = new Color(122, 139, 166);
    private final Color COLOR_BORDER = new Color(42,  45,  58);
    private final Color GREEN_BADGE  = new Color(74,  222, 128);
    private final Color ORANGE_BADGE = new Color(251, 146,  60);

    public BookshopGUI() {
        setTitle("Bookshop Management System");
        setSize(1100, 640);
        setMinimumSize(new Dimension(900, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadSampleData();
        refreshTable(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HEADER);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        JLabel logo = new JLabel("Bookshop  ·  Management System");
        logo.setFont(new Font("Georgia", Font.BOLD, 16));
        logo.setForeground(ACCENT_GOLD);
        header.add(logo, BorderLayout.WEST);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_PANEL);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER),
            BorderFactory.createEmptyBorder(18, 16, 18, 16)
        ));
        sidebar.setPreferredSize(new Dimension(260, 0));

        addSectionLabel(sidebar, "ADD NEW BOOK");
        tfId       = addFormField(sidebar, "Book ID");
        tfTitle    = addFormField(sidebar, "Title");
        tfAuthor   = addFormField(sidebar, "Author");
        tfBorrower = addFormField(sidebar, "Customer (optional)");
        tfDate     = addFormField(sidebar, "Purchase Date (YYYY-MM-DD)");
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildButton("+ Add Book", ACCENT_GOLD, BG_DARK, e -> addBook()));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(buildButton("Clear Fields", BG_FIELD, COLOR_MUTED, e -> clearForm()));

        sidebar.add(Box.createVerticalStrut(18));
        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(14));

        addSectionLabel(sidebar, "SEARCH");
        tfSearch = addFormField(sidebar, "ID, title, or author...");
        tfSearch.addActionListener(e -> searchBooks());
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(buildButton("Find", ACCENT_BLUE, BG_DARK, e -> searchBooks()));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(buildButton("Show All", BG_FIELD, COLOR_MUTED, e -> clearSearch()));
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private void addSectionLabel(JPanel parent, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(COLOR_BORDER.brighter());
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(8));
    }

    private JTextField addFormField(JPanel parent, String hint) {
        JLabel lbl = new JLabel(hint);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(COLOR_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setBackground(BG_FIELD);
        field.setForeground(COLOR_TEXT);
        field.setCaretColor(ACCENT_GOLD);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(3));
        parent.add(field);
        parent.add(Box.createVerticalStrut(8));
        return field;
    }

    private JButton buildButton(String text, Color bg, Color fg, java.awt.event.ActionListener al) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }

    private JScrollPane buildTablePanel() {
        String[] cols = {"ID", "Title", "Author", "Customer", "Purchase Date", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(BG_DARK);
        table.setForeground(COLOR_TEXT);
        table.setGridColor(BG_ROW_ALT);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(30, 39, 68));
        table.setSelectionForeground(COLOR_TEXT);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        int[] widths = {70, 200, 140, 140, 110, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JTableHeader th = table.getTableHeader();
        th.setBackground(BG_HEADER);
        th.setForeground(COLOR_MUTED);
        th.setFont(new Font("SansSerif", Font.BOLD, 10));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        th.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setBackground(sel ? new Color(30, 39, 68) : (row % 2 == 0 ? BG_DARK : BG_ROW_ALT));
                setForeground(col == 0 ? ACCENT_GOLD : col == 5 ? getStatusColor(v) : COLOR_TEXT);
                setFont(new Font("SansSerif", col == 0 ? Font.BOLD : Font.PLAIN, 12));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private Color getStatusColor(Object val) {
        if (val == null) return COLOR_TEXT;
        return val.toString().equals("In Stock") ? GREEN_BADGE : ORANGE_BADGE;
    }

    private void addBook() {
        String id       = tfId.getText().trim();
        String title    = tfTitle.getText().trim();
        String author   = tfAuthor.getText().trim();
        String customer = tfBorrower.getText().trim();
        String date     = tfDate.getText().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            showMsg("ID, Title, and Author are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(id)) {
                showMsg("A book with ID \"" + id + "\" already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        books.add(new Book(id, title, author, customer, date));
        clearForm();
        refreshTable(null);
        showMsg("Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchBooks() {
        String key = tfSearch.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            showMsg("Enter a search term.", "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getId().toLowerCase().contains(key)
             || b.getTitle().toLowerCase().contains(key)
             || b.getAuthor().toLowerCase().contains(key)) {
                results.add(b);
            }
        }

        refreshTable(results);

        if (results.isEmpty()) {
            showMsg("No books matched \"" + key + "\".", "No Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMsg("Found " + results.size() + " book(s).", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearSearch() {
        tfSearch.setText("");
        refreshTable(null);
    }

    private void clearForm() {
        for (JTextField f : new JTextField[]{tfId, tfTitle, tfAuthor, tfBorrower, tfDate})
            f.setText("");
    }

    private void refreshTable(ArrayList<Book> list) {
        tableModel.setRowCount(0);
        ArrayList<Book> source = (list != null) ? list : books;
        for (Book b : source) {
            String status = (b.getBorrower() == null || b.getBorrower().isEmpty()) ? "In Stock" : "Sold";
            tableModel.addRow(new Object[]{
                b.getId(), b.getTitle(), b.getAuthor(),
                b.getBorrower(), b.getDueDate(), status
            });
        }
    }

    private void loadSampleData() {
        books.add(new Book("BK-001", "The Midnight Library", "Matt Haig",     "Alice Njeri",  "2026-04-10"));
        books.add(new Book("BK-002", "Atomic Habits",        "James Clear",   "",             ""));
        books.add(new Book("BK-003", "Dune",                 "Frank Herbert", "Brian Omondi", "2026-04-15"));
        books.add(new Book("BK-004", "Educated",             "Tara Westover", "",             ""));
    }

    private void showMsg(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new BookshopGUI().setVisible(true));
    }
}
