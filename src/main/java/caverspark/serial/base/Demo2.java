package caverspark.serial.base;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @date 2020/8/31 18:06
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//nio 序列化 ： 不能动态扩容
public class Demo2 {

    public static void main(String[] args) {
        int a = 11;
        int b = 22;

        //序列化
        //nio使用ByteBuffer进行序列化时，创建bytebuffer的时候，必须指定大小，所以没法扩容
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(a);
        buffer.putInt(b);
        byte[] array = buffer.array();
        System.out.println(Arrays.toString(buffer.array()));

        //反序列化
        ByteBuffer bb = ByteBuffer.wrap(array);
        System.out.println("a : " + bb.getInt());
        System.out.println("b : " + bb.getInt());

    }
}