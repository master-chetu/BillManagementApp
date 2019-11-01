package chetu.felixpat.letsply.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Chetu on 07-02-2018.
 */

public class BillingDetails  extends SugarRecord{

    String name;
    String billNo;
    String date;
    String amount;
    String amountPaid;
    String toBePaid;

    public BillingDetails() {
    }


    public BillingDetails(String name, String billNo, String date, String amount, String amountPaid, String toBePaid) {
        this.name = name;
        this.billNo = billNo;
        this.date = date;
        this.amount = amount;
        this.amountPaid = amountPaid;
        this.toBePaid = toBePaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getToBePaid() {
        return toBePaid;
    }

    public void setToBePaid(String toBePaid) {
        this.toBePaid = toBePaid;
    }


}