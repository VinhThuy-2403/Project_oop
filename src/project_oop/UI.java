/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop;
import project_oop.Structure.Khoa;
import project_oop.Structure.BenhNhan;
import project_oop.Structure.BacSi;
import project_oop.Connection.DBUtils;
import project_oop.Connection.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.*;
import java.sql.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

/**
 *  
 * @author homin
 */
public class UI extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel doctorPanel;
    private JPanel patientPanel;
    private JTable doctorTable;
    private JTable patientTable;
    private DefaultTableModel doctorTableModel;
    private DefaultTableModel patientTableModel;
//    private JDateChooser dateChooser;
//    private JComboBox<String> positionComboBox;

    public UI() {
        setTitle("Hospital Management System");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        doctorPanel = createDoctorPanel();
        patientPanel = createPatientPanel();

        tabbedPane.addTab("Bác sĩ", doctorPanel);
        tabbedPane.addTab("Bệnh nhân", patientPanel);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        topPanel.add(createDatePickerPanel());
//        topPanel.add(createPositionSelectorPanel());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);

        refreshDoctorTable();
        refreshPatientTable();
    }

//    private JPanel createDatePickerPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
//
//        JLabel dateLabel = new JLabel("Chọn ngày:");
//        dateChooser = new JDateChooser();
//        dateChooser.setDateFormatString("dd/MM/yyyy");
//        dateChooser.setPreferredSize(new Dimension(120, 30));
//
//        JButton selectButton = new JButton("Chọn");
//        selectButton.addActionListener(e -> handleDateSelection());
//
//        panel.add(dateLabel);
//        panel.add(dateChooser);
//        panel.add(selectButton);
//
//        return panel;
//    }
//
//    private JPanel createPositionSelectorPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
//
//        JLabel positionLabel = new JLabel("Chọn chức vụ:");
//        positionComboBox = new JComboBox<>(new String[]{"Tất cả", "Bác sĩ", "Y tá", "Điều dưỡng", "Kỹ thuật viên"});
//        positionComboBox.setPreferredSize(new Dimension(120, 30));
//
//        JButton filterButton = new JButton("Lọc");
//        filterButton.addActionListener(e -> handlePositionFilter());
//
//        panel.add(positionLabel);
//        panel.add(positionComboBox);
//        panel.add(filterButton);
//
//        return panel;
//    }
//
//    private void handleDateSelection() {
//        Date selectedDate = (Date) dateChooser.getDate();
//        if (selectedDate != null) {
//            // Here you can implement the logic to filter or process data based on the selected date
//            JOptionPane.showMessageDialog(this, "Ngày đã chọn: " + selectedDate);
//        } else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngày");
//        }
//    }
//
//    private void handlePositionFilter() {
//        String selectedPosition = (String) positionComboBox.getSelectedItem();
//        if (selectedPosition != null) {
//            if (selectedPosition.equals("Tất cả")) {
//                refreshDoctorTable();
//            } else {
//                filterDoctorsByPosition(selectedPosition);
//            }
//        }
//    }
//
//    private void filterDoctorsByPosition(String position) {
//        try {
//            Connection conn = DBConnection.getConnection();
//            List<BacSi> filteredList = DBUtils.listChucvu(conn, position);
//            updateDoctorTable(filteredList);
//        } catch (SQLException | ClassNotFoundException ex) {
//            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
//        }
//    }

    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        doctorTableModel = new DefaultTableModel(
                new Object[]{
                        "ID", "Họ tên", "Ngày sinh", "Địa chỉ", "Giới tính", "Số điện thoại", "Quốc tịch",
                        "Mã chuyên khoa", "Chức vụ", "ngày vào làm việc", "Lương cơ bản", "Hệ số lương"
                },
                0
        );
        doctorTable = new JTable(doctorTableModel);

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

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton sortButton = new JButton("Sắp xếp");
        JButton filterButton = new JButton("Lọc");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(filterButton);
        showBackButton();

        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addDoctor());
        editButton.addActionListener(e -> editDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        sortButton.addActionListener(e -> sortDoctors());
        filterButton.addActionListener(e -> filterDoctors());

        return panel;
    }

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
                new JLabel("SĐT"), sdtField,
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
                Connection conn = DBConnection.getConnection();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                BacSi bacSi = new BacSi(
                        idField.getText(),
                        hoTenField.getText(),
                        dateFormat.format(ngaySinhField.getDate()),
                        diaChiField.getText(),
                        gioiTinhCombo.getSelectedItem().toString(),
                        sdtField.getText(),
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
                    JTextField luongCoBanField = new JTextField(String.valueOf(bacSi.getLuongCoBan()));
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
                        bacSi.setHoTen(hoTenField.getText());
                        bacSi.setNgaySinh(ngaySinhField.getText());
                        bacSi.setDiaChi(diaChiField.getText());
                        bacSi.setGioiTinh(gioiTinhCombo.getSelectedItem().toString());
                        bacSi.setSdt(sdtField.getText());
                        bacSi.setQuocTich(quocTichField.getText());
                        bacSi.setChuyenKhoa(new Khoa(maChuyenKhoaField.getText(), ""));
                        bacSi.setChucVu(chucVuCombo.getSelectedItem().toString());
                        bacSi.setNgayVaoLamViec(ngayVaoLamViecField.getText());
                        bacSi.setLuongCoBan(Float.parseFloat(luongCoBanField.getText()));
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
                showBackButton();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    } else if (selectedOption == 1) {  // Lọc theo chuyên khoa
        String[] chuyenKhoaOptions = {"Khoa Nội", "Khoa Ngoại", "Khoa Tim Mạch", "Khoa Nhi"};
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

    
    private void showBackButton() {
        // Tạo nút Trở lại
        JButton backButton = new JButton("Làm mới");
        backButton.addActionListener(e -> {
            try {
                // Gọi lại bản hiển thị ban đầu
                Connection conn = DBConnection.getConnection();
                List<BacSi> allDoctors = DBUtils.listBacSi(conn);
                List<BenhNhan> allpats = DBUtils.listBenhNhan(conn);
                updateDoctorTable(allDoctors);
                updatePatientTable(allpats);
                // Ẩn nút Trở lại khi không cần thiết nữa
                getContentPane().remove(backButton);
                revalidate();
                repaint();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // Thêm nút vào giao diện
        JPanel panel = new JPanel();
        panel.add(backButton);
        add(panel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        patientTableModel = new DefaultTableModel(
                new Object[]{
                        "Mã bệnh nhân", "Họ tên", "Tên bệnh án", "ngày khám", "Ngày sinh",
                        "Địa chỉ", "Giới tính", "Số điện thoại", "Quốc tịch", "Mã khoa khám", "Nhập viện"
                },
                0
        );
        patientTable = new JTable(patientTableModel);

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
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton sortButton = new JButton("Sắp xếp");
        JButton filterButton = new JButton("Lọc");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(filterButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());
        sortButton.addActionListener(e -> sortPatients());
        filterButton.addActionListener(e -> filterPatients());

        return panel;
    }

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
                        sdtField.getText(),
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
                            new JLabel("SĐT"), sdtField,
                            new JLabel("Quốc tịch"), quocTichField,
                            new JLabel("Mã khoa khám"), maKhoaKhamField,
                            new JLabel("Nhập viện"), nhapVienCheck
                    };

                    int result = JOptionPane.showConfirmDialog(null, inputs, "Sửa Bệnh nhân", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        benhNhan.setHoTen(hoTenField.getText());
                        benhNhan.setTenBenhAn(tenBenhAnField.getText());
                        benhNhan.setNgayKham(ngayKhamField.getText());
                        benhNhan.setNgaySinh(ngaySinhField.getText());
                        benhNhan.setDiaChi(diaChiField.getText());
                        benhNhan.setGioiTinh(gioiTinhCombo.getSelectedItem().toString());
                        benhNhan.setSdt(sdtField.getText());
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
            String[] chuyenKhoaOptions = {"Khoa Nội", "Khoa Ngoại", "Khoa Tim Mạch", "Khoa Nhi"};
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UI().setVisible(true);
        });
    }
}


