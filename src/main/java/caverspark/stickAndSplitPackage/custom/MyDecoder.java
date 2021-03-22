package caverspark.stickAndSplitPackage.custom;

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

    private final static int BASE_LENGTH = 4;

    @Override
    //收到消息后会产生一个事件，回调这个方法
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        //收到数据之后，先判断buffer中刻度的数据长度是否大于数据包的基本长度：4个字节
        if(buffer.readableBytes() > BASE_LENGTH){
            //此时说明有数据到来
            //做标记：记住当前读指针的位置，为了回滚使用
            buffer.markReaderIndex();

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