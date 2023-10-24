package com.hch.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class SocketSend {

    private Socket socket;

    // log输出组件
    private Consumer<String> logAppender;

    // 本地文件流
    private InputStream in;

    // socket输出流
    private OutputStream out;

    public void setLogAppender(Consumer<String> logAppender) {
        this.logAppender = logAppender;
        
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * 构造方法
     * @param ip                目标设置ip
     * @param port              目标设置端口
     * @param logAppender       日志输出组件
     */
    public SocketSend(String ip, int port, Consumer<String> logAppender) {
        try {
            socket = new Socket(ip, port);
            this.logAppender = logAppender;
        } catch (UnknownHostException e) {
            System.out.printf("未知主机异常");
            e.printStackTrace();
        } catch (IOException e) {
            logAppender.accept("io 异常");
            e.printStackTrace();
        }
    }

    /**
     * 发送文件到远程
     * @param filePath  文件地址
     */
    public void sendFileToRemote(String filePath) {
        try {
            
            logAppender.accept("连接到服务器");


            in = new BufferedInputStream(new FileInputStream(filePath));
            out = this.socket.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                logAppender.accept(new String(buf, 0, len, "utf-8"));
            }
            byte[] endBytes = "\n###file EOF###\n".getBytes("utf-8");
            out.write(endBytes, 0, endBytes.length);
            out.flush();
            // 这里不仅io关闭，需要等待目标设置读取完成后，手动操作关闭

            
        } catch (IOException e) {
            logAppender.accept("io异常");
            e.printStackTrace();
            return;
        }

        
    }

    /**
     * socket及文件关闭操作
     */
    public void closeSocket() {

        logAppender.accept("关闭网络");
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logAppender.accept("in关闭异常");
                e.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                logAppender.accept("out关闭异常");
                e.printStackTrace();
            }
        }

        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                logAppender.accept("socket已关闭");
            } catch (IOException e) {
                logAppender.accept("socket关闭异常");
                e.printStackTrace();
            }
        }
        
    }
}
