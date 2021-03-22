package caverspark.nettyprotobuf;

import caverspark.nettyprotobuf.handler.ServerMessageHandler;
import caverspark.serial.protobuf.SubscribeReqProto;
import org.jboss.netty.bootstrap.Bootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2020/9/1 15:25
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class Server {
    public static void main(String[] args) {
        //服务类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //boss线程，主要监听端口和获取worker线程以及分配socketchannel给worker线程
        ExecutorService boss = Executors.newCachedThreadPool();
        //worker线程负责数据的读写
        ExecutorService worker= Executors.newCachedThreadPool();

        //设置niosocket工厂
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
        //设置管道的工厂
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                //管道过滤器
                pipeline.addLast("1",new ProtobufVarint32FrameDecoder());
                pipeline.addLast("2",new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                pipeline.addLast("3",new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast("4",new ProtobufEncoder());
                pipeline.addLast("5",new ServerMessageHandler());
                return pipeline;
            }
        });

        //服务类绑定端口
        serverBootstrap.bind(new InetSocketAddress(7777));
        System.out.println("服务端启动...");
    }
}