package com.px.wsserver;


import com.px.wsserver.handler.WSServerInitialzer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WSServer {
	private Logger log = LoggerFactory.getLogger(getClass());
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private static ServerBootstrap serverBootstrap;
    private ChannelFuture future;
    public WSServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitialzer());
    }

    public void start() throws InterruptedException {
        this.future = serverBootstrap.bind(8686);
        System.err.println("netty wsserver已经启动完毕,端口-->8686");
        //future.channel().closeFuture().sync();
    }

    public static class SingletonWsserver{
        static final WSServer instance = new WSServer();
    }
    public static WSServer getInstance(){
        return SingletonWsserver.instance;
    }
    public static ServerBootstrap getServer(){
        return serverBootstrap;
    }

}
