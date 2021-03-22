package caverspark.stickAndSplitPackage.nettysolution;

import org.jboss.netty.channel.*;

/**
 * @date 2020/9/2 15:52
 * @author chengjiaqing
 * @version : 0.1
 */

public class ServerMessageHandler extends SimpleChannelHandler {

    /**
     * 接收消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("receive request: " + e.getMessage());

    }

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


