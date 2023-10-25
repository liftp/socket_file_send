package com.hch;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hch.file.SocketSend;

/**
 * 简单发送socket文件流到目标设备的java桌面应用
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        App app = new App();

        app.initJFrame();
    }

    private String tipText = "1. 确保目标设备连接到同一wifi下 \n"
        + "2. 设置目标设备ip和端口，这里没有进行格式校验，使用时需注意 \n"
        + "3. 选择文件路径，指定要发送的文件 \n"
        + "4. 点击'发送文件'按钮，窗口显示发送完成，传输结束 \n";

    private int posX = 30;
    private int posY = 30;

    // 纵向间隙
    private int marginV = 50;
    // 横向间隙
    private int marginH = 120;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int marginPosX() {
        return marginH + posX;
    }

    public int addPosY() {
        this.posY = posY + marginV;
        return posY;
    }

    public void initJFrame() {

        JFrame jf = new JFrame("socket文件发送");
        

        JPanel panel = new JPanel();
        JLabel ipLabel = new JLabel("目标ip");
        

        JTextField ipInput = new JTextField("172.16.1.227", 20);
        

        JLabel portLabel = new JLabel("目标端口");
        

        
        JTextField portInput = new JTextField("80", 20);
        

        JButton fileBtn = new JButton("文件选择");
        

        JTextField sendText = new JTextField("未选择文件");
        
        // 文件选择
        fileBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int val = fc.showOpenDialog(null);
            if (val == JFileChooser.APPROVE_OPTION) {
                sendText.setText(fc.getSelectedFile().toString());
            } else {
                sendText.setText("未选择文件");
            }
        });

        JTextArea actionLogText = new JTextArea();
        actionLogText.setEditable(false);

        JScrollPane scroll = new JScrollPane(actionLogText);

        
        // actionLogText.setAutoscrolls(true);
        // actionLogText.append(null);
        
        // 文件发送
        JButton sendBtn = new JButton("发送文件");
        

        sendBtn.addActionListener(e -> {
            String filePath = sendText.getText().trim();
            if ("".equals(filePath) || sendText.getText().equals("未选择文件")) {
                actionLogText.append("请选择文件\n");
            } else {
                SocketSend socketSend = new SocketSend(
                    ipInput.getText().trim(), 
                    Integer.parseInt(portInput.getText().trim()), 
                    log -> actionLogText.append(log + "\n")
                    );
                socketSend.sendFileToRemote(filePath);

            }
        });


        // 使用提示
        JTextArea tip = new JTextArea();
        tip.setEditable(false);
        tip.setBackground(Color.GRAY);
        tip.setText(tipText);
        tip.setLineWrap(true);
        tip.setForeground(Color.YELLOW);


        panel.add(ipLabel);
        panel.add(ipInput);
        panel.add(portLabel);
        panel.add(portInput);
        panel.add(fileBtn);
        panel.add(sendText);
        panel.add(sendBtn);
        panel.add(scroll);
        panel.add(tip);

        jf.getContentPane().setLayout(null);
        panel.setLayout(null);

        ipLabel.setBounds(getPosX(), getPosY(), 100, 30);
        ipInput.setBounds(marginPosX(), getPosY(), 150, 30);
        portLabel.setBounds(getPosX(), addPosY(), 100, 30);
        portInput.setBounds(marginPosX(), getPosY(), 150, 30);
        fileBtn.setBounds(getPosX(), addPosY(), 100, 30);
        sendText.setBounds(marginPosX(), getPosY(), 150, 30);
        sendBtn.setBounds(getPosX(), addPosY(), 100, 30);
        actionLogText.setBounds(getPosX(), addPosY(), 200, 80);
        scroll.setBounds(getPosX(), getPosY(), 270, 80);
        tip.setBounds(getPosX(), getPosY() + 140, 270, 140);

        panel.setBounds(20, 20, 400, 600);
    
        
        jf.setBounds(20, 20, 400, 600);
        jf.setResizable(false);
        jf.add(panel);
        // jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
