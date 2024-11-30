/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop;


import java.util.Calendar;
/**
 *
 * @author homin
 */
public class Date {
    private int Day;
    private int Month;
    private int Year;

    public Date(int Day, int Month, int Year) {
        if (Day >=1 && Day <= 31){
            this.Day = Day;
        }
        else this.Day = 1;
        
        if (Month >=1 && Month <= 12){
            this.Month = Month;
        }
        else this.Month = 1;
        
        if (Year >= 0){
            this.Year = Year;
        }
        else Year = 1;  
    }
    
    //Chuyen doi sang kieu Date trong sqls
    public java.sql.Date toSqlDate() {
        // Xây dựng chuỗi ngày tháng theo định dạng yyyy-MM-dd
        String formattedDate = String.format("%04d-%02d-%02d", this.Year, this.Month, this.Day);
        // Chuyển đổi chuỗi thành java.sql.Date
        return java.sql.Date.valueOf(formattedDate);
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int Day) {
        this.Day = Day;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int Month) {
        this.Month = Month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int Year) {
        this.Year = Year;
    }
}
