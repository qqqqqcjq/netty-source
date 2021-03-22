package caverspark.netty;

import org.jboss.netty.bootstrap.Bootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2020/8/30 17:29
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//这个类就相当于http rpc websockt等，是基于netty实现的应用层的逻辑
public class Server {
    public static void main(String[] args) {
        //服务类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //boss线程，主要监听端口和获取worker线程以及分配socketchannel给worker线程
        ExecutorService boss = Executors.newCachedThreadPool();
        //worker线程， 负责数据的读写
        ExecutorService worker = Executors.newCachedThreadPool();

        //设置nioServerSocket工厂
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));

        //设置管道的工厂
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                //http websocket rpc等应用层协议，肯定也会使用编解码
                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addLast("encoder",new StringEncoder());
                pipeline.addLast("myServerMessageHandler",new MyServerMessageHandler());
                return pipeline;
            }
        });

        //服务类绑定端口
        serverBootstrap.bind(new InetSocketAddress(7777));
        System.out.println("服务端启动");
    }
}