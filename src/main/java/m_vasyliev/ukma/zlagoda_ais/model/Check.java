package m_vasyliev.ukma.zlagoda_ais.model;

import java.sql.Timestamp;

public class Check {
    private String checkNumber;
    private String employeeId;
    private String cardNumber;
    private Timestamp printDate;
    private double sumTotal;
    private double vat;

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Timestamp getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Timestamp printDate) {
        this.printDate = printDate;
    }

    public double getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(Double sumTotal) {
        this.sumTotal = sumTotal;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
}
