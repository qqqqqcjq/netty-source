package caverspark.stickAndSplitPackage.socket;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2020/9/2 11:00
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class Server {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService woker = Executors.newCachedThreadPool();
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss,woker));
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("myDecoder", new MyDecoder());
                pipeline.addLast("myHandler", new ServerHandler());

                return pipeline;
            }
        });

        serverBootstrap.bind(new InetSocketAddress(7777));
        System.out.println("服务端启动...");
    }
}