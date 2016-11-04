>Dribbble是一个优秀的设计师网站，这里有上万优秀设计师为移动开发人员提供了海量精美的UI资源。Dribbble很早就开放了API，也有许多优秀的第三方客户端，本着学习的目的，我在课余时间写了这个还很粗糙的客户端。目前的功能还很简陋，用户体验也不是很完善，主要是因为Dribbble的API服务器在国外，国内加载资源很慢。不过，我会慢慢更新的，一点一点把它变得更好。

[项目github地址](https://github.com/mrrobot97/Designer)

先看一下预览图：

![](http://ockr1qfi1.bkt.clouddn.com/screener_20161029%2800_13_49%29.jpg)

![](http://ockr1qfi1.bkt.clouddn.com/screener_20161028%2823_54_36%29.jpg)

![](http://ockr1qfi1.bkt.clouddn.com/screener_20161029%2800_14_23%29.png)

![](http://ockr1qfi1.bkt.clouddn.com/screener_20161029%2800_15_02%29.png)

![](http://ockr1qfi1.bkt.clouddn.com/screener_20161029%2800_15_24%29.png)

![](http://ockr1qfi1.bkt.clouddn.com/screener_20161029%2800_15_39%29.png)

主要使用的技术和开源项目:

	1.	MVP设计模式。
	2.	RxJAVA
	3.	Retrofit
	4.	OkHttp
	5.	Glide

feature:

	1.页面支持滑动返回。
	2.离线缓存浏览。
	
	
	
todo list：

	1.	使用数据库离线缓存,节省客户端流量。
	2.	使用palette动态改变背景色，增加美观性。
	3.	使用Dribbble提供的Oauth2认证允许用户登录，并对每个设计进行评论，点赞，收藏等功能。
	4.	改善图片加载速度，提供友好的用户反馈。
	5. 发现并消灭BUG，提高软件使用的稳定性。
	
	
欢迎fork、issue、star。
	
[release apk download](http://ockr1qfi1.bkt.clouddn.com/Designer.apk)
