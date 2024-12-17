/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop.GUI;

//import project_oop.Structure.Khoa;
import com.toedter.calendar.JDateChooser;
import project_oop.Structure.BenhNhan;
import project_oop.Structure.BacSi;
import project_oop.Connection.DBUtils;
import project_oop.Connection.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import project_oop.Structure.Khoa;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UI1 extends JFrame {
    private final JPanel navigationPanel;
    private JPanel contentPanel;
    private final JPanel headerPanel;
    private JPanel homePanel;
    private JPanel bacSiPanel;
    private JPanel benhNhanPanel;
    private JTable doctorTable;
    private JTable patientTable;
    private DefaultTableModel doctorTableModel;
    private DefaultTableModel patientTableModel;
    private JButton refreshButton;

    public UI1() {
        setTitle("HỆ THỐNG QUẢN LÝ BỆNH VIỆN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Colors
        Color primaryColor = new Color(0, 102, 204); // Header xanh đậm
        Color bgColor = new Color(230, 247, 255);    // Nền xanh pastel
        Color hoverColor = new Color(0, 153, 255);   // Hover xanh sáng
        
        // Header Panel
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);

        navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        navigationPanel.setBackground(primaryColor);
        
        createHeader(primaryColor, hoverColor);
        createHomePanel(bgColor, primaryColor);
        createBacSiPanel(bgColor, primaryColor);
        createBenhNhanPanel(bgColor, primaryColor);

        showPanel("home");

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        refreshDoctorTable();
        refreshPatientTable();
    }

    private void createHeader(Color primaryColor, Color hoverColor) {
//        headerPanel = new JPanel();
//        headerPanel.setLayout(new BorderLayout());
//        headerPanel.setBackground(Color.WHITE);

//        navigationPanel = new JPanel();
//        navigationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
//        navigationPanel.setBackground(Color.WHITE);

        String[] navItems = {"Trang Chủ", "Bác Sĩ", "Bệnh Nhân", "Các Khoa"};
        for (String item : navItems) {
            JButton button = new JButton(item);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);

            button.setBackground(primaryColor);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(primaryColor);
                }
            });
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.addActionListener(e -> handleNavigation(item));
            navigationPanel.add(button);
        }
        
        headerPanel.add(navigationPanel, BorderLayout.CENTER);
    }
    
