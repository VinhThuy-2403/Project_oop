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
    
    public static String getTenKhoa(Connection conn, String makhoa) throws SQLException
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
    
    public static List<BacSi> listBacSi (Connection conn) throws SQLException
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
    
    
    
    public static void insertBacSi(Connection conn, BacSi bs) throws SQLException
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
    
    public static void deleteBacSi(Connection conn, String id) throws SQLException
    {
        String sql = "Delete from BacSi where ID = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        pstm.setString(1, id);
        
        pstm.executeUpdate();
    }
    
    public static BacSi findBacSi(Connection conn, String id) throws SQLException
    {
        String sql = "Select * from BacSi where ID = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        
        while (rs.next())
        {   
            

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
            
            BacSi bs = new BacSi(id, hoTen, ns, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
            return bs;
        }
        return null;
    }
    
    public static void updateBacSi(Connection conn, String id, BacSi bs) throws SQLException
    {
        BacSi oldbs = findBacSi(conn, id);
        if (oldbs == null)
        {
            insertBacSi(conn, bs);
        }
        else
        {
            String sql = "Update BacSi set hoTen = ?, ngaySinh = ?, diaChi = ?, gioiTinh = ?, sdt=?,"
                    + "quocTich = ?, maChuyenKhoa = ?, chucVu = ?, ngayVaoLamViec = ?, luongCoBan = ?, "
                    + "heSoLuong = ? where ID = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, bs.getHoTen());
            pstm.setDate(2, bs.getNgaySinh().toSqlDate());
            pstm.setString(3, bs.getDiaChi());
            pstm.setString(4, bs.getGioiTinh());
            pstm.setString(5, bs.getSdt());
            pstm.setString(6, bs.getQuocTich());
            pstm.setString(7, bs.getChuyenKhoa().getMaKhoa());
            pstm.setString(8, bs.getChucVu());
            pstm.setDate(9, bs.getNgayVaoLamViec().toSqlDate()); // Chuyển đổi sang java.sql.Date
            pstm.setFloat(10, bs.getLuongCoBan());
            pstm.setFloat(11, bs.getHeSoLuong());
            pstm.setString(12, id);
            
            pstm.executeUpdate();
        }
    }
    
    
    
    //////////////////////////////////
    //////Benh Nhan///////////////////
    /////////////////////////////////
    public static List<BenhNhan> listBenhNhan (Connection conn) throws SQLException
    {
        String sql = "Select * from BenhNhan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        List<BenhNhan> listBN = new ArrayList<BenhNhan>();
        
        while (rs.next())
        {
            String maBN = rs.getString("maBN");
            String hoTen = rs.getString("hoTen");
            String tenbenhan = rs.getString("tenBenhAn");
            
            java.sql.Date ngaykham = rs.getDate("ngayKham");
            LocalDate ngayk = ngaykham.toLocalDate();
            int ngaykh = ngayk.getDayOfMonth(); 
            int thangkh = ngayk.getMonthValue(); 
            int namkh = ngayk.getYear(); 

            Date nk = new Date(ngaykh, thangkh, namkh);
            
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
            
            String makhoakham = rs.getString("maKhoaKham");
            String tenkhoa = getTenKhoa(conn, makhoakham);
            Khoa khoakham = new Khoa(makhoakham, tenkhoa);

            boolean nhapvien = rs.getBoolean("nhapVien");
            
            BenhNhan bn = new BenhNhan(maBN, hoTen, tenbenhan, nk, ns, diaChi, gioiTinh, sdt, quocTich, khoakham, nhapvien);
            
            listBN.add(bn);
            
        }
        return listBN;
    }
    
    
    public static void insertBenhNhan(Connection conn, BenhNhan bn) throws SQLException
    {
        String sql = "INSERT INTO BenhNhan (maBN, hoTen, tenBenhAn, ngayKham, ngaySinh, diaChi, gioiTinh, sdt, quocTich, maKhoaKham, nhapVien)"
                + "values (?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        pstm.setString(1, bn.getMaBN());                        
        pstm.setString(2, bn.getHoTen());                        
        pstm.setString(3, bn.getTenBenhAn());
        pstm.setDate(4, bn.getNgayKham().toSqlDate());
        pstm.setDate(5, bn.getNgaySinh().toSqlDate());
        pstm.setString(6, bn.getDiaChi());
        pstm.setString(7, bn.getGioiTinh());
        pstm.setString(8, bn.getSdt());
        pstm.setString(9, bn.getQuocTich());
        pstm.setString(10, bn.getKhoaKham().getMaKhoa());
        pstm.setBoolean(11, bn.isNhapVien());
        
        pstm.executeUpdate();
    }
    
    
    public static void deleteBenhNhan(Connection conn, String maBN) throws SQLException
    {
        String sql = "Delete from BenhNhan where maBN = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        pstm.setString(1, maBN);
        
        pstm.executeUpdate();
    }
    
    public static BenhNhan findBenhNhan(Connection conn, String maBN) throws SQLException
    {
        String sql = "Select * from BenhNhan where maBN = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, maBN);
        ResultSet rs = pstm.executeQuery();
        
        while (rs.next())
        {   
            
            String hoTen = rs.getString("hoTen");
            String tenbenhan = rs.getString("tenBenhAn");
            
            java.sql.Date ngaykham = rs.getDate("ngayKham");
            LocalDate ngayk = ngaykham.toLocalDate();
            int ngaykh = ngayk.getDayOfMonth(); 
            int thangkh = ngayk.getMonthValue(); 
            int namkh = ngayk.getYear(); 

            Date nk = new Date(ngaykh, thangkh, namkh);
            
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
            
            String makhoakham = rs.getString("maKhoaKham");
            String tenkhoa = getTenKhoa(conn, makhoakham);
            Khoa khoakham = new Khoa(makhoakham, tenkhoa);

            boolean nhapvien = rs.getBoolean("nhapVien");
            
            BenhNhan bn = new BenhNhan(maBN, hoTen, tenbenhan, nk, ns, diaChi, gioiTinh, sdt, quocTich, khoakham, nhapvien);
            
            return bn;

        }
        return null;
    }
    
    public static void updateBenhNhan(Connection conn, String maBN, BenhNhan bn) throws SQLException
    {
        BenhNhan oldbn = findBenhNhan(conn, maBN);
        if (oldbn == null)
        {
            insertBenhNhan(conn, bn);
        }
        else
        {
            String sql = "UPDATE BenhNhan SET hoTen = ?, tenBenhAn = ?, ngayKham = ?, ngaySinh = ?, diaChi = ?, "
                + "gioiTinh = ?, sdt = ?, quocTich = ?, maKhoaKham = ?, nhapVien = ? WHERE maBN = ?";

            PreparedStatement pstm = conn.prepareStatement(sql);
            
                                   
            pstm.setString(1, bn.getHoTen());                        
            pstm.setString(2, bn.getTenBenhAn());
            pstm.setDate(3, bn.getNgayKham().toSqlDate());
            pstm.setDate(4, bn.getNgaySinh().toSqlDate());
            pstm.setString(5, bn.getDiaChi());
            pstm.setString(6, bn.getGioiTinh());
            pstm.setString(7, bn.getSdt());
            pstm.setString(8, bn.getQuocTich());
            pstm.setString(9, bn.getKhoaKham().getMaKhoa());
            pstm.setBoolean(10, bn.isNhapVien());
            pstm.setString(11, bn.getMaBN()); 
            
            pstm.executeUpdate();
        }
    }
    
}
