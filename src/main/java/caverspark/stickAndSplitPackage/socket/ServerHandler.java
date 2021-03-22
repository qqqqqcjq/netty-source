package caverspark.stickAndSplitPackage.socket;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @date 2020/9/2 11:03
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class ServerHandler extends SimpleChannelHandler {

    //    @Override
//    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        //客户端发送了100条消息，如果没有粘包分包现象的话，这里应该打印100条
//        //但是实际结果只打印了2行，100条消息合并成了2条消息
//        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
//		byte[] bs = buffer.array();
//        System.out.println("server receive data " + new String(bs));
//    }

    @Override
    //MyDecoder处理后，这个SimpleChannelHandler处理的类型是new String(dst)
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("server receive data " + e.getMessage());
    }
}