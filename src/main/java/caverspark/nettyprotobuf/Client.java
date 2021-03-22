package caverspark.nettyprotobuf;

import caverspark.nettyprotobuf.handler.ClientMessageHandler;
import caverspark.serial.protobuf.SubscribeRespProto;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2020/9/1 15:25
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class Client {

    public static void main(String[] args) throws InterruptedException {
        //服务类
        ClientBootstrap clientBootstrap = new ClientBootstrap();

        //线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        //socket工厂
        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));

        //管道工厂
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                //没有下解码处理，e.getMessage是ChannelBuffer类型
				pipeline.addLast("1", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("2", new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
				pipeline.addLast("3", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("4", new ProtobufEncoder());
                pipeline.addLast("5",new ClientMessageHandler());
                return pipeline;
            }
        });

        ChannelFuture connect = clientBootstrap.connect(new InetSocketAddress("127.0.0.1", 7777)).sync();
    }
}