package chetu.felixpat.letsply.model;

import com.orm.SugarRecord;

/**
 * Created by Chetu on 16-02-2018.
 */

public class PlyDetails extends SugarRecord {

    String name;
    String billCount;
    String amount;
    String toBePaid;

    public PlyDetails() {
    }

    public PlyDetails(String name, String billCount, String amount, String toBePaid) {
        this.name = name;
        this.billCount = billCount;
        this.amount = amount;
        this.toBePaid = toBePaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBillCount() {
        return billCount;
    }

    public void setBillCount(String billCount) {
        this.billCount = billCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToBePaid() {
        return toBePaid;
    }

    public void setToBePaid(String toBePaid) {
        this.toBePaid = toBePaid;
    }
}
