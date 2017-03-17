package com.omega.demo.api.bean;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.math.BigDecimal;

/**
 * Created by jackychenb on 17/03/2017.
 */
public class OrderListModel extends ListModel {

    private String userId;
    private String number;
    private BigDecimal amountStart;
    private BigDecimal amountEnd;
    private BigDecimal gmtCreatedStart;
    private BigDecimal gmtCreatedEnd;
    private String itemNo;
    private String itemName;

    public OrderListModel() {
        this(40);
    }

    public OrderListModel(int pageSize) {
        super(pageSize);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmountStart() {
        return amountStart;
    }

    public void setAmountStart(BigDecimal amountStart) {
        this.amountStart = amountStart;
    }

    public BigDecimal getAmountEnd() {
        return amountEnd;
    }

    public void setAmountEnd(BigDecimal amountEnd) {
        this.amountEnd = amountEnd;
    }

    public BigDecimal getGmtCreatedStart() {
        return gmtCreatedStart;
    }

    public void setGmtCreatedStart(BigDecimal gmtCreatedStart) {
        this.gmtCreatedStart = gmtCreatedStart;
    }

    public BigDecimal getGmtCreatedEnd() {
        return gmtCreatedEnd;
    }

    public void setGmtCreatedEnd(BigDecimal gmtCreatedEnd) {
        this.gmtCreatedEnd = gmtCreatedEnd;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
