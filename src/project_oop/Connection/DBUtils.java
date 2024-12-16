/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop.Connection;

import java.sql.*;
import java.util.*;
import java.time.*;
import project_oop.Structure.BacSi;
import project_oop.Structure.BenhNhan;
import project_oop.Structure.Khoa;

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
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            
            String makhoa = rs.getString("maChuyenKhoa");
            String tenkhoa = getTenKhoa(conn, makhoa);
            Khoa khoa = new Khoa(makhoa, tenkhoa);
            
            String chucVu = rs.getString("chucVu");
            String ngayVaoLamViec = rs.getString("ngayVaoLamViec");
            float luongCoBan = rs.getFloat("luongCoBan");
            float heSoLuong = rs.getFloat("heSoLuong");
            
            BacSi bs = new BacSi(ID, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
            
            listBS.add(bs);
        }
        return listBS;
    }
    
    
    
    public static void insertBacSi(Connection conn, BacSi bs) throws SQLException {
        String sql = "insert into BacSi(ID, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, maChuyenKhoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong) "
                   + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, bs.getID());
        pstm.setString(2, bs.getHoTen());
        pstm.setString(3, bs.getNgaySinh()); 
        pstm.setString(4, bs.getDiaChi());
        pstm.setString(5, bs.getGioiTinh());
        pstm.setString(6, bs.getSdt());
        pstm.setString(7, bs.getQuocTich());
        pstm.setString(8, bs.getChuyenKhoa().getMaKhoa());
        pstm.setString(9, bs.getChucVu());
        pstm.setString(10, bs.getNgayVaoLamViec()); 
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
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");      
            String makhoa = rs.getString("maChuyenKhoa");
            String tenkhoa = getTenKhoa(conn, makhoa);
            Khoa khoa = new Khoa(makhoa, tenkhoa);
            String chucVu = rs.getString("chucVu");
            String ngayVaoLamViec = rs.getString("ngayVaoLamViec");
            float luongCoBan = rs.getFloat("luongCoBan");
            float heSoLuong = rs.getFloat("heSoLuong");
            
            BacSi bs = new BacSi(id, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
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
            pstm.setString(2, bs.getNgaySinh());
            pstm.setString(3, bs.getDiaChi());
            pstm.setString(4, bs.getGioiTinh());
            pstm.setString(5, bs.getSdt());
            pstm.setString(6, bs.getQuocTich());
            pstm.setString(7, bs.getChuyenKhoa().getMaKhoa());
            pstm.setString(8, bs.getChucVu());
            pstm.setString(9, bs.getNgayVaoLamViec());
            pstm.setFloat(10, bs.getLuongCoBan());
            pstm.setFloat(11, bs.getHeSoLuong());
            pstm.setString(12, id);
            
            pstm.executeUpdate();
        }
    }
    
    public static List<BacSi> listChucvu(Connection conn, String chucVu) throws SQLException{
        String sql = "Select * from BacSi where chucVu= ?";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, chucVu);
        ResultSet rs = psmt.executeQuery();

        List<BacSi> listbyChucVu = new ArrayList<>();
        while (rs.next()){
            String ID = rs.getString("ID");
            String hoTen = rs.getString("hoTen");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            String maKhoa = rs.getString("maChuyenKhoa");
            String tenKhoa = getTenKhoa(conn, maKhoa);
            Khoa khoa = new Khoa(maKhoa, tenKhoa);
            String ngayVaoLamViec = rs.getString("ngayVaoLamViec");
            float luongCoBan = rs.getFloat("luongCoBan");
            float heSoLuong = rs.getFloat("heSoLuong");
            BacSi bs = new BacSi(ID, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
            listbyChucVu.add(bs);
        }
        return listbyChucVu;
    }
    
    public static List<BacSi> listtenChuyenKhoa(Connection conn, String tenKhoa) throws SQLException{
        String sql = "Select * from Bacsi where maChuyenKhoa= (select maKhoa from Khoa where tenKhoa = ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, tenKhoa);
        ResultSet rs = psmt.executeQuery();

        List<BacSi> listbytenKhoa = new ArrayList<>();
        while (rs.next()){
            String ID = rs.getString("ID");
            String hoTen = rs.getString("hoTen");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            String maKhoa = rs.getString("maChuyenKhoa");
            Khoa khoa = new Khoa(maKhoa, tenKhoa);
            String chucVu = rs.getString("chucVu");
            String ngayVaoLamViec = rs.getString("ngayVaoLamViec");
            float luongCoBan = rs.getFloat("luongCoBan");
            float heSoLuong = rs.getFloat("heSoLuong");
            BacSi bs = new BacSi(ID, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
            listbytenKhoa.add(bs);
        }
        return listbytenKhoa;
    }
    
    public static List<BacSi> sortbyluongBacSi(Connection conn, String sort) throws SQLException{
        String sql = "Select *, (luongCoBan * heSoLuong) as luong from BacSi order by luong " + sort;
        PreparedStatement psmt = conn.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery();
        //ASC nếu tăng dần, DESC nếu giảm dần
        List<BacSi> listsort = new ArrayList<>();
        while (rs.next()){
            String ID = rs.getString("ID");
            String hoTen = rs.getString("hoTen");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            String maKhoa = rs.getString("maChuyenKhoa");
            String tenKhoa = getTenKhoa(conn, maKhoa);
            Khoa khoa = new Khoa(maKhoa, tenKhoa);
            String chucVu = rs.getString("chucVu");
            String ngayVaoLamViec = rs.getString("ngayVaoLamViec");
            float luongCoBan = rs.getFloat("luongCoBan");
            float heSoLuong = rs.getFloat("heSoLuong");
            BacSi bs = new BacSi(ID, hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoa, chucVu, ngayVaoLamViec, luongCoBan, heSoLuong);
            listsort.add(bs);
        }
        return listsort;
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
            String ngayKham = rs.getString("ngayKham");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            
            String makhoakham = rs.getString("maKhoaKham");
            String tenkhoa = getTenKhoa(conn, makhoakham);
            Khoa khoakham = new Khoa(makhoakham, tenkhoa);

            boolean nhapvien = rs.getBoolean("nhapVien");
            
            BenhNhan bn = new BenhNhan(maBN, hoTen, tenbenhan, ngayKham, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoakham, nhapvien);
            
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
        pstm.setString(4, bn.getNgayKham());
        pstm.setString(5, bn.getNgaySinh());
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
            String ngayKham = rs.getString("ngayKham");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");         
            String makhoakham = rs.getString("maKhoaKham");
            String tenkhoa = getTenKhoa(conn, makhoakham);
            Khoa khoakham = new Khoa(makhoakham, tenkhoa);
            boolean nhapvien = rs.getBoolean("nhapVien");     
            BenhNhan bn = new BenhNhan(maBN, hoTen, tenbenhan, ngayKham, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoakham, nhapvien);
            
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
            pstm.setString(3, bn.getNgayKham());
            pstm.setString(4, bn.getNgaySinh());
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
    
    public static List<BenhNhan> listtenkhoakham(Connection conn, String tenKhoaKham) throws SQLException
    {
        String sql = "select * from BenhNhan where maKhoaKham = (select maKhoa from Khoa where tenKhoa = ?)";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, tenKhoaKham);
        ResultSet rs = psmt.executeQuery();
        List<BenhNhan> listbytenKhoa = new ArrayList<>();
        while (rs.next()){
            String maBN = rs.getString("maBN");
            String hoTen = rs.getString("hoTen");
            String tenBenhAn = rs.getString("tenBenhAn");
            String ngayKham = rs.getString("ngayKham");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            String makhoakham = rs.getString("maKhoaKham");
//            String tenkhoa = getTenKhoa(conn, makhoakham);
            Khoa khoakham = new Khoa(makhoakham, tenKhoaKham);
            boolean nhapvien = rs.getBoolean("nhapVien");
            
            BenhNhan bn = new BenhNhan(maBN, hoTen, tenBenhAn, ngayKham, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoakham, nhapvien);
            
            listbytenKhoa.add(bn);
        }
        return listbytenKhoa;
    }
    
    public static List<BenhNhan> ListNhapvien(Connection conn, boolean nhapVien) throws SQLException{
        String sql = "select * from BenhNhan where nhapVien= ?";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setBoolean(1, nhapVien);
        ResultSet rs = psmt.executeQuery();
        List<BenhNhan> listbyNhapVien = new ArrayList<>();
        while (rs.next()){
            String maBN = rs.getString("maBN");
            String hoTen = rs.getString("hoTen");
            String tenBenhAn = rs.getString("tenBenhAn");
            String ngayKham = rs.getString("ngayKham");
            String ngaySinh = rs.getString("ngaySinh");
            String diaChi = rs.getString("diaChi");
            String gioiTinh = rs.getString("gioiTinh");
            String sdt = rs.getString("sdt");
            String quocTich = rs.getString("quocTich");
            
            String makhoakham = rs.getString("maKhoaKham");
            String tenkhoa = getTenKhoa(conn, makhoakham);
            Khoa khoakham = new Khoa(makhoakham, tenkhoa);
//            boolean nhapvien = rs.getBoolean("nhapVien");
            
            BenhNhan bn = new BenhNhan(maBN, hoTen, tenBenhAn, ngayKham, ngaySinh, diaChi, gioiTinh, sdt, quocTich, khoakham, nhapVien);
            
            listbyNhapVien.add(bn);
        }
        return listbyNhapVien;
    }
}
