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
            logAppender.accept("io 异常,连接socket失败");
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


            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            OutputStream out = this.socket.getOutputStream();
            byte[] buf = new byte[1024];
            long fileSize = 0;
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                fileSize += len;
                logAppender.accept(new String(buf, 0, len, "utf-8"));
            }
            // 最后发送结束标志
            byte[] endBytes = "\n###file EOF###\n".getBytes("utf-8");
            fileSize += endBytes.length;
            out.write(endBytes, 0, endBytes.length);
            out.flush();
            
            // 需要等待目标设置读取完成后，再关闭io
            while (socket.getInputStream() == null) {
                Thread.sleep(500);
            }
            // 接收远程响应
            InputStream response = socket.getInputStream();
            byte[] responseBytes = new byte[1024];
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
    }

    /**
     * socket关闭操作
     */
    public void closeSocket() {

        logAppender.accept("关闭网络");

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
