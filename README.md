# socket-instant_messaging
java socket即时通讯软件

1. 采用C/S模式，使得各个用户通过服务器转发实现聊天的功能。
2. 分为两大模块：客户端模块和服务器端模块。
3. 客户端模块的主要功能：
1）登陆功能：用户可以注册，登录。
2）显示用户：将在线用户显示在列表中。
3）接收信息：能接收其他用户发出的信息。
4）发送信息：能发出用户要发出的信息。
5）私聊功能：选择用户进行私聊。

4.服务器端模块的主要功能：
1)检验登陆信息：检查登陆信息是否正确，并向客户端返回登陆信息，如信息正确。就允许用户登陆。
2)显示在线状态：将该用户的状态发给各在线用户。
3)转发聊天信息：将消息转发给所有在线的用户


本人csdn博客上有较详细的说明：https://blog.csdn.net/congcong7267/article/details/88567710
