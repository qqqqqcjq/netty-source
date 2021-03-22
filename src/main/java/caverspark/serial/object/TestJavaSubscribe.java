package caverspark.serial.object;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2020/8/31 19:57
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//ObjectOutputStream ObjectInputStream性能是很低的
public class TestJavaSubscribe {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        deserializez(serialize());

    }

    //序列化
    public static byte[] serialize() throws IOException {
        SubscribeReq subscribeReq = new SubscribeReq();
        subscribeReq.setSubReqID(1);
        subscribeReq.setUserName("abc");
        subscribeReq.setProductName("giao");

        List<String> addressList = new ArrayList<>();
        addressList.add("changsha");
        addressList.add("shenzhen");
        subscribeReq.setAddress(addressList);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
        //序列化 ： 把subscribeReq对象写入ByteArrayOutputStream中
        objectOutputStream.writeObject(subscribeReq);
        //从ByteOutputStream获取序列化好的字节数组
        byte[] byteArray = baos.toByteArray();
        for (int i = 0; i < byteArray.length; i++) {
            System.out.print(byteArray[i]+",");
        }
        System.out.println();
        return  byteArray;
    }

    //反序列化
    public static SubscribeReq deserializez(byte[] byteArray) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream
                = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        SubscribeReq subscribeReq = (SubscribeReq) objectInputStream.readObject();
        System.out.println(subscribeReq.toString());
        return subscribeReq;
    }
}