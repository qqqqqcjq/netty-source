java.nio.channels.Selector#select()
Selects a set of keys whose corresponding channels are ready for I/O operations.
选择一组键，它们对应的通道已经为I/O操作准备好了。
SelectableChannel#register(java.nio.channels.Selector, int)
Registers this channel with the given selector, returning a selection key.
将这个通道注册到给定的选择器中，并指明这个通道感兴趣的事件

while (true) {
    selector.select();
    // 获得selector中选中的项的迭代器
    Iterator ite = this.selector.selectedKeys().iterator();
    while (ite.hasNext()) {
        SelectionKey key = (SelectionKey) ite.next();
        // 删除已选的key,以防重复处理
        ite.remove();
        ....
    }
}
selector.select()得到一组就绪的事件，就绪的事件用SelectionKey封装，处理后要删除，这个删除只是把就绪事件删除了，不会删除通道感兴趣的事件类型SelectionKey.OP_WRITE和SelectionKey.OP_READ

NioClient和NioClient1的区别是有没有注册write事件给select
如果有channel在Selector上注册了SelectionKey.OP_WRITE事件，在调用selector.select();时，系统会检查内核写缓冲区是否可写（什么时候是不可写的呢，比如缓冲区已满，channel调用了shutdownOutPut等等），如果可写，selector.select();立即返回，随后进入key.isWritable()分支。
当然也可以不注册SelectionKey.OP_WRITE事件，你在channel上可以直接调用write(...)，也可以将数据发送出去，但这样不够灵活，而且可能浪费CPU。


socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
//循环处理
while (true) {
    selector.select();
    Set<SelectionKey> keys = selector.selectedKeys();
    Iterator<SelectionKey> iter = keys.iterator();
    while(iter.hasNext()) {
        SelectionKey key = iter.next();
        if(key.isConnectable()) {
            //连接建立或者连接建立不成功
            SocketChannel channel = (SocketChannel) key.channel();
            //完成连接的建立
            if(channel.finishConnect()) {

            }
        }
        if(key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(500 * 1024 * 1024);
            buffer.clear();
            channel.read(buffer);
            //buffer Handler
        }
        iter.remove();
    }
}
起初对OP_CONNECT事件还有finishConnect不理解，OP_CONNECT事件何时触发，特别是为什么要在key.isConnectable()分支里调用finishConnect方法后才能进行读写操作。
首先，在non-blocking模式下调用socketChannel.connect(new InetSocketAddress("127.0.0.1",8080));连接远程主机，如果连接能立即建立就像本地连接一样，该方法会立即返回true，
否则该方法会立即返回false,然后系统底层进行三次握手建立连接。连接有两种结果，一种是成功连接，第二种是异常，但是connect方法已经返回，无法通过该方法的返回值或者是异常来通
知用户程序建立连接的情况，所以由OP_CONNECT事件和finishConnect方法来通知用户程序。不管系统底层三次连接是否成功，selector都会被唤醒继而触发OP_CONNECT事件，如果握手成功，
并且该连接未被其他线程关闭，finishConnect会返回true，然后就可以顺利的进行channle读写。如果网络故障，或者远程主机故障，握手不成功，用户程序可以通过finishConnect方法获得
底层的异常通知，进而处理异常。

SelectionKey.OP_ACCEPT表示有客户端请求连接的事件
serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);


已经建立连接后不能再注册下面事件，会有非法性检查
channel.register(selector,SelectionKey.OP_ACCEPT);
channel.register(selector,SelectionKey.OP_CONNECT);

SelectionKey.OP_ACCEPT表示有客户端请求连接的事件
serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);   selectionKey.isAcceptable()表示连接就绪  selectionKey对应的是serverSocketChannel
channel.register(selector, SelectionKey.OP_CONNECT);   selectionKey.isConnectable()表示正在建立连接  selectionKey对应的是channel
客户端的channel是SocketChannel channel = SocketChannel.open()生成的实例
服务端的channel是SocketChannel channel = serverSocketChannel.accept()生成的实例