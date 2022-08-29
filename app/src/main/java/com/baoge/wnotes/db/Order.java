package com.baoge.wnotes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "order")
public class Order {
    @Id(autoincrement = true)
    private Long id;

    //记录添加时间
    private long orderAddTime;
    //标记哪一天交易
    private long orderTime;

    private String city;
    private String hospital;
    private String departMent;
    private String technician;
    private String installer;
    private String device;
    private int devicePrice;

    //交易金额
    private int transactionAmount;
    //打车费用
    private int taxiFare;
    //配件价格
    private int partPrice;
    //装机费
    private int installPrice;
    //支持费用
    private int supportPrice;
    //支持姓名
    private String supportName;
    private String otherContent;
    private int otherPrice;
    private int invoice;//发票

    private boolean isAleadySupport;
    //新增，编辑，退货
    private int type;
    @Generated(hash = 1889993842)
    public Order(Long id, long orderAddTime, long orderTime, String city,
            String hospital, String departMent, String technician, String installer,
            String device, int devicePrice, int transactionAmount, int taxiFare,
            int partPrice, int installPrice, int supportPrice, String supportName,
            String otherContent, int otherPrice, int invoice,
            boolean isAleadySupport, int type) {
        this.id = id;
        this.orderAddTime = orderAddTime;
        this.orderTime = orderTime;
        this.city = city;
        this.hospital = hospital;
        this.departMent = departMent;
        this.technician = technician;
        this.installer = installer;
        this.device = device;
        this.devicePrice = devicePrice;
        this.transactionAmount = transactionAmount;
        this.taxiFare = taxiFare;
        this.partPrice = partPrice;
        this.installPrice = installPrice;
        this.supportPrice = supportPrice;
        this.supportName = supportName;
        this.otherContent = otherContent;
        this.otherPrice = otherPrice;
        this.invoice = invoice;
        this.isAleadySupport = isAleadySupport;
        this.type = type;
    }
    @Generated(hash = 1105174599)
    public Order() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getOrderAddTime() {
        return this.orderAddTime;
    }
    public void setOrderAddTime(long orderAddTime) {
        this.orderAddTime = orderAddTime;
    }
    public long getOrderTime() {
        return this.orderTime;
    }
    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getHospital() {
        return this.hospital;
    }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    public String getDepartMent() {
        return this.departMent;
    }
    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }
    public String getTechnician() {
        return this.technician;
    }
    public void setTechnician(String technician) {
        this.technician = technician;
    }
    public String getInstaller() {
        return this.installer;
    }
    public void setInstaller(String installer) {
        this.installer = installer;
    }
    public String getDevice() {
        return this.device;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    public int getDevicePrice() {
        return this.devicePrice;
    }
    public void setDevicePrice(int devicePrice) {
        this.devicePrice = devicePrice;
    }
    public int getTransactionAmount() {
        return this.transactionAmount;
    }
    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public int getTaxiFare() {
        return this.taxiFare;
    }
    public void setTaxiFare(int taxiFare) {
        this.taxiFare = taxiFare;
    }
    public int getPartPrice() {
        return this.partPrice;
    }
    public void setPartPrice(int partPrice) {
        this.partPrice = partPrice;
    }
    public int getInstallPrice() {
        return this.installPrice;
    }
    public void setInstallPrice(int installPrice) {
        this.installPrice = installPrice;
    }
    public int getSupportPrice() {
        return this.supportPrice;
    }
    public void setSupportPrice(int supportPrice) {
        this.supportPrice = supportPrice;
    }
    public String getSupportName() {
        return this.supportName;
    }
    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }
    public String getOtherContent() {
        return this.otherContent;
    }
    public void setOtherContent(String otherContent) {
        this.otherContent = otherContent;
    }
    public int getOtherPrice() {
        return this.otherPrice;
    }
    public void setOtherPrice(int otherPrice) {
        this.otherPrice = otherPrice;
    }
    public int getInvoice() {
        return this.invoice;
    }
    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }
    public boolean getIsAleadySupport() {
        return this.isAleadySupport;
    }
    public void setIsAleadySupport(boolean isAleadySupport) {
        this.isAleadySupport = isAleadySupport;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }


}
