package caverspark.stickAndSplitPackage.socket;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2020/9/2 11:00
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class Client {

    public static void main(String[] args) {
        ClientBootstrap clientBootstrap = new ClientBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("1", new StringEncoder());
                pipeline.addLast("2",new ClientHandler());
                return pipeline;
            }
        });

        ChannelFuture future = clientBootstrap.connect(new InetSocketAddress("127.0.0.1",7777));
    }
}