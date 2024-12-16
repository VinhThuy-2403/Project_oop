/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop.Structure;

/**
 *
 * @author Hp
 */
public class BacSi extends Person{
    private String ID;
    private Khoa ChuyenKhoa;
    private String chucVu;
    private String ngayVaoLamViec;
    private float luongCoBan;
    private float heSoLuong;

    public BacSi(String ID, String hoTen, String ngaySinh, String diaChi, String gioiTinh, String sdt, String quocTich, Khoa ChuyenKhoa, String chucVu, String ngayVaoLamViec, float luongCoBan, float heSoLuong) {
        super(hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich);
        this.ID = ID;
        this.ChuyenKhoa = ChuyenKhoa;
        this.chucVu = chucVu;
        this.ngayVaoLamViec = ngayVaoLamViec;
        this.luongCoBan = luongCoBan;
        this.heSoLuong = heSoLuong;
    }

    public BacSi() {
        
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Khoa getChuyenKhoa() {
        return ChuyenKhoa;
    }

    public void setChuyenKhoa(Khoa ChuyenKhoa) {
        this.ChuyenKhoa = ChuyenKhoa;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getNgayVaoLamViec() {
        return ngayVaoLamViec;
    }

    public void setNgayVaoLamViec(String ngayVaoLamViec) {
        this.ngayVaoLamViec = ngayVaoLamViec;
    }

    public float getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(float luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public float getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(float heSoLuong) {
        this.heSoLuong = heSoLuong;
    }
}
