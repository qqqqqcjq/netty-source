package caverspark.nio;

import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @date 2020/8/30 14:33
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
  
public class NioClient {

    //通道管理器
    private Selector selector;

    /**
     * 获得一个Socket通道，并对该通道做一些初始化的工作
     * @param ip 连接的服务器的ip
     * @param port  连接的服务器的端口号
     * @throws IOException
     */
    public void initClient(String ip,int port) throws IOException {
        // 获得一个Socket通道
        SocketChannel channel = SocketChannel.open();
        // 设置通道为非阻塞
        channel.configureBlocking(false);
        // 获得一个通道管理器
        this.selector = Selector.open();

        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
        //用channel.finishConnect();才能完成连接
        channel.connect(new InetSocketAddress(ip,port));
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    @SuppressWarnings({"rawtypes" })
    public void listen() throws IOException {
        // 轮询访问selector
        while (true) {
            selector.select();
            // 获得selector中选中的项的迭代器
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 连接事件发生
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key
                            .channel();
                    // 如果正在连接，则完成连接
                    if(channel.isConnectionPending()){
                        if(channel.finishConnect()){

                        }

                    }
                    // 设置成非阻塞
                    channel.configureBlocking(false);

                    //在这里可以给服务端发送信息哦
                    channel.write(ByteBuffer.wrap(new String("向服务端发送了一条信息").getBytes()));
                    //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
                    channel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    //channel.register(selector,SelectionKey.OP_WRITE);
                    //channel.register(selector,SelectionKey.OP_ACCEPT);
                    //channel.register(selector,SelectionKey.OP_CONNECT);

                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key);
                } else if(key.isWritable()){
                    write(key);
                }

            }

        }
    }
    /**
     * 处理读取服务端发来的信息 的事件
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int readData = socketChannel.read(byteBuffer);
        if (readData > 0) {

            // 先将缓冲区数据转化成byte数组,再转化成String
            String msg = new String(byteBuffer.array(),"UTF-8").trim();
            System.out.println("客户端收到消息："+msg);

//            for (int i = 0; i < 20; i++) {
//                //回写数据
//                ByteBuffer writeBackBuffer = ByteBuffer.wrap("again".getBytes("UTF-8"));
//                socketChannel.write(writeBackBuffer);
//            }

        }
    }

    public void write(SelectionKey key) throws IOException {
        System.out.println("write jimo");

        SocketChannel socketChannel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //回写数据
        ByteBuffer writeBackBuffer = ByteBuffer.wrap("again".getBytes("UTF-8"));
        socketChannel.write(writeBackBuffer);

    }
    /**
     * 启动客户端测试
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient();
        client.initClient("localhost",8888);
        client.listen();
    }
}