package caverspark.netty;

import org.jboss.netty.channel.*;

/**
 * @date 2020/8/30 19:45
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class MyServerMessageHandler extends SimpleChannelHandler {

    @Override
    //接收数据
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("messageReceived");
        String s = (String) e.getMessage();
        System.out.println("服务端接收到的数据： " + s);

        //回写数据给客户端
        ctx.getChannel().write("hello...");
        super.messageReceived(ctx,e);
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        super.exceptionCaught(ctx, e);
    }

    //获取新连接事件
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
    }

    //关闭通道的时候触发(必须连接已经建立)
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    //通道关闭时触发

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }
}