//    Search
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(200, 30));

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setBackground(new Color(0, 102, 204));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        // Tạo JPopupMenu cho gợi ý
        JPopupMenu suggestionsPopup = new JPopupMenu();
        suggestionsPopup.setFocusable(false); // Không để bảng gợi ý chiếm quyền điều khiển con trỏ văn bản

        // Thêm DocumentListener với debounce
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private Timer typingTimer; // Timer để debounce

            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextChange();
            }

            private void handleTextChange() {
                if (typingTimer != null) {
                    typingTimer.stop();  // Dừng timer cũ nếu có
                }

                // Tạo timer mới với độ trễ 300ms
                typingTimer = new Timer(300, e -> showSuggestions());
                typingTimer.setRepeats(false);  // Chạy một lần sau thời gian trễ
                typingTimer.start();
            }

            private void showSuggestions() {
                String text = searchField.getText();
                if (text.length() >= 2) {
                    List<String> suggestions = getSuggestions(text);
                    if (!suggestions.isEmpty()) {
                        suggestionsPopup.removeAll();
                        for (String suggestion : suggestions) {
                            JMenuItem item = new JMenuItem(suggestion);
                            item.addActionListener(e -> {
                                searchField.setText(suggestion);
                                suggestionsPopup.setVisible(false);
                                performSearch(suggestion);
                            });
                            suggestionsPopup.add(item);
                        }
                        // Đảm bảo gợi ý không che mất trường tìm kiếm
                        suggestionsPopup.show(searchField, 0, searchField.getHeight());
                    } else {
                        suggestionsPopup.setVisible(false);
                    }
                } else {
                    suggestionsPopup.setVisible(false);
                }
            }
        });

        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }


    private List<String> getSuggestions(String text) {
        List<String> suggestions = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            if (contentPanel == bacSiPanel) {
                List<BacSi> allDoctors = DBUtils.listBacSi(conn);
                suggestions = allDoctors.stream()
                        .filter(doctor ->
                                doctor.getHoTen().toLowerCase().contains(text.toLowerCase()) ||
                                        doctor.getID().toLowerCase().contains(text.toLowerCase()) ||
                                        doctor.getChucVu().toLowerCase().contains(text.toLowerCase()) ||
                                        doctor.getChuyenKhoa().getTenKhoa().toLowerCase().contains(text.toLowerCase())
                        )
                        .map(BacSi::getHoTen)
                        .distinct()
                        .limit(5)
                        .collect(Collectors.toList());
            } else if (contentPanel == benhNhanPanel) {
                List<BenhNhan> allPatients = DBUtils.listBenhNhan(conn);
                suggestions = allPatients.stream()
                        .filter(patient ->
                                patient.getHoTen().toLowerCase().contains(text.toLowerCase()) ||
                                        patient.getMaBN().toLowerCase().contains(text.toLowerCase()) ||
                                        patient.getTenBenhAn().toLowerCase().contains(text.toLowerCase()) ||
                                        patient.getKhoaKham().getTenKhoa().toLowerCase().contains(text.toLowerCase())
                        )
                        .map(BenhNhan::getHoTen)
                        .distinct()
                        .limit(5)
                        .collect(Collectors.toList());
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy gợi ý: " + ex.getMessage());
        }
        return suggestions;
    }

    // Add search functionality
    private void performSearch(String searchText) {
        if (contentPanel == bacSiPanel) {
            searchDoctors(searchText);
        } else if (contentPanel == benhNhanPanel) {
            searchPatients(searchText);
        }
    }
    
    private void createHomePanel(Color bgColor, Color primaryColor) {
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(bgColor);

        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống Quản lý Bệnh viện!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        welcomeLabel.setForeground(primaryColor);
        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        
    }

    private void createBacSiPanel(Color bgColor, Color primaryColor) {
        bacSiPanel = new JPanel();
        bacSiPanel.setLayout(new BorderLayout());
        bacSiPanel.setBackground(bgColor);

        JLabel bacSiLabel = new JLabel("Danh sách Bác Sĩ", SwingConstants.CENTER);
        bacSiLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bacSiPanel.add(bacSiLabel, BorderLayout.NORTH);

        doctorTableModel = new DefaultTableModel(
            new Object[]{"ID", "Họ tên", "Ngày sinh", "Địa chỉ", "Giới tính", "Số điện thoại", "Quốc tịch",
                        "Mã chuyên khoa", "Chức vụ", "Ngày vào làm việc", "Lương cơ bản", "Hệ số lương"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(doctorTableModel);
        customizeTable(doctorTable, primaryColor);
        doctorTable.setRowSelectionAllowed(true);
        doctorTable.setColumnSelectionAllowed(false);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Đặt width cho các cột
        doctorTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        doctorTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // hoTen
        doctorTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // ngaySinh
        doctorTable.getColumnModel().getColumn(3).setPreferredWidth(150);  // diaChi
        doctorTable.getColumnModel().getColumn(4).setPreferredWidth(60);   // gioiTinh
        doctorTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // sdt
        doctorTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // quocTich
        doctorTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // maChuyenKhoa
        doctorTable.getColumnModel().getColumn(8).setPreferredWidth(100);  // chucVu
        doctorTable.getColumnModel().getColumn(9).setPreferredWidth(100);  // ngayVaoLamViec
        doctorTable.getColumnModel().getColumn(10).setPreferredWidth(100); // luongCoBan
        doctorTable.getColumnModel().getColumn(11).setPreferredWidth(80);  // heSoLuong

        // Thêm renderer cho cột "Lương cơ bản" để hiện đầy đủ số
        doctorTable.getColumnModel().getColumn(10).setCellRenderer(new DefaultTableCellRenderer() {
            private final DecimalFormat df = new DecimalFormat("#,###"); // Định dạng số

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number number) {
                    value = df.format(number.longValue()); // Chuyển đổi số
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        bacSiPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton sortButton = new JButton("Sắp xếp");
        JButton filterButton = new JButton("Lọc");

        Dimension buttonSize = new Dimension(150, 40);
        addButton.setMaximumSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        sortButton.setMaximumSize(buttonSize);
        filterButton.setMaximumSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(sortButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(filterButton);

        addButton.addActionListener(e -> addDoctor());
        editButton.addActionListener(e -> editDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        sortButton.addActionListener(e -> sortDoctors());
        filterButton.addActionListener(e -> filterDoctors());

        bacSiPanel.add(buttonPanel, BorderLayout.EAST);

        // Popup menu
        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) { // Windows
                    showDoctorPopupMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) { // Mac/Linux
                    showDoctorPopupMenu(e);
                }
            }
        });
    }


    private void createBenhNhanPanel(Color bgColor, Color primaryColor) {
        benhNhanPanel = new JPanel();
        benhNhanPanel.setLayout(new BorderLayout());
        benhNhanPanel.setBackground(bgColor);

        JLabel benhNhanLabel = new JLabel("Danh sách Bệnh Nhân", SwingConstants.CENTER);
        benhNhanLabel.setFont(new Font("Arial", Font.BOLD, 24));
        benhNhanPanel.add(benhNhanLabel, BorderLayout.NORTH);

        patientTableModel = new DefaultTableModel(
            new Object[]{"Mã bệnh nhân", "Họ tên", "Tên bệnh án", "Ngày khám", "Ngày sinh",
                        "Địa chỉ", "Giới tính", "Số điện thoại", "Quốc tịch", "Mã khoa khám", "Nhập viện"}, 0
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(patientTableModel);
        customizeTable(patientTable, primaryColor);
        patientTable.setRowSelectionAllowed(true);      // Chọn hàng
        patientTable.setColumnSelectionAllowed(false);  // Không cho phép chọn cột
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        patientTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // maBN
        patientTable.getColumnModel().getColumn(1).setPreferredWidth(120); // hoTen
        patientTable.getColumnModel().getColumn(2).setPreferredWidth(120); // tenBenhAn
        patientTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // ngayKham
        patientTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // ngaySinh
        patientTable.getColumnModel().getColumn(5).setPreferredWidth(150); // diaChi
        patientTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // gioiTinh
        patientTable.getColumnModel().getColumn(7).setPreferredWidth(100); // sdt
        patientTable.getColumnModel().getColumn(8).setPreferredWidth(80);  // quocTich
        patientTable.getColumnModel().getColumn(9).setPreferredWidth(60);  // maKhoaKham
        patientTable.getColumnModel().getColumn(10).setPreferredWidth(60); // nhapVien

        JScrollPane scrollPane = new JScrollPane(patientTable);
        benhNhanPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton sortButton = new JButton("Sắp xếp");
        JButton filterButton = new JButton("Lọc");

        Dimension buttonSize = new Dimension(150, 40);
        addButton.setMaximumSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        sortButton.setMaximumSize(buttonSize);
        filterButton.setMaximumSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(sortButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(filterButton);
        
        addButton.addActionListener(e -> addPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());
        sortButton.addActionListener(e -> sortPatients());
        filterButton.addActionListener(e -> filterPatients());


        benhNhanPanel.add(buttonPanel, BorderLayout.EAST);
        // Thêm MouseListener để lắng nghe chuột phải
        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) { // Xử lý cho sự kiện popup trên Windows
                    showPatientPopupMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) { // Xử lý cho sự kiện popup trên Mac/Linux
                    showPatientPopupMenu(e);
                }
            }
        });
    }
    
    private void refreshDoctorTable() {
        try {
            Connection conn = DBConnection.getConnection();
            List<BacSi> bacSiList = DBUtils.listBacSi(conn);
            updateDoctorTable(bacSiList);

        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu bác sĩ: " + ex.getMessage());
        }
    }

    private void updateDoctorTable(List<BacSi> bacSiList) {
        doctorTableModel.setRowCount(0);
        for (BacSi bs : bacSiList) {
            doctorTableModel.addRow(new Object[]{
                    bs.getID(),
                    bs.getHoTen(),
                    bs.getNgaySinh(),
                    bs.getDiaChi(),
                    bs.getGioiTinh(),
                    bs.getSdt(),
                    bs.getQuocTich(),
                    bs.getChuyenKhoa().getMaKhoa(),
                    bs.getChucVu(),
                    bs.getNgayVaoLamViec(),
                    bs.getLuongCoBan(),
                    bs.getHeSoLuong()
            });
        }
    }

    private void refreshPatientTable() {
        try {
            Connection conn = DBConnection.getConnection();
            List<BenhNhan> benhNhanList = DBUtils.listBenhNhan(conn);
            updatePatientTable(benhNhanList);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu bệnh nhân: " + ex.getMessage());
        }
    }

    private void updatePatientTable(List<BenhNhan> benhNhanList) {
        patientTableModel.setRowCount(0);
        for (BenhNhan bn : benhNhanList) {
            patientTableModel.addRow(new Object[]{
                    bn.getMaBN(),
                    bn.getHoTen(),
                    bn.getTenBenhAn(),
                    bn.getNgayKham(),
                    bn.getNgaySinh(),
                    bn.getDiaChi(),
                    bn.getGioiTinh(),
                    bn.getSdt(),
                    bn.getQuocTich(),
                    bn.getKhoaKham().getMaKhoa(),
                    bn.isNhapVien() ? "1" : "0"
            });
        }
    }

    private void handleNavigation(String item) {
        switch (item) {
            case "Trang Chủ" -> showPanel("home");
            case "Bác Sĩ" -> showPanel("bacsi");
            case "Bệnh Nhân" -> showPanel("benhnhan");
            case "Các Khoa" -> showKhoaList();
        }
    }

    private void showPanel(String panelName) {
        if (contentPanel != null) {
            remove(contentPanel);
        }

        // Remove the refresh button if it exists
        removeRefreshButton();

        // Remove existing search panel if any
        Component[] components = headerPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && component != navigationPanel) {
                headerPanel.remove(component);
            }
        }

        switch (panelName) {
            case "home" -> {
                contentPanel = homePanel;
                // Ensure search panel is removed when returning to home
                headerPanel.remove(createSearchPanel());
            }
            case "bacsi" -> {
                contentPanel = bacSiPanel;
                headerPanel.add(createSearchPanel(), BorderLayout.EAST);
                showRefreshButton("DoctorPanel");
            }
            case "benhnhan" -> {
                contentPanel = benhNhanPanel;
                headerPanel.add(createSearchPanel(), BorderLayout.EAST);
                showRefreshButton("PatientPanel");   
            }
        }
        add(contentPanel, BorderLayout.CENTER);
        headerPanel.revalidate();
        headerPanel.repaint();
        revalidate();
        repaint();
    }



