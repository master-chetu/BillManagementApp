package chetu.felixpat.letsply.model;

import java.util.List;

/**
 * Created by Chetu on 07-02-2018.
 */

public class BillingModelList {

    List<BillingDetails> billingDetailsList;

    public List<BillingDetails> getBillingDetailsList(){return billingDetailsList;}
    public void setBillingDetailsList(List<BillingDetails> billingDetailsList) {
        this.billingDetailsList = billingDetailsList;
    }
}
