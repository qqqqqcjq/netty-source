package caverspark.bio.example2;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2020/8/29 16:59
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//使用线程池解决资源有限的问题
public class TraditionalSocketThreadPool {

    public static void main(String[] args) throws IOException {

        //ExecutorService threadpool = Executors.newCachedThreadPool();
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("服务端启动");

        while(true){
            Socket socket = serverSocket.accept();
            threadPool.execute(()->{
                try {
                    System.out.println("有新客户端连接上来了...");
                    // 获取客户端输入流
                    InputStream is = socket.getInputStream();
                    byte[] b = new byte[1024];
                    while (true) {
                        // 循环读取数据
                        // read() 阻塞点
                        int data = is.read(b);
                        if (data != -1) {
                            String info = new String(b, 0, data, "GBK");
                            System.out.println(info);
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    }
}