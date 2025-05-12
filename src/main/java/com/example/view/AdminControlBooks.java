package com.example.view;

import com.example.model.DanhMucSach;
import com.example.model.Sach;
import com.example.controller.SachController;
import com.example.dao.SachDAO;
import com.example.dao.DanhMucSachDAO;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminControlBooks extends JPanel {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color BLUE = new Color(0, 150, 200);
    private final Color ORANGE = new Color(255, 140, 0);
    private final Color RED = new Color(200, 0, 0);
    private final Color GREEN = new Color(34, 139, 34);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(245, 245, 245);

    // Components
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel searchPanel;

    private JLabel titleLabel;
    private JLabel searchLabel;
    private JLabel noDataLabel;

    private JTextField searchField;

    private JButton addBookButton;
    private JButton editBookButton;
    private JButton deleteBookButton;
    private JButton reloadButton;
    private JButton searchButton;

    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPopupMenu contextMenu;
    private JMenuItem viewMenuItem;
    private JMenuItem editMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem updateStatusMenuItem;
    
    // Controller
    private SachController sachController;

    public AdminControlBooks() {
        // Set up the panel
        setLayout(new BorderLayout());

        // Initialize components
        initComponents();
        
        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        initController();

        // Add event listeners
        addEventListeners();

        // Load initial data
        loadData();
    }
    
    private void initController() {
        SachDAO sachDAO = new SachDAO();
        DanhMucSachDAO danhMucSachDAO = new DanhMucSachDAO();
        sachController = new SachController(this, sachDAO, danhMucSachDAO);
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(WHITE);

        searchPanel = new JPanel();
        searchPanel.setBackground(LIGHT_GRAY);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Initialize labels
        titleLabel = new JLabel("Quản lý sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        noDataLabel = new JLabel("Không tìm thấy kết quả");
        noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        noDataLabel.setForeground(RED);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noDataLabel.setVisible(false);

        // Initialize text fields
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        // Initialize buttons
        addBookButton = createButton("Thêm sách mới", PURPLE);
        editBookButton = createButton("Sửa sách", ORANGE);
        deleteBookButton = createButton("Xóa sách", RED);
        reloadButton = createButton("Reload", BLUE);
        searchButton = createButton("Tìm", GREEN);

        // Initialize table
        String[] columnNames = {
                "Mã sách", "ISBN", "Tên sách", "Tác giả", "Danh mục",
                "Năm xuất bản", "NXB", "Số bản", "Khả dụng", "Vị trí"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setRowHeight(25);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        booksTable.setShowGrid(false);
        booksTable.setIntercellSpacing(new Dimension(0, 0));

        // Set up alternating row colors
        booksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        });

        tableScrollPane = new JScrollPane(booksTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Initialize context menu
        contextMenu = new JPopupMenu();
        viewMenuItem = new JMenuItem("Xem chi tiết");
        editMenuItem = new JMenuItem("Chỉnh sửa");
        deleteMenuItem = new JMenuItem("Xóa");
        updateStatusMenuItem = new JMenuItem("Cập nhật trạng thái");

        contextMenu.add(viewMenuItem);
        contextMenu.add(editMenuItem);
        contextMenu.add(deleteMenuItem);
        contextMenu.add(updateStatusMenuItem);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void layoutComponents() {
        // Set up the main panel with padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title to the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the center panel with buttons, search, and table
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(WHITE);

        // Add buttons to button panel
        buttonPanel.add(addBookButton);
        buttonPanel.add(editBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(reloadButton);

        // Set up search panel
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add components to center panel with spacing
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(searchPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create a panel for the table and no data label
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add no data label on top of the table
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.add(noDataLabel, BorderLayout.CENTER);
        tablePanel.add(overlayPanel, BorderLayout.NORTH);

        centerPanel.add(tablePanel);

        // Add center panel to main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addEventListeners() {
        // Add action listeners to buttons
        addBookButton.addActionListener(e -> addBook());
        editBookButton.addActionListener(e -> editBook());
        deleteBookButton.addActionListener(e -> deleteBook());
        reloadButton.addActionListener(e -> reloadData());

        // Add mouse listener to table for context menu
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Show context menu on right click
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Show context menu on right click
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            private void showContextMenu(MouseEvent e) {
                int row = booksTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < booksTable.getRowCount()) {
                    booksTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Add action listeners to context menu items
        viewMenuItem.addActionListener(e -> viewBookDetails());
        editMenuItem.addActionListener(e -> editBook());
        deleteMenuItem.addActionListener(e -> deleteBook());
        updateStatusMenuItem.addActionListener(e -> updateBookStatus());

        // Add component listener for resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Adjust table size when panel is resized
                tableScrollPane.setPreferredSize(new Dimension(
                        getWidth() - 40,
                        getHeight() - 200
                ));
                revalidate();
            }
        });
    }

    // Data handling methods
    private void loadData() {
        try {
            // Sử dụng SachController để tải dữ liệu
            sachController.loadBooks();
            
            // Show/hide no data label
            noDataLabel.setVisible(tableModel.getRowCount() == 0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Hiển thị Sách lên bảng
    public void displaySachs(List<Sach> sachs) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Sach sach : sachs) {
            Object[] row = {
                    sach.getMaSach(),
                    sach.getISBN(),
                    sach.getTenSach(),
                    sach.getTacGia(),
                    sach.getDanhMucSach().getTenDanhMuc(),
                    sach.getNamXuatBan(),
                    sach.getNXB(),
                    sach.getSoBan(),
                    sach.getKhaDung(),
                    sach.getViTri()
            };
            tableModel.addRow(row);
        }
    }

    private void reloadData() {
        loadData();
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được tải lại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // Book operations
    private void addBook() {
        JOptionPane.showMessageDialog(this, "Chức năng thêm sách mới", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        // This would typically open a form to add a new book
    }

    private void editBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách để chỉnh sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionPane.showMessageDialog(this, "Chỉnh sửa sách có mã: " + bookId, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        // This would typically open a form to edit the selected book
    }

    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = tableModel.getValueAt(selectedRow, 0).toString();
        String bookTitle = tableModel.getValueAt(selectedRow, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa sách '" + bookTitle + "' (Mã: " + bookId + ")?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // This would typically delete the book from the database
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Đã xóa sách thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewBookDetails() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String bookId = tableModel.getValueAt(selectedRow, 0).toString();
        String bookTitle = tableModel.getValueAt(selectedRow, 2).toString();

        JOptionPane.showMessageDialog(
                this,
                "Xem chi tiết sách: " + bookTitle + " (Mã: " + bookId + ")",
                "Chi tiết sách",
                JOptionPane.INFORMATION_MESSAGE
        );
        // This would typically open a form to view book details
    }

    private void updateBookStatus() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách để cập nhật trạng thái.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = tableModel.getValueAt(selectedRow, 0).toString();
        String bookTitle = tableModel.getValueAt(selectedRow, 2).toString();

        JOptionPane.showMessageDialog(
                this,
                "Cập nhật trạng thái sách: " + bookTitle + " (Mã: " + bookId + ")",
                "Cập nhật trạng thái",
                JOptionPane.INFORMATION_MESSAGE
        );
        // This would typically open a form to update book status
    }

    // Getter cho các thành phần UI liên quan đến tìm kiếm
    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JLabel getNoDataLabel() {
        return noDataLabel;
    }

    public JTable getBooksTable() {
        return booksTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Main method for testing
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and display the form
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Book Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.add(new AdminControlBooks());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