//Bệnh nhân
    private void showPatientPopupMenu(MouseEvent e) {
        int row = patientTable.rowAtPoint(e.getPoint());
        if (row >= 0 && row < patientTable.getRowCount()) {
            patientTable.setRowSelectionInterval(row, row); // Chọn hàng tương ứng
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem detailItem = new JMenuItem("Xem chi tiết");
            detailItem.addActionListener(event -> openPatientDetailFrame(row));

            popupMenu.add(detailItem);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    // Mở Frame chi tiết thông tin bệnh nhân
    private void openPatientDetailFrame(int selectedRow) {
        JFrame detailFrame = new JFrame("Chi tiết bệnh nhân");
        detailFrame.setSize(500, 600);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setLayout(new BorderLayout(10, 10));

        // Header Panel
        JLabel headerLabel = new JLabel("THÔNG TIN CHI TIẾT BỆNH NHÂN", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 102, 204));
        detailFrame.add(headerLabel, BorderLayout.NORTH);

        // Content Panel (Đổi tên thành contentPatientPanel)
        JPanel contentPatientPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        contentPatientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                "Thông tin chi tiết", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(0, 102, 204)));
        contentPatientPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã bệnh nhân", "Họ tên", "Tên bệnh án", "Ngày khám", "Ngày sinh",
                                "Địa chỉ", "Giới tính", "Số điện thoại", "Quốc tịch", "Mã khoa khám"};

        // Hiển thị thông tin bệnh nhân
        for (int i = 0; i < columnNames.length; i++) {
            JLabel labelTitle = new JLabel(columnNames[i] + ": ");
            labelTitle.setFont(new Font("Arial", Font.BOLD, 14));
            labelTitle.setForeground(Color.BLACK);

            JLabel labelValue = new JLabel(patientTableModel.getValueAt(selectedRow, i).toString());
            labelValue.setFont(new Font("Arial", Font.PLAIN, 14));
            labelValue.setForeground(new Color(51, 51, 51));

            JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fieldPanel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
            fieldPanel.setBackground(Color.WHITE);
            fieldPanel.add(labelValue);

            contentPatientPanel.add(labelTitle);
            contentPatientPanel.add(fieldPanel);
        }

        // Checkbox cho "Nhập viện" (Lấy dữ liệu từ bảng)
        JLabel nhapVienLabel = new JLabel("Nhập viện: ");
        nhapVienLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nhapVienLabel.setForeground(Color.BLACK);

        JCheckBox nhapVienCheckBox = new JCheckBox();
        Object value = patientTableModel.getValueAt(selectedRow, columnNames.length); // Cột "Nhập viện"

        // Kiểm tra giá trị là 1 hay 0
        if (value != null && value.toString().equals("1")) {
            nhapVienCheckBox.setSelected(true);
        } else {
            nhapVienCheckBox.setSelected(false);
        }

        nhapVienCheckBox.setBackground(Color.WHITE);
        nhapVienCheckBox.setFocusPainted(false);
        nhapVienCheckBox.setEnabled(false); // Không cho chỉnh sửa Checkbox

        contentPatientPanel.add(nhapVienLabel);
        contentPatientPanel.add(nhapVienCheckBox);

        detailFrame.add(contentPatientPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(220, 53, 69));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> detailFrame.dispose());
        footerPanel.add(closeButton);
        detailFrame.add(footerPanel, BorderLayout.SOUTH);

        detailFrame.getContentPane().setBackground(Color.WHITE);
        detailFrame.setVisible(true);
    }

