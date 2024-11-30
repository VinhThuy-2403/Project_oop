/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;

/**
 *
 * @author Hp
 */
public class DBUtils {
    
    public static String getTenKhoa(Connection conn, String makhoa) throws SQLException, ClassNotFoundException
    {
        String sql = "Select * from Khoa where makhoa = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, makhoa);
        ResultSet rs = pstm.executeQuery();
        
        while(rs.next())
        {
            return rs.getString("tenkhoa");
        }
        return null;
    }
    
    public static List<BacSi> listBacSi (Connection conn) throws SQLException, ClassNotFoundException
    {
        String sql = "Select * from BacSi";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        List<BacSi> listBS = new ArrayList<BacSi>();
        
        while (rs.next())
        {
            String ID = rs.getString("ID");

            String hoTen = rs.getString("hoTen");
            
            java.sql.Date ngaySinh = rs.getDate("ngaySinh");
            LocalDate ngaySinhLocal = ngaySinh.toLocalDate();
            int ngay = ngaySinhLocal.getDayOfMonth(); 
            int thang = ngaySinhLocal.getMonthValue(); 
            int nam = ngaySinhLocal.getYear(); 

            Date ns = new Date(ngay, thang, nam);
            
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            
            String makhoa = rs.getString("maChuyenKhoa");
            String tenkhoa = getTenKhoa(conn, makhoa);
            Khoa khoa = new Khoa(makhoa, tenkhoa);
            
            String chucVu = rs.getString("chucVu");
            
            
            java.sql.Date ngayLamViec = rs.getDate("ngayVaoLamViec");
            LocalDate ngayLamViecLocal = ngayLamViec.toLocalDate();
            int ngayLV = ngayLamViecLocal.getDayOfMonth(); 
            int thangLV = ngayLamViecLocal.getMonthValue(); 
            int namLV = ngaySinhLocal.getYear(); 
            Date ngayVaoLamViec = new Date(ngayLV, thangLV, namLV);
            
            float luongCoBan = rs.getFloat("luongCoBan");
            float heSoLuong = rs.getFloat("heSoLuong");
            
            BacSi bs = new BacSi(ID, hoTen, ns, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
            
            listBS.add(bs);
        }
        return listBS;
    }
    
    
    
    public static void insertBacSi(Connection conn, BacSi bs) throws SQLException, ClassNotFoundException
    {
        String sql = "Insert BacSi(ID, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, maChuyenKhoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong)"
                + "values (?,?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        pstm.setString(1, bs.getID());
        pstm.setString(2, bs.getHoTen());
        pstm.setDate(3, bs.getNgaySinh().toSqlDate()); // Chuyển đổi sang java.sql.Date
        pstm.setString(4, bs.getDiaChi());
        pstm.setString(5, bs.getGioiTinh());
        pstm.setString(6, bs.getSdt());
        pstm.setString(7, bs.getQuocTich());
        pstm.setString(8, bs.getChuyenKhoa().getMaKhoa());
        pstm.setString(9, bs.getChucVu());
        pstm.setDate(10, bs.getNgayVaoLamViec().toSqlDate()); // Chuyển đổi sang java.sql.Date
        pstm.setFloat(11, bs.getLuongCoBan());
        pstm.setFloat(12, bs.getHeSoLuong());
        
        pstm.executeUpdate();
    }
}
