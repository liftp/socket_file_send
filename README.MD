### 简单使用socket发送文件到远程设备
1. 展示
    ![image](https://github.com/liftp/socket_file_send/blob/main/src/main/resources/app_img.png?raw=true)
2. 使用说明
    * 确保目标设备连接到同一wifi下 
    * 设置目标设备ip和端口，这里没有进行格式校验，使用时需注意
    * 选择文件路径，指定要发送的文件 
    * 点击'发送文件'按钮，窗口显示发送完成，传输结束 
3. 本地exe打包
    安装wix3，javafx jmods 详情https://www.youtube.com/watch?v=r9aw6qhPUN8   
    安装好相关依赖，使用mvn clean package 打包，使用java -jar 成功运行程序之后，
    然后使用命令,基于项目路径：jpackage --main-jar .\target\socket_file_send-1.0-SNAPSHOT.jar --main-class com.hch.App --module-path "javafx-jmods local path" --add-modules javafx.controls,javafx.fxml --win-shortcut --win-menu
    成功后产生App-1.0.exe
    