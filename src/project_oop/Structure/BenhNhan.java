/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop.Structure;

/**
 *
 * @author Hp
 */
public class BenhNhan extends Person{
    private String maBN;
    private String tenBenhAn;
    private String ngayKham;
    private Khoa khoaKham;
    private boolean nhapVien;

    public BenhNhan(String maBN, String hoTen, String tenBenhAn, String ngayKham, String ngaySinh, String diaChi, String gioiTinh, String sdt, String quocTich, Khoa khoaKham, boolean nhapVien) {
        super(hoTen, ngaySinh, diaChi, gioiTinh, sdt, quocTich);
        this.maBN = maBN;
        this.tenBenhAn = tenBenhAn;
        this.ngayKham = ngayKham;
        this.khoaKham = khoaKham;
        this.nhapVien = nhapVien;
    }

    public BenhNhan() {
    }

    public String getMaBN() {
        return maBN;
    }

    public void setMaBN(String maBN) {
        this.maBN = maBN;
    }

    public String getTenBenhAn() {
        return tenBenhAn;
    }

    public void setTenBenhAn(String tenBenhAn) {
        this.tenBenhAn = tenBenhAn;
    }

    public String getNgayKham() {
        return ngayKham;
    }

    public void setNgayKham(String ngayKham) {
        this.ngayKham = ngayKham;
    }

    public Khoa getKhoaKham() {
        return khoaKham;
    }

    public void setKhoaKham(Khoa khoaKham) {
        this.khoaKham = khoaKham;
    }

    public boolean isNhapVien() {
        return nhapVien;
    }

    public void setNhapVien(boolean nhapVien) {
        this.nhapVien = nhapVien;
    }
}
