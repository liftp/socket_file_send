### 简单使用socket发送文件到远程设备
1. 展示
    ![image](https://github.com/liftp/socket_file_send/blob/main/src/main/resources/app_img.jpg?raw=true)
2. 使用说明
    * 确保目标设备连接到同一wifi下 
    * 设置目标设备ip和端口，这里没有进行格式校验，使用时需注意；
    * 选择文件路径，指定要发送的文件 
    * 点击'发送文件'按钮，由于需要等待目标设置完成读取，不能发送完直接关闭io流， 所以需要手动'关闭网络传输'， 关闭主窗口也可以     