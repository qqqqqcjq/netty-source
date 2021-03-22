package caverspark.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @date 2020/8/30 10:36
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class NioSocketDemo2 {

    //通道管理器(选择器)，多个用户公用，所以需要放在这里
    private Selector selector;

    /**
     * 初始化服务端ServerSocketChannel通道，并初始化选择器
     * 获得一个ServerSocketChannel，并对该通道做一些初始化的工作
     */
    public  void initServer(int port) throws IOException{
        // 获取ServerSocket通道 ， 相对于传统的ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞，传统的bio:accept()是阻塞的，前一个客户端连接没有断开之前，不会连接下一个客户端
        // 这里设置为非阻塞，每个客户端连接请求来了之后，都会接收, 然后交给linux的epoll函数去处理
        serverSocketChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道选择器(管理器)
        this.selector = Selector.open();
        // 将通道选择器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        // 当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        // 意思是大门交给selector看着，给我监听是否有accpet事件
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");

        /*
		***SelectionKey中定义的4中事件 ***
		OP_ACCEPT —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
		OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
		OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
		OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
		*/
    }

    /*
     * 采用轮询的方式监听selectors上是否有需要处理的事件，如果有，则进行处理
     */
    public void listenSelector() throws  IOException{

        //轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回，否则，该方法会一直阻塞
            //多路复用
            this.selector.select();

            //获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator iteratorKey = this.selector.selectedKeys().iterator();
            while(iteratorKey.hasNext()){
                SelectionKey selectionKey = (SelectionKey) iteratorKey.next();
                //删除已选的key,防止重复处理
                iteratorKey.remove();
                //处理请求
                hander(selectionKey);

            }
        }
    }

    //处理请求
    public void hander(SelectionKey selectionKey) throws IOException {

        //测试连接是否就绪
        if(selectionKey.isAcceptable()){
            System.out.println("新的客户端连接...");
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();

            //获得和客户端连接的通道
            //完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
            //如果serverSocketChannel是非阻塞的，如果连接没有就绪的话，accpet()会立即返回null,后面再次轮询，直到连接就绪
            //如果serverSocketChannel是阻塞的，那么accept()阻塞到连接就绪
            SocketChannel channel = serverSocketChannel.accept();

            //正式通话的channel也设置为非阻塞的
            channel.configureBlocking(false);

            //给正式通话的channel注册读事件
            channel.register(selector,SelectionKey.OP_READ );
            //已经建立连接后不能再注册下面事件，会有非法性检查
            //channel.register(selector,SelectionKey.OP_ACCEPT);
            //channel.register(selector,SelectionKey.OP_CONNECT);
        }
        //测试读数据是否就绪
        else if(selectionKey.isReadable()) {
            // 服务器可读取消息:得到事件发生的Socket通道
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            // 创建读取的缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int readData = socketChannel.read(byteBuffer);
            if (readData > 0) {

                // 先将缓冲区数据转化成byte数组,再转化成String
                String msg = new String(byteBuffer.array(),"UTF-8").trim();
                System.out.println("服务端收到消息："+msg);

                //回写数据
                ByteBuffer writeBackBuffer = ByteBuffer.wrap("again".getBytes("UTF-8"));
                socketChannel.write(writeBackBuffer);
            }
        }
        else{
            System.out.println("客户端关闭了");
            //SelectionKey对象会失效，这意味着Selector再也不会监控与它相关的事件
            selectionKey.cancel();
        }
    }

    public static void main(String[] args) throws IOException {
        NioSocketDemo2 nioSocketDemo = new NioSocketDemo2();
        nioSocketDemo.initServer(8888);
        nioSocketDemo.listenSelector();
    }

}