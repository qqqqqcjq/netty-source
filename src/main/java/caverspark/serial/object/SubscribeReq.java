package caverspark.serial.object;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2020/8/31 19:54
 * @author chengjiaqing
 * @version : 0.1
 */


public class SubscribeReq implements Serializable {

    private static final long serialVersionUID = 1L;;

    private int subReqID;
    private String userName;
    private String productName;
    private List address;

    public int getSubReqID() {
        return subReqID;
    }
    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public List getAddress() {
        return address;
    }
    public void setAddress(List address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return "SubscribeReq [subReqID=" + subReqID + ", userName=" + userName + ", productName=" + productName
                + ", address=" + address + "]";
    }
}