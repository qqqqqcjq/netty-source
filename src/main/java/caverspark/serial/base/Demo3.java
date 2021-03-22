package caverspark.serial.base;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.util.Arrays;

/**
 * @date 2020/8/31 18:11
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//netty序列化 ： 可以动态扩容
public class Demo3 {

    public static void main(String[] args) {
        //ChannelBuffer是对ByteBuffer的封装，可以动态扩容，支持零拷贝
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeInt(11);
        buffer.writeInt(22);
        buffer.writeLong(23);

        //bytes数组的大小由buffer中写指针的位置决定
        //往channelbuffer中写数据的时候，这个写指针就会移动的长度就是写的数据的长度
        byte[] bytes = new byte[buffer.writerIndex()];
        //序列化
        buffer.readBytes(bytes);
        System.out.println(Arrays.toString(bytes));

        //反序列化
        ChannelBuffer wrappedBuffer = ChannelBuffers.wrappedBuffer(bytes);
        System.out.println(wrappedBuffer.readInt());
        System.out.println(wrappedBuffer.readInt());


    }
}