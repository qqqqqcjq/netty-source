package caverspark.stickAndSplitPackage.socket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * @date 2020/9/2 14:12
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class MyDecoder extends FrameDecoder {

    private final static int BASE_LENGTH = 4 + 4;
    private static final int HEAD_FLAG = -32533456;

    @Override
    //收到消息后会产生一个事件，回调这个方法
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        //收到数据之后，先判断buffer中刻度的数据长度是否大于数据包的基本长度：4个字节
        if(buffer.readableBytes() > BASE_LENGTH){

            //防止socket攻击处理  begin-------------------
            if (buffer.readableBytes() > 500) {
                System.out.println("socket 攻击，跳过这条消息");
                buffer.skipBytes(buffer.readableBytes());
            }


            while (true){
                //做标记：标记当前读指针的位置，为了回滚使用
                //buffer.markReaderIndex(); 与 buffer.resetReaderIndex();配合使用回滚读指针
                //也可以使用 int beginReader = buffer.readerIndex()与buffer.readerIndex(beginReader)配合使用回滚读指针
                buffer.markReaderIndex();

                // 关键代码：读取包头，首先判断是否有包头的长度，如果没有需要继续等待数据, 所以要回滚读指针
                if(buffer.readableBytes() < 4){
                    return null;
                }

                // 一次读取4字节int，看是否是包头，如果是包头，跳出循环，然后继续读取后面的数据[长度+数据]
                if(buffer.readInt() == HEAD_FLAG){
                    break;
                }

                //不是包头，先回滚读指针，然后跳过一个字节(TCP/UDP粘包拆包的最小单位就是一个字节，不是一个bit)
                buffer.resetReaderIndex();
                buffer.readByte();

                //如果跳过一个字节后长度又变的不满足,返回null,等待与下一个数据包合并
                if(buffer.readableBytes() < BASE_LENGTH){
                    return null;
                }

            }
            //防止socket攻击处理  end-------------------


            // 说明此时有数据包到来，并且是从长度开始读取
            // 上面已经做了标记了，此处不需要在做标记
            // buffer.markReaderIndex();
            //读长度
            int dataLength = buffer.readInt();
            //读数据本身
            //1.数据本身的长度还不够，要继续等待后面的数据到来，所以要回滚读指针，然后返回null
            if(buffer.readableBytes() < dataLength){
                buffer.resetReaderIndex();

                //根据netty对decode()方法的调用逻辑：return null表示此事数据包不完整，需要继续等待下一个数据包的到来
                return null;
            }
            //2.否则，说明数据包已经完整，可以读数据
            byte[] dst = new byte[dataLength];
            buffer.readBytes(dst);

            return new String(dst);
            //继续传递给下一个handler, 根据netty对decode()方法的调用逻辑，下一个handler处理的就是new String(dst)
            //FrameDecoder extends SimpleChannelUpstreamHandler implements LifeCycleAwareChannelHandler

            //但是有一个问题，此时buffer中的数据可能还没有读完，那么剩下的数据怎么办：
            // 根据netty对decode()的调用逻辑，下一个收到这个客户端的消息，会和这次剩下的buffer合并，然后再调用decode()进行处理

        }
        return null;
    }
}