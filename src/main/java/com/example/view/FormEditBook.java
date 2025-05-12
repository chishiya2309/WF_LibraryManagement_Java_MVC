package com.example.view;

import com.example.model.Sach;
import com.example.model.DanhMucSach;
import com.example.dao.SachDAO;
import com.example.dao.DanhMucSachDAO;
import com.example.controller.FormEditBookController;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class FormEditBook extends JFrame {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color LIGHT_GRAY = new Color(240, 240, 240);

    // Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;

    private JLabel titleLabel;
    private JLabel bookIdLabel;
    private JLabel isbnLabel;
    private JLabel bookTitleLabel;
    private JLabel authorLabel;
    private JLabel categoryLabel;
    private JLabel pubYearLabel;
    private JLabel publisherLabel;
    private JLabel copiesLabel;
    private JLabel availableLabel;
    private JLabel locationLabel;

    private JTextField bookIdField;
    private JTextField isbnField;
    private JTextField bookTitleField;
    private JTextField authorField;
    private JComboBox<String> categoryComboBox;
    private JTextField pubYearField;
    private JTextField publisherField;
    private JTextField copiesField;
    private JTextField availableField;
    private JTextField locationField;

    private JButton saveButton;
    private JButton cancelButton;

    // Data access objects
    private SachDAO sachDAO;
    private DanhMucSachDAO danhMucSachDAO;
    private List<DanhMucSach> categories;
    
    // Controller
    private FormEditBookController controller;
    
    // Current book
    private Sach currentBook;

    /**
     * Constructor for the book edit form
     * @param sach Đối tượng Sach để chỉnh sửa
     */
    public FormEditBook(Sach sach) {
        // Set up the frame
        setTitle("Chỉnh sửa thông tin sách");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Lưu thông tin sách hiện tại
        this.currentBook = sach;

        // Initialize data access objects
        sachDAO = new SachDAO();
        danhMucSachDAO = new DanhMucSachDAO();
        
        // Initialize controller
        controller = new FormEditBookController(this, sachDAO, danhMucSachDAO);

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Load categories
        loadCategories();

        // Populate form with book data
        populateBookData();

        // Add event listeners
        addEventListeners();
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridBagLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Set panel backgrounds
        headerPanel.setBackground(PURPLE);
        contentPanel.setBackground(Color.WHITE);
        buttonPanel.setBackground(LIGHT_GRAY);

        // Initialize header label
        titleLabel = new JLabel("Chỉnh sửa sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        bookIdLabel = new JLabel("Mã sách:");
        isbnLabel = new JLabel("ISBN *");
        bookTitleLabel = new JLabel("Tên sách *");
        authorLabel = new JLabel("Tác giả *");
        categoryLabel = new JLabel("Danh mục");
        pubYearLabel = new JLabel("Năm xuất bản");
        publisherLabel = new JLabel("NXB");
        copiesLabel = new JLabel("Số bản");
        availableLabel = new JLabel("Khả dụng");
        locationLabel = new JLabel("Vị trí");

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        bookIdLabel.setFont(labelFont);
        isbnLabel.setFont(labelFont);
        bookTitleLabel.setFont(labelFont);
        authorLabel.setFont(labelFont);
        categoryLabel.setFont(labelFont);
        pubYearLabel.setFont(labelFont);
        publisherLabel.setFont(labelFont);
        copiesLabel.setFont(labelFont);
        availableLabel.setFont(labelFont);
        locationLabel.setFont(labelFont);

        // Initialize text fields
        bookIdField = new JTextField(20);
        isbnField = new JTextField(20);
        bookTitleField = new JTextField(20);
        authorField = new JTextField(20);
        categoryComboBox = new JComboBox<>();
        pubYearField = new JTextField(20);
        publisherField = new JTextField(20);
        copiesField = new JTextField(20);
        availableField = new JTextField(20);
        locationField = new JTextField(20);

        // Make book ID field read-only
        bookIdField.setEditable(false);
        bookIdField.setBackground(new Color(240, 240, 240));

        // Set text field fonts
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);
        bookIdField.setFont(fieldFont);
        isbnField.setFont(fieldFont);
        bookTitleField.setFont(fieldFont);
        authorField.setFont(fieldFont);
        categoryComboBox.setFont(fieldFont);
        pubYearField.setFont(fieldFont);
        publisherField.setFont(fieldFont);
        copiesField.setFont(fieldFont);
        availableField.setFont(fieldFont);
        locationField.setFont(fieldFont);

        // Initialize buttons
        saveButton = new JButton("Lưu thay đổi");
        cancelButton = new JButton("Hủy");

        // Style buttons
        saveButton.setBackground(PURPLE);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void layoutComponents() {
        // Set up the main layout
        setLayout(new BorderLayout());

        // Set up the header panel
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Set up the content panel with GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        // Book ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(bookIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(bookIdField, gbc);

        // ISBN
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(isbnLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(isbnField, gbc);

        // Book Title
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(bookTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(bookTitleField, gbc);

        // Author
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(authorField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(categoryComboBox, gbc);

        // Publication Year
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(pubYearLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(pubYearField, gbc);

        // Publisher
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(publisherLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPanel.add(publisherField, gbc);

        // Copies
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(copiesLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPanel.add(copiesField, gbc);

        // Available
        gbc.gridx = 0;
        gbc.gridy = 8;
        contentPanel.add(availableLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        contentPanel.add(availableField, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 9;
        contentPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        contentPanel.add(locationField, gbc);

        // Add buttons to the button panel
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Add panels to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }

    private void loadCategories() {
        try {
            categories = danhMucSachDAO.getAllDanhMucSach();
            categoryComboBox.removeAllItems();
            
            for (DanhMucSach category : categories) {
                categoryComboBox.addItem(category.getTenDanhMuc());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải danh mục: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void populateBookData() {
        if (currentBook != null) {
            bookIdField.setText(currentBook.getMaSach());
            isbnField.setText(currentBook.getISBN());
            bookTitleField.setText(currentBook.getTenSach());
            authorField.setText(currentBook.getTacGia());
            pubYearField.setText(String.valueOf(currentBook.getNamXuatBan()));
            publisherField.setText(currentBook.getNXB());
            copiesField.setText(String.valueOf(currentBook.getSoBan()));
            availableField.setText(String.valueOf(currentBook.getKhaDung()));
            locationField.setText(currentBook.getViTri());
            
            // Chọn danh mục tương ứng trong combo box
            if (currentBook.getDanhMucSach() != null) {
                String selectedCategory = currentBook.getDanhMucSach().getTenDanhMuc();
                for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                    if (categoryComboBox.getItemAt(i).equals(selectedCategory)) {
                        categoryComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> controller.saveBook());

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> dispose());
    }
    
    // Getter methods for controller to access form fields
    public JTextField getBookIdField() {
        return bookIdField;
    }
    
    public JTextField getIsbnField() {
        return isbnField;
    }
    
    public JTextField getBookTitleField() {
        return bookTitleField;
    }
    
    public JTextField getAuthorField() {
        return authorField;
    }
    
    public JComboBox<String> getCategoryComboBox() {
        return categoryComboBox;
    }
    
    public JTextField getPubYearField() {
        return pubYearField;
    }
    
    public JTextField getPublisherField() {
        return publisherField;
    }
    
    public JTextField getCopiesField() {
        return copiesField;
    }
    
    public JTextField getAvailableField() {
        return availableField;
    }
    
    public JTextField getLocationField() {
        return locationField;
    }
    
    public List<DanhMucSach> getCategories() {
        return categories;
    }
    
    public Sach getCurrentBook() {
        return currentBook;
    }
}
