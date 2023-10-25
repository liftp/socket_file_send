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

    private String ip;
    private int port;

    // log输出组件
    private Consumer<String> logAppender;

    /**
     * 构造方法
     * @param ip                目标设置ip
     * @param port              目标设置端口
     * @param logAppender       日志输出组件
     */
    public SocketSend(String ip, int port, Consumer<String> logAppender) {
        this.ip = ip;
        this.port = port;
        this.logAppender = logAppender;
    }

    /**
     * 发送文件到远程
     * @param filePath  文件地址
     */
    public void sendFileToRemote(String filePath) {
        try {
            
            logAppender.accept("连接到服务器");

            Socket socket = new Socket(ip, port);

            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            OutputStream out = socket.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                logAppender.accept(new String(buf, 0, len, "utf-8"));
            }
            // 最后发送结束标志
            byte[] endBytes = "\n###file EOF###\n".getBytes("utf-8");
            out.write(endBytes, 0, endBytes.length);
            out.flush();
            
            // 接收远程响应
            InputStream response = socket.getInputStream();
            byte[] responseBytes = new byte[1024];
            // 阻塞读直到响应
            int readSize = response.read(responseBytes);
            String responseContent = new String(responseBytes, 0, readSize, "utf-8");
            logAppender.accept("远程响应内容：");
            logAppender.accept(responseContent);
            // 关闭io
            in.close();
            out.close();
            // 关闭socket 
            socket.close();
            logAppender.accept("传输完成");
        } catch (IOException e) {
            logAppender.accept("io异常");
            e.printStackTrace();
        }
        
    }
}
