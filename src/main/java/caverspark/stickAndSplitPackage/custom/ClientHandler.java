package caverspark.stickAndSplitPackage.custom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * @date 2020/9/2 11:11
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class ClientHandler extends SimpleChannelHandler {
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        String message = "hello moto";
        byte[] msg = message.getBytes();

        //定义数据包 ,结构为： 4个字节的长度+数据
        ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
        //写长度 ：使用4个字节的Int型保存长度
        channelBuffer.writeInt(msg.length);
        //写数据本身
        channelBuffer.writeBytes(msg);
        for (int i = 0; i < 100; i++) {
            channel.write( channelBuffer);
        }
    }
}