package caverspark.stickAndSplitPackage.nettysolution;

import org.jboss.netty.channel.*;

/**
 * @date 2020/9/2 15:51
 * @author chengjiaqing
 * @version : 0.1
 */


public class ClientMessageHandler extends SimpleChannelHandler {

    /**
     * 接收消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("server response : " + e.getMessage());
    }

    /**
     * 新连接
     */
//    @Override
//    //连接建立后，基于特定分割符发送消息
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        String separator = "#@#";//特定字符
        String msg = "zhang fei send cmd";
        for (int i = 0; i < 1000; i++) {
            channel.write(msg + i + separator);
        }
    }
//
//    //连接建立后，基于特定分割符发送消息
//    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//        Channel channel = ctx.getChannel();
//        String separator = System.getProperty("line.separator");// 系统换行符
//        String msg = "zhang fei send cmd";
//        for (int i = 0; i < 1000; i++) {
//            channel.write(msg + i + separator);
//        }
//    }
    //连接建立后，基于特定长度19个字节发送消息
//    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//        System.out.println("连接建立...");
//        Channel channel = ctx.getChannel();
//        String msg = "zhang fei send cmd";
//        for (int i = 0; i < 1000; i++) {
//            channel.write(msg);
//        }
//    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        for (int i = 0; i < e.getCause().getStackTrace().length; i++) {
            System.out.println(e.getCause().getStackTrace()[i]);
        }
    }
}
