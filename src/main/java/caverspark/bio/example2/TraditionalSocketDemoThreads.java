package caverspark.bio.example2;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @date 2020/8/29 16:46
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//来一个客户端新建一个线程，开启一个tcp连接，依次支持多客户端
//问题：tcp也是有最大连接数的， 线程数目也是有数量限制的，而且tcp连接和线程池都消耗内存，cpu等计算机资源，不能开启太多
public class TraditionalSocketDemoThreads {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("服务端启动");

        while(true){

            //获取socket套接字
            //accept()阻塞点
            Socket socket = serverSocket.accept();
            System.out.println("有新客户端连接上来了");

            new Thread(()->{
                //从客户端获取输入流
                InputStream is = null;
                try {
                    is = socket.getInputStream();
                    byte[] b = new byte[1024];

                    while(true){
                        //循环读取数据
                        //read阻塞点
                        int data = is.read(b);
                        if(data != -1){
                            String info = new String(b,0,data,"GBK");
                            System.out.println(info);
                        } else {
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();


        }
    }
}