//Bác sĩ
    private void showDoctorPopupMenu(MouseEvent e) {
        int row = doctorTable.rowAtPoint(e.getPoint());
        if (row >= 0 && row < doctorTable.getRowCount()) {
            doctorTable.setRowSelectionInterval(row, row); // Chọn hàng
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem detailItem = new JMenuItem("Xem chi tiết");
            detailItem.addActionListener(event -> openDoctorDetailFrame(row));

            popupMenu.add(detailItem);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void openDoctorDetailFrame(int selectedRow) {
        JFrame detailFrame = new JFrame("Chi tiết bác sĩ");
        detailFrame.setSize(500, 600);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setLayout(new BorderLayout(10, 10));

        // Header Panel
        JLabel headerLabel = new JLabel("THÔNG TIN CHI TIẾT BÁC SĨ", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 102, 204));
        detailFrame.add(headerLabel, BorderLayout.NORTH);

        // Content Panel (contentDoctorPanel)
        JPanel contentDoctorPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        contentDoctorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                "Thông tin chi tiết", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(0, 102, 204)));
        contentDoctorPanel.setBackground(Color.WHITE);

        String[] columnNames = {"ID", "Họ tên", "Ngày sinh", "Địa chỉ", "Giới tính",
                                "Số điện thoại", "Quốc tịch", "Mã chuyên khoa",
                                "Chức vụ", "Ngày vào làm việc", "Lương cơ bản", "Hệ số lương"};

        // Sử dụng DecimalFormat để định dạng lại Lương cơ bản
        DecimalFormat salaryFormat = new DecimalFormat("#,###,###");

        // Hiển thị thông tin bác sĩ với các khung đẹp
        for (int i = 0; i < columnNames.length; i++) {
            JLabel labelTitle = new JLabel(columnNames[i] + ": ");
            labelTitle.setFont(new Font("Arial", Font.BOLD, 14));
            labelTitle.setForeground(Color.BLACK);

            String value = doctorTableModel.getValueAt(selectedRow, i).toString();

            // Kiểm tra nếu là Lương cơ bản, sử dụng DecimalFormat
            if (i == 10) {  // Lương cơ bản ở vị trí thứ 11 (index = 10)
                try {
                    // Chuyển giá trị lương cơ bản sang số và định dạng
                    double salary = Double.parseDouble(value);
                    value = salaryFormat.format(salary);
                } catch (NumberFormatException e) {
                    value = "Không xác định";  // Nếu không thể chuyển đổi, hiển thị thông báo
                }
            }

            JLabel labelValue = new JLabel(value);
            labelValue.setFont(new Font("Arial", Font.PLAIN, 14));
            labelValue.setForeground(new Color(51, 51, 51));

            JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fieldPanel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
            fieldPanel.setBackground(Color.WHITE);
            fieldPanel.add(labelValue);

            contentDoctorPanel.add(labelTitle);
            contentDoctorPanel.add(fieldPanel);
        }

        // Footer Panel
        JPanel footerPanel = new JPanel();
        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(220, 53, 69));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> detailFrame.dispose());
        footerPanel.add(closeButton);

        detailFrame.add(contentDoctorPanel, BorderLayout.CENTER);
        detailFrame.add(footerPanel, BorderLayout.SOUTH);

        detailFrame.getContentPane().setBackground(Color.WHITE);
        detailFrame.setVisible(true);
    }
    
    
    
    
    //  CÁC PHƯƠNG THỨC THAO TÁC TRÊN DANH SÁCH BÁC SĨ
    private void addDoctor() {
        JTextField idField = new JTextField();
        JTextField hoTenField = new JTextField();
        JDateChooser ngaySinhField = new JDateChooser();
        JTextField diaChiField = new JTextField();
        JComboBox<String> gioiTinhCombo = new JComboBox<>(new String[]{"nam", "nữ"});
        JTextField sdtField = new JTextField();
        JTextField quocTichField = new JTextField();
        JTextField maChuyenKhoaField = new JTextField();
        JComboBox<String> chucVuCombo = new JComboBox<>(new String[]{"Trưởng Khoa", "Phó Khoa", "Bác Sĩ", "Thực Tập Sinh"});
        JDateChooser ngayVaoLamViecField = new JDateChooser();
        JTextField luongCoBanField = new JTextField();
        JTextField heSoLuongField = new JTextField();

        JComponent[] inputs = new JComponent[] {
                new JLabel("ID"), idField,
                new JLabel("Họ tên"), hoTenField,
                new JLabel("Ngày sinh (yyyy-MM-dd)"), ngaySinhField,
                new JLabel("Địa chỉ"), diaChiField,
                new JLabel("Giới tính"), gioiTinhCombo,
                new JLabel("Số điện thoại"), sdtField,
                new JLabel("Quốc tịch"), quocTichField,
                new JLabel("Mã chuyên khoa"), maChuyenKhoaField,
                new JLabel("Chức vụ"), chucVuCombo,
                new JLabel("Ngày vào làm việc (yyyy-MM-dd)"), ngayVaoLamViecField,
                new JLabel("Lương cơ bản"), luongCoBanField,
                new JLabel("Hệ số lương"), heSoLuongField
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Thêm Bác sĩ", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Kiểm tra định dạng số điện thoại
                String sdt = sdtField.getText();
                if (!isValidPhoneNumber(sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
                    return; // Nếu số điện thoại không hợp lệ, không tiếp tục thực hiện
                }

                Connection conn = DBConnection.getConnection();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                BacSi bacSi = new BacSi(
                        idField.getText(),
                        hoTenField.getText(),
                        dateFormat.format(ngaySinhField.getDate()),
                        diaChiField.getText(),
                        gioiTinhCombo.getSelectedItem().toString(),
                        sdt,
                        quocTichField.getText(),
                        new Khoa(maChuyenKhoaField.getText(), ""),
                        chucVuCombo.getSelectedItem().toString(),
                        dateFormat.format(ngayVaoLamViecField.getDate()),
                        Float.parseFloat(luongCoBanField.getText()),
                        Float.parseFloat(heSoLuongField.getText())
                );
                DBUtils.insertBacSi(conn, bacSi);
                refreshDoctorTable();
                JOptionPane.showMessageDialog(this, "Thêm bác sĩ thành công!");
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Lương cơ bản và hệ số lương phải là số!");
            }
        }
    }
    
    private void editDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) doctorTable.getValueAt(selectedRow, 0);
            try {
                Connection conn = DBConnection.getConnection();
                BacSi bacSi = DBUtils.findBacSi(conn, id);
                if (bacSi != null) {
                    JTextField hoTenField = new JTextField(bacSi.getHoTen());
                    JTextField ngaySinhField = new JTextField(bacSi.getNgaySinh());
                    JTextField diaChiField = new JTextField(bacSi.getDiaChi());
                    JComboBox<String> gioiTinhCombo = new JComboBox<>(new String[]{"nam", "nữ"});
                    gioiTinhCombo.setSelectedItem(bacSi.getGioiTinh());
                    JTextField sdtField = new JTextField(bacSi.getSdt());
                    JTextField quocTichField = new JTextField(bacSi.getQuocTich());
                    JTextField maChuyenKhoaField = new JTextField(bacSi.getChuyenKhoa().getMaKhoa());
                    JComboBox<String> chucVuCombo = new JComboBox<>(new String[]{"Trưởng Khoa", "Phó Khoa", "Bác Sĩ", "Thực Tập Sinh"});
                    chucVuCombo.setSelectedItem(bacSi.getChucVu());
                    JTextField ngayVaoLamViecField = new JTextField(bacSi.getNgayVaoLamViec());

                    // Định dạng số lương cơ bản để hiển thị đầy đủ số 0
                    DecimalFormat salaryFormat = new DecimalFormat("#,###,###");
                    String luongCoBanFormatted = salaryFormat.format(bacSi.getLuongCoBan());

                    JTextField luongCoBanField = new JTextField(luongCoBanFormatted);
                    JTextField heSoLuongField = new JTextField(String.valueOf(bacSi.getHeSoLuong()));

                    JComponent[] inputs = new JComponent[] {
                            new JLabel("Họ tên"), hoTenField,
                            new JLabel("Ngày sinh (yyyy-MM-dd)"), ngaySinhField,
                            new JLabel("Địa chỉ"), diaChiField,
                            new JLabel("Giới tính"), gioiTinhCombo,
                            new JLabel("SĐT"), sdtField,
                            new JLabel("Quốc tịch"), quocTichField,
                            new JLabel("Mã chuyên khoa"), maChuyenKhoaField,
                            new JLabel("Chức vụ"), chucVuCombo,
                            new JLabel("Ngày vào làm việc (yyyy-MM-dd)"), ngayVaoLamViecField,
                            new JLabel("Lương cơ bản"), luongCoBanField,
                            new JLabel("Hệ số lương"), heSoLuongField
                    };

                    int result = JOptionPane.showConfirmDialog(null, inputs, "Sửa Bác sĩ", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // Kiểm tra định dạng số điện thoại
                        String sdt = sdtField.getText();
                        if (!isValidPhoneNumber(sdt)) {
                            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
                            return; // Nếu số điện thoại không hợp lệ, không tiếp tục thực hiện
                        }

                        // Cập nhật các thông tin bác sĩ
                        bacSi.setHoTen(hoTenField.getText());
                        bacSi.setNgaySinh(ngaySinhField.getText());
                        bacSi.setDiaChi(diaChiField.getText());
                        bacSi.setGioiTinh(gioiTinhCombo.getSelectedItem().toString());
                        bacSi.setSdt(sdt);
                        bacSi.setQuocTich(quocTichField.getText());
                        bacSi.setChuyenKhoa(new Khoa(maChuyenKhoaField.getText(), ""));
                        bacSi.setChucVu(chucVuCombo.getSelectedItem().toString());
                        bacSi.setNgayVaoLamViec(ngayVaoLamViecField.getText());
                        bacSi.setLuongCoBan(Float.parseFloat(luongCoBanField.getText().replace(",", ""))); // Loại bỏ dấu phẩy khi lưu vào cơ sở dữ liệu
                        bacSi.setHeSoLuong(Float.parseFloat(heSoLuongField.getText()));

                        DBUtils.updateBacSi(conn, id, bacSi);
                        refreshDoctorTable();
                        JOptionPane.showMessageDialog(this, "Cập nhật bác sĩ thành công!");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Lương cơ bản và hệ số lương phải là số!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bác sĩ cần sửa.");
        }
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) doctorTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bác sĩ này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DBConnection.getConnection();
                    DBUtils.deleteBacSi(conn, id);
                    refreshDoctorTable();
                    JOptionPane.showMessageDialog(this, "Xóa bác sĩ thành công!");
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bác sĩ cần xóa.");
        }
    }

    private void sortDoctors() {
        String[] options = {"Lương tăng dần", "Lương giảm dần"};
        int choice = JOptionPane.showOptionDialog(this, "Chọn cách sắp xếp:", "Sắp xếp bác sĩ",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        try {
            Connection conn = DBConnection.getConnection();
            List<BacSi> sortedList;
            if (choice == 0) {
                sortedList = DBUtils.sortbyluongBacSi(conn, "ASC");
            } else {
                sortedList = DBUtils.sortbyluongBacSi(conn, "DESC");
            }
            updateDoctorTable(sortedList);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

   private void filterDoctors() {
        String[] options = {"Chức vụ", "Chuyên khoa"};
        int selectedOption = JOptionPane.showOptionDialog(this, "Chọn tiêu chí lọc", "Lọc bác sĩ",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (selectedOption == 0) {  // Lọc theo chức vụ
            String[] chucVuOptions = {"Trưởng Khoa", "Phó Khoa", "Bác Sĩ", "Thực Tập Sinh"};
            JComboBox<String> chucVuCombo = new JComboBox<>(chucVuOptions);

            int result = JOptionPane.showConfirmDialog(this, chucVuCombo, "Chọn chức vụ", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String chucVu = (String) chucVuCombo.getSelectedItem();
                try {
                    Connection conn = DBConnection.getConnection();
                    List<BacSi> filteredList = DBUtils.listChucvu(conn, chucVu);
                    updateDoctorTable(filteredList);
                    // Hiển thị nút Trở lại

                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        } else if (selectedOption == 1) {  // Lọc theo chuyên khoa
            String[] chuyenKhoaOptions = {"Khoa Nội", "Khoa Ngoại", "Khoa Phụ Sản", "Khoa Nhi", "Khoa Truyền Nhiễm", "Khoa Cấp Cứu", "Khoa Hồi Sức Tích Cực Và Chống Độc", "Khoa Y Học Cổ Truyền", "Khoa Vật Lý Trị Liệu - Phục Hồi Chức Năng", "Khoa Ung Bướu", "Khoa Y Học Hạt Nhân", "Khoa Phẫu Thuật - Gây Mê Hồi Sức", "Khoa Chẩn Đoán Hình Ảnh", "Khoa Xét Nghiệm", "Khoa Giải Phẫu Bệnh", "Khoa Lọc Máu", "Khoa Nội Soi", "Khoa Thăm Dò Chức Năng", "Khoa Dược", "Khoa Dinh Dưỡng"};
            JComboBox<String> chuyenKhoaCombo = new JComboBox<>(chuyenKhoaOptions);

            int result = JOptionPane.showConfirmDialog(this, chuyenKhoaCombo, "Chọn chuyên khoa", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String tenChuyenKhoa = (String) chuyenKhoaCombo.getSelectedItem();
                try {
                    Connection conn = DBConnection.getConnection();
                    List<BacSi> filteredList = DBUtils.listtenChuyenKhoa(conn, tenChuyenKhoa);
                    updateDoctorTable(filteredList);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        }
    }
    private void searchDoctors(String searchText) {
        try {
            Connection conn = DBConnection.getConnection();
            List<BacSi> allDoctors = DBUtils.listBacSi(conn);
            List<BacSi> filteredDoctors = allDoctors.stream()
                    .filter(doctor ->
                            doctor.getHoTen().toLowerCase().contains(searchText.toLowerCase()) ||
                                    doctor.getID().toLowerCase().contains(searchText.toLowerCase()) ||
                                    doctor.getChucVu().toLowerCase().contains(searchText.toLowerCase()) ||
                                    doctor.getChuyenKhoa().getTenKhoa().toLowerCase().contains(searchText.toLowerCase())
                    )
                    .collect(Collectors.toList());
            updateDoctorTable(filteredDoctors);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }

   
    // CÁC PHƯƠNG THỨC THAO TÁC TRÊN DANH SÁCH BỆNH NHÂN
    private void addPatient() {
        JTextField maBNField = new JTextField();
        JTextField hoTenField = new JTextField();
        JTextField tenBenhAnField = new JTextField();
        JDateChooser ngayKhamField = new JDateChooser();
        JDateChooser ngaySinhField = new JDateChooser();
        JTextField diaChiField = new JTextField();
        JComboBox<String> gioiTinhCombo = new JComboBox<>(new String[]{"nam", "nữ"});
        JTextField sdtField = new JTextField();
        JTextField quocTichField = new JTextField();
        JTextField maKhoaKhamField = new JTextField();
        JCheckBox nhapVienCheck = new JCheckBox();

        JComponent[] inputs = new JComponent[] {
                new JLabel("Mã bệnh nhân"), maBNField,
                new JLabel("Họ tên"), hoTenField,
                new JLabel("Tên bệnh án"), tenBenhAnField,
                new JLabel("Ngày khám (yyyy-MM-dd)"), ngayKhamField,
                new JLabel("Ngày sinh (yyyy-MM-dd)"), ngaySinhField,
                new JLabel("Địa chỉ"), diaChiField,
                new JLabel("Giới tính"), gioiTinhCombo,
                new JLabel("Số điện thoại"), sdtField,
                new JLabel("Quốc tịch"), quocTichField,
                new JLabel("Mã khoa khám"), maKhoaKhamField,
                new JLabel("Nhập viện"), nhapVienCheck
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Thêm Bệnh nhân", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Kiểm tra định dạng số điện thoại
                String sdt = sdtField.getText();
                if (!isValidPhoneNumber(sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
                    return; // Nếu số điện thoại không hợp lệ, không tiếp tục thực hiện
                }

                Connection conn = DBConnection.getConnection();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                BenhNhan benhNhan = new BenhNhan(
                        maBNField.getText(),
                        hoTenField.getText(),
                        tenBenhAnField.getText(),
                        dateFormat.format(ngayKhamField.getDate()),
                        dateFormat.format(ngaySinhField.getDate()),
                        diaChiField.getText(),
                        gioiTinhCombo.getSelectedItem().toString(),
                        sdt,
                        quocTichField.getText(),
                        new Khoa(maKhoaKhamField.getText(), ""),
                        nhapVienCheck.isSelected()
                );
                DBUtils.insertBenhNhan(conn, benhNhan);
                refreshPatientTable();
                JOptionPane.showMessageDialog(this, "Thêm bệnh nhân thành công!");
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    private void editPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String maBN = (String) patientTable.getValueAt(selectedRow, 0);
            try {
                Connection conn = DBConnection.getConnection();
                BenhNhan benhNhan = DBUtils.findBenhNhan(conn, maBN);
                if (benhNhan != null) {
                    JTextField hoTenField = new JTextField(benhNhan.getHoTen());
                    JTextField tenBenhAnField = new JTextField(benhNhan.getTenBenhAn());
                    JTextField ngayKhamField = new JTextField(benhNhan.getNgayKham());
                    JTextField ngaySinhField = new JTextField(benhNhan.getNgaySinh());
                    JTextField diaChiField = new JTextField(benhNhan.getDiaChi());
                    JComboBox<String> gioiTinhCombo = new JComboBox<>(new String[]{"nam", "nữ"});
                    gioiTinhCombo.setSelectedItem(benhNhan.getGioiTinh());
                    JTextField sdtField = new JTextField(benhNhan.getSdt());
                    JTextField quocTichField = new JTextField(benhNhan.getQuocTich());
                    JTextField maKhoaKhamField = new JTextField(benhNhan.getKhoaKham().getMaKhoa());
                    JCheckBox nhapVienCheck = new JCheckBox("", benhNhan.isNhapVien());

                    JComponent[] inputs = new JComponent[] {
                            new JLabel("Họ tên"), hoTenField,
                            new JLabel("Tên bệnh án"), tenBenhAnField,
                            new JLabel("Ngày khám (yyyy-MM-dd)"), ngayKhamField,
                            new JLabel("Ngày sinh (yyyy-MM-dd)"), ngaySinhField,
                            new JLabel("Địa chỉ"), diaChiField,
                            new JLabel("Giới tính"), gioiTinhCombo,
                            new JLabel("Số điện thoại"), sdtField,
                            new JLabel("Quốc tịch"), quocTichField,
                            new JLabel("Mã khoa khám"), maKhoaKhamField,
                            new JLabel("Nhập viện"), nhapVienCheck
                    };

                    int result = JOptionPane.showConfirmDialog(null, inputs, "Sửa Bệnh nhân", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // Kiểm tra định dạng số điện thoại
                        String sdt = sdtField.getText();
                        if (!isValidPhoneNumber(sdt)) {
                            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
                            return; // Nếu số điện thoại không hợp lệ, không tiếp tục thực hiện
                        }

                        benhNhan.setHoTen(hoTenField.getText());
                        benhNhan.setTenBenhAn(tenBenhAnField.getText());
                        benhNhan.setNgayKham(ngayKhamField.getText());
                        benhNhan.setNgaySinh(ngaySinhField.getText());
                        benhNhan.setDiaChi(diaChiField.getText());
                        benhNhan.setGioiTinh(gioiTinhCombo.getSelectedItem().toString());
                        benhNhan.setSdt(sdt);
                        benhNhan.setQuocTich(quocTichField.getText());
                        benhNhan.setKhoaKham(new Khoa(maKhoaKhamField.getText(), ""));
                        benhNhan.setNhapVien(nhapVienCheck.isSelected());

                        DBUtils.updateBenhNhan(conn, maBN, benhNhan);
                        refreshPatientTable();
                        JOptionPane.showMessageDialog(this, "Cập nhật bệnh nhân thành công!");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân cần sửa.");
        }
    }


    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String maBN = (String) patientTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bệnh nhân này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DBConnection.getConnection();
                    DBUtils.deleteBenhNhan(conn, maBN);
                    refreshPatientTable();
                    JOptionPane.showMessageDialog(this, "Xóa bệnh nhân thành công!");
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân cần xóa.");
        }
    }

    private void sortPatients() {
        JOptionPane.showMessageDialog(this, "Chức năng sắp xếp bệnh nhân chưa được triển khai.");
    }

   private void filterPatients() {
    String[] options = {"Nhập viện", "Không nhập viện", "Chọn khoa khám"};
    int choice = JOptionPane.showOptionDialog(this, "Chọn trạng thái lọc:", "Lọc bệnh nhân",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    try {
        Connection conn = DBConnection.getConnection();

        if (choice == 0 || choice == 1) {  // Lọc theo trạng thái nhập viện
            List<BenhNhan> filteredList = DBUtils.ListNhapvien(conn, choice == 0);
            updatePatientTable(filteredList);
        } else if (choice == 2) {  // Lọc theo khoa khám
            String[] chuyenKhoaOptions = {"Khoa Nội", "Khoa Ngoại", "Khoa Phụ Sản", "Khoa Nhi", "Khoa Truyền Nhiễm", "Khoa Cấp Cứu", "Khoa Hồi Sức Tích Cực Và Chống Độc", "Khoa Y Học Cổ Truyền", "Khoa Vật Lý Trị Liệu - Phục Hồi Chức Năng", "Khoa Ung Bướu", "Khoa Y Học Hạt Nhân", "Khoa Phẫu Thuật - Gây Mê Hồi Sức", "Khoa Chẩn Đoán Hình Ảnh", "Khoa Xét Nghiệm", "Khoa Giải Phẫu Bệnh", "Khoa Lọc Máu", "Khoa Nội Soi", "Khoa Thăm Dò Chức Năng", "Khoa Dược", "Khoa Dinh Dưỡng"};
            // Tạo JComboBox với các tên khoa
            JComboBox<String> khoaComboBox = new JComboBox<>(chuyenKhoaOptions);
            int result = JOptionPane.showConfirmDialog(this, khoaComboBox, "Chọn khoa khám", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String selectedKhoa = (String) khoaComboBox.getSelectedItem();
                if (selectedKhoa != null && !selectedKhoa.isEmpty()) {
                    List<BenhNhan> filteredList = DBUtils.listtenkhoakham(conn, selectedKhoa);
                    updatePatientTable(filteredList);
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một khoa khám!");
                        }
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void searchPatients(String searchText) {
        try {
            Connection conn = DBConnection.getConnection();
            List<BenhNhan> allPatients = DBUtils.listBenhNhan(conn);
            List<BenhNhan> filteredPatients = allPatients.stream()
                    .filter(patient ->
                            patient.getHoTen().toLowerCase().contains(searchText.toLowerCase()) ||
                                    patient.getMaBN().toLowerCase().contains(searchText.toLowerCase()) ||
                                    patient.getTenBenhAn().toLowerCase().contains(searchText.toLowerCase()) ||
                                    patient.getKhoaKham().getTenKhoa().toLowerCase().contains(searchText.toLowerCase())
                    )
                    .collect(Collectors.toList());
            updatePatientTable(filteredPatients);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }
   
//    Refresh button
    private void showRefreshButton(String currentPanel) {
        if ("DoctorPanel".equals(currentPanel) || "PatientPanel".equals(currentPanel)) {
            refreshButton = new JButton("Làm mới");

            // Thiết lập màu sắc và font
            refreshButton.setBackground(new Color(0, 102, 204)); // Màu nền xanh đậm
            refreshButton.setForeground(Color.WHITE);            // Màu chữ trắng
            refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            refreshButton.setFocusPainted(false);
            refreshButton.setBorderPainted(false);

            // Hiệu ứng hover
            refreshButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    refreshButton.setBackground(new Color(0, 153, 255)); // Màu hover xanh sáng
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    refreshButton.setBackground(new Color(0, 102, 204)); // Trở về màu gốc
                }
            });

            // Hành động khi click
            refreshButton.addActionListener(e -> {
                try {
                    Connection conn = DBConnection.getConnection();

                    if ("DoctorPanel".equals(currentPanel)) {
                        List<BacSi> allDoctors = DBUtils.listBacSi(conn);
                        updateDoctorTable(allDoctors);
                    } else if ("PatientPanel".equals(currentPanel)) {
                        List<BenhNhan> allPatients = DBUtils.listBenhNhan(conn);
                        updatePatientTable(allPatients);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            });

            // Thêm nút vào JPanel
            JPanel panel = new JPanel();
            panel.setBackground(new Color(230, 247, 255)); // Nền xanh pastel
            panel.add(refreshButton);

            // Thêm JPanel vào giao diện
            add(panel, BorderLayout.SOUTH);
            revalidate();
            repaint();
        }
    }

    private void removeRefreshButton() {
        if (refreshButton != null) {
            Container parent = refreshButton.getParent();
            if (parent != null) {
                remove(parent);
                refreshButton = null;
                revalidate();
                repaint();
            }
        }
    }


//    CUSTOM
    
    private void customizeTable(JTable table, Color headerColor) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(headerColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                return c;
            }
        });
    }
    
    
//  Khoa
    private void showKhoaList() {
        // Tạo Popup Menu cho "Các Khoa"
        JPopupMenu khoaMenu = new JPopupMenu();

        // Danh sách các khoa (Ví dụ)
        String[] khoaList = {"Khoa Nội (K01)", "Khoa Ngoại (K02)", "Khoa Phụ Sản (K03)", "Khoa Nhi (K04)",
                                "Khoa Truyền Nhiễm (K05)", "Khoa Cấp Cứu (K06)", "Khoa Hồi Sức Tích Cực Và Chống Độc (K07)",
                                "Khoa Y Học Cổ Truyền (K08)", "Khoa Vật Lý Trị Liệu - Phục Hồi Chức Năng (K09)",
                                "Khoa Ung Bướu (K10)", "Khoa Y Học Hạt Nhân (K11)", "Khoa Phẫu Thuật - Gây Mê Hồi Sức (K12)",
                                "Khoa Chẩn Đoán Hình Ảnh (K13)", "Khoa Xét Nghiệm (K14)", "Khoa Giải Phẫu Bệnh (K15)",
                                "Khoa Lọc Máu (K16)", "Khoa Nội Soi (K17)", "Khoa Thăm Dò Chức Năng (K18)",
                                "Khoa Dược (K19)", "Khoa Dinh Dưỡng (K20)"
                            };

        for (String khoa : khoaList) {
            JMenuItem khoaItem = new JMenuItem(khoa);
            khoaMenu.add(khoaItem);
        }

        // Hiển thị Popup Menu tại vị trí của nút "Các Khoa"
        khoaMenu.show(headerPanel, 361, 40);  // Đặt tọa độ của Popup Menu sao cho phù hợp
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy để kiểm tra số điện thoại (có thể điều chỉnh tùy theo yêu cầu)
        String regex = "^\\d{10}$"; // Kiểm tra số điện thoại có 10 chữ số (có thể thay đổi)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI1 frame = new UI1();
            frame.setVisible(true);
        });
    }
}
