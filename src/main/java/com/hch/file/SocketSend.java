package com.hch.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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

            OutputStream out = socket.getOutputStream();
            File destFile = new File(filePath);
            long length = destFile.length();

            // 写入文件信息，"文件名:文件大小;"，等待目标设备就绪
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            out.write((fileName + ":" + length + ";").getBytes("utf-8"));
            out.flush();
            // 阻塞等待目标响应
            recvSocket(socket);
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                // logAppender.accept(new String(buf, 0, len, "utf-8"));
            }
            out.flush();
            
            // 接收远程响应
            recvSocket(socket);
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

    void recvSocket(Socket socket) throws IOException {
        InputStream response = socket.getInputStream();
        byte[] responseBytes = new byte[1024];
        // 阻塞读直到响应
        int readSize = response.read(responseBytes);
        String responseContent = new String(responseBytes, 0, readSize, "utf-8");
        logAppender.accept("远程响应内容：");
        logAppender.accept(responseContent);
    }
}
