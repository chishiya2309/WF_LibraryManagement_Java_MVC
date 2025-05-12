package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.text.SimpleDateFormat;
import com.example.dao.DanhMucSachDAO;
import com.example.controller.DanhMucSachController;

public class AdminControlCategories extends JPanel {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color DARK_ORANGE = new Color(255, 140, 0);
    private final Color RED = new Color(200, 0, 0);
    private final Color BLUE = new Color(0, 150, 200);
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

    private JButton addCategoryButton;
    private JButton editCategoryButton;
    private JButton deleteCategoryButton;
    private JButton reloadButton;
    private JButton searchButton;

    private JTable categoriesTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPopupMenu contextMenu;
    private JMenuItem viewDetailsMenuItem;
    private JMenuItem editMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem viewBooksMenuItem;

    // MVC components
    private DanhMucSachDAO danhMucSachDAO;
    private DanhMucSachController controller;

    public AdminControlCategories() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize data access object
        danhMucSachDAO = new DanhMucSachDAO();

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();

        // Initialize controller
        controller = new DanhMucSachController(this, danhMucSachDAO);

        // Add event listeners - chỉ giữ lại những event không xử lý logic nghiệp vụ
        addEventListeners();

        // Load initial data
        loadData();
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
        titleLabel = new JLabel("Danh mục sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        noDataLabel = new JLabel("Không tìm thấy kết quả");
        noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        noDataLabel.setForeground(Color.RED);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noDataLabel.setVisible(false);

        // Initialize text fields
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        // Initialize buttons
        addCategoryButton = createButton("Thêm danh mục mới", PURPLE);
        editCategoryButton = createButton("Sửa danh mục", DARK_ORANGE);
        deleteCategoryButton = createButton("Xóa danh mục", RED);
        reloadButton = createButton("Reload", BLUE);
        searchButton = createButton("Tìm", GREEN);

        // Initialize table
        String[] columnNames = {
                "Mã danh mục", "Tên danh mục", "Mô tả", "Danh mục cha",
                "Số lượng sách", "Ngày tạo", "Cập nhật lần cuối", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        categoriesTable = new JTable(tableModel);
        categoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesTable.setRowHeight(25);
        categoriesTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        categoriesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        categoriesTable.setShowGrid(false);
        categoriesTable.setIntercellSpacing(new Dimension(0, 0));

        // Set up alternating row colors
        categoriesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(240, 240, 240));
                }

                // Format status column
                if (column == 7 && value != null) {
                    String status = value.toString();
                    if (status.equals("Hoạt động")) {
                        c.setForeground(GREEN);
                    } else if (status.equals("Ngừng hoạt động")) {
                        c.setForeground(RED);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        tableScrollPane = new JScrollPane(categoriesTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Initialize context menu
        contextMenu = new JPopupMenu();
        viewDetailsMenuItem = new JMenuItem("Xem chi tiết");
        editMenuItem = new JMenuItem("Chỉnh sửa");
        deleteMenuItem = new JMenuItem("Xóa");
        viewBooksMenuItem = new JMenuItem("Xem sách trong danh mục");

        contextMenu.add(viewDetailsMenuItem);
        contextMenu.add(editMenuItem);
        contextMenu.add(deleteMenuItem);
        contextMenu.add(viewBooksMenuItem);
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
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(editCategoryButton);
        buttonPanel.add(deleteCategoryButton);
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
        // Chỉ giữ lại những sự kiện không liên quan đến logic nghiệp vụ
        reloadButton.addActionListener(e -> reloadData());

        // Add mouse listener to table for context menu
        categoriesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewCategoryDetails();
                }
            }

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
                int row = categoriesTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < categoriesTable.getRowCount()) {
                    categoriesTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Add action listeners to context menu items
        viewDetailsMenuItem.addActionListener(e -> viewCategoryDetails());
        editMenuItem.addActionListener(e -> controller.editSelectedCategory());
        viewBooksMenuItem.addActionListener(e -> viewCategoryBooks());

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

    private void loadData() {
        try {
            // Sử dụng SachController để tải dữ liệu
            controller.loadCategories();

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

    // Phương thức để tải lại dữ liệu từ controller
    private void reloadData() {
        try {
            controller.loadCategories();
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được tải lại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu danh mục: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewCategoryDetails() {
        JOptionPane.showMessageDialog(this,
                "Chức năng xem chi tiết danh mục sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewCategoryBooks() {
        JOptionPane.showMessageDialog(this,
                "Chức năng xem sách trong danh mục sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Getter methods for UI components (for controller to access)
    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JLabel getNoDataLabel() {
        return noDataLabel;
    }

    public JTable getCategoriesTable() {
        return categoriesTable;
    }

    public JButton getAddCategoryButton() {
        return addCategoryButton;
    }

    public JButton getEditCategoryButton() {
        return editCategoryButton;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getDeleteCategoryButton() {
        return deleteCategoryButton;
    }

    public JMenuItem getDeleteMenuItem() {
        return deleteMenuItem;
    }
}