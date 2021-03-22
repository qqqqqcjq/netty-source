package caverspark.nettyprotobuf.handler;

import caverspark.serial.protobuf.SubscribeReqProto;
import caverspark.serial.protobuf.SubscribeRespProto;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @date 2020/9/1 15:26
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class ServerMessageHandler extends SimpleChannelHandler {

    private SubscribeRespProto.SubscribeResp resp(int subReqId){
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqId)
        .setRespCode(0)
        .setDesc("订购成功");

        return  builder.build();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) e.getMessage();
        System.out.println("client req : " + req);
        ctx.getChannel().write(resp(req.getSubReqID()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
    }
}