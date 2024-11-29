/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop;

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
