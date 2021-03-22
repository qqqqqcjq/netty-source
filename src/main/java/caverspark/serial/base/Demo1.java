package caverspark.serial.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @date 2020/8/31 16:47
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//原始int转byte数组
public class Demo1 {
    public static void main(String[] args) throws IOException {
        int a = 11;
        int b = 22;
        int c = 33;

        //使用os就可以把自己序列写进磁盘或者网卡
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(intToBytes(a));
        os.write(intToBytes(b));
        os.write(intToBytes(c));
        byte[] byteArray = os.toByteArray();
        System.out.println(Arrays.toString(byteArray));

        //使用is就可以从磁盘或者网卡把数据读取到内存
        //也可以从内存中读取
        ByteArrayInputStream is = new ByteArrayInputStream(byteArray);
        byte[] aBytes = new byte[4];
        byte[] bBytes = new byte[4];
        byte[] cBytes = new byte[4];
        is.read(aBytes);
        is.read(bBytes);
        is.read(cBytes);
        System.out.println("a : " + bytesToInt(aBytes));
        System.out.println("b : " + bytesToInt(bBytes));
        System.out.println("c : " + bytesToInt(cBytes));

    }


    //大端序列，先写高位，再写低位
    //小端序列，先写低位，再写高位
    //以一个字节8位为单位
    //比如11 ： 二进制为 00000000 00000000 00000000 00001011，从左到右就从高到低

    //int转为小端字节序列
    public  static byte[] intToBytes(int value){
        byte[] byteArray = new byte[4];

        byteArray[3] = (byte)((value & 0xFF000000) >> 3 * 8); //取最高8位
        byteArray[2] = (byte)((value & 0x00FF0000) >> 2 * 8);
        byteArray[1] = (byte)((value & 0x0000FF00) >> 1 * 8);
        byteArray[0] = (byte)((value & 0x000000FF));

        return byteArray;
    }


    //小端字节序列转换为int
    public  static int bytesToInt(byte[] byteArray){
        return byteArray[0]&0xFF
                | byteArray[1] << 1*8&0xFF00
                | byteArray[2]<< 2*8 & 0xFF0000
                | byteArray[3] <<3*8 &0xFF000000;
    }
}

