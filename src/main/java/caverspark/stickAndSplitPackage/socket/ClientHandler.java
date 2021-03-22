package caverspark.stickAndSplitPackage.socket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @date 2020/9/2 11:11
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class ClientHandler extends SimpleChannelHandler {

    //定义一个包头，就是一个标志，标识一条消息，要特殊一点
    private static final int HEAD_FLAG = -32533456;

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        String message = "hello moto";
        byte[] msg = message.getBytes();

        //定义数据包 ,结构为： 4个字节的长度+数据
        ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();

        //写包头
        channelBuffer.writeInt(HEAD_FLAG);
        //写长度 ：使用4个字节的Int型保存长度
        channelBuffer.writeInt(msg.length);
        //写数据本身
        channelBuffer.writeBytes(msg);
        for (int i = 0; i < 1000; i++) {
            channel.write( channelBuffer);
        }
    }
}