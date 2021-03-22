package caverspark.bio.example1;
/** 
 * @date 2020/5/20 22:57
 * @author chengjiaqing
 * @version : 0.1
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /**
     * 入口
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // 为了简单起见，所有的异常信息都往外抛
        int port = 8899;
        // 定义一个ServiceSocket监听在端口8899上
        ServerSocket server = new ServerSocket(port);
        System.out.println("等待与客户端建立连接...");
        while (true) {
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
            // 一直阻塞到有连接进来才会继续执行，处理连接，返回一个socket对象,请求的数据会放进socket中
            // 然后把socket交给一个线程处理，可以优化为线程池
            Socket socket = server.accept();
            new Thread(new Task(socket)).start();
        }
        // server.close();
    }
    /**
     * 处理Socket请求的线程类
     */
    static class Task implements Runnable {
        private Socket socket;
        /**
         * 构造函数
         */
        public Task(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                handlerSocket();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 跟客户端Socket进行通信
         *
         * @throws IOException
         */
        private void handlerSocket() throws Exception {
            // 跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String temp;
            int index;

            /**
             * BufferedReader的readLine方法是一次读一行的，这个方法是阻塞的，直到它读到了一行数据为止程序才会继续往下执行，
             * 那么readLine什么时候才会读到一行呢？直到程序遇到了换行符或者是对应流的结束符readLine方法才会认为读到了一行，才会结束其阻塞，让程序继续往下执行。
             * 所以我们在使用BufferedReader的readLine读取数据的时候一定要记得在对应的输出流里面一定要写入换行符（流结束之后会自动标记为结束，readLine可以识别），
             * 写入换行符之后一定记得如果输出流不是马上关闭的情况下记得flush一下，这样数据才会真正的从缓冲区里面写入。
             */
            while ((temp = br.readLine()) != null) {
                if ((index = temp.indexOf("eof")) != -1) {
                    // 遇到eof时就结束接收
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
            System.out.println("Form Cliect[port:" + socket.getPort() + "] 消息内容:" + sb.toString());

            Thread.sleep(10000);
            // 回应一下客户端
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            writer.write(String.format("Hi,%d.天朗气清，惠风和畅！", socket.getPort()));
            writer.flush();
            writer.close();
            br.close();
            socket.close();
        }
    }
}
