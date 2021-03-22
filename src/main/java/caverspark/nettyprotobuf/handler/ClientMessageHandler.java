package caverspark.nettyprotobuf.handler;

import caverspark.serial.protobuf.SubscribeReqProto;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @date 2020/9/1 15:26
 * @author chengjiaqing
 * @version : 0.1
 */


public class ClientMessageHandler extends SimpleChannelHandler {

    //接收消息到消息后会回调这个方法
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("server resp : " + e.getMessage());
    }

    //建立连接后开始给服务端发送数据
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected");

        for (int i = 0; i < 10; i++) {
            ctx.getChannel().write(req(i));
        }
        System.out.println("请求发送完毕");
    }

    //构建请求数据
    private SubscribeReqProto.SubscribeReq req(int i){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        List address = new ArrayList();
        address.add("长沙市岳麓区");
        address.add("深圳市南山区");
        builder.setSubReqID(i)
                .setProductName("netty权威指南"+i)
                .setUserName("xiaoming")
                .addAllAddress(address);
        return builder.build();
    }

    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

        for (int i = 0; i < e.getCause().getStackTrace().length; i++) {
            System.out.println(e.getCause().getStackTrace()[i]);
        }
        System.out.println(e.getCause());
    }

    /**
     * 必须是链接已经建立，关闭通道的时候才会触发
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
    }

    /**
     * channel关闭的时候触发
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
    }
}