# EASecurity

## 介绍  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;企业级应用安全中心（Enterprise Application Security Center，简称EASecurity）。  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;满足安全三要素CIA（保密性Confidentiality、完整性Integrity、可用性Availability），符合安全架构5A方法论（身份认证Authentication、授权Authorization、访问控制Access Control、可审计Auditable、资产保护Asset Protection）。  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;符合中国国内各大央企的安全管理要求，在保证开发人员易用性（符合国内编程人员使用习惯）前提下，最大程度的做到可配置和易管理。开发和运维分离，做到上线后运维维护权限变更不用再次发版。  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在企业中，人员的基本信息、账号信息、组织架构信息、权限（授权）信息，这些信息是与实体关系对应的，且不会因系统应用的不同而不同。这些信息，多数都涉及商业秘密，其查看及使用都应该被安全管控。旧的企业架构中，数据割裂非常严重。不同应用系统由不同厂商提供，运维操作人员不安全性较高，多数开发人员可以轻松的拿到企业正式数据，一不留神就会公开到互联网。EASecurity可以很好的规避以上问题。通过微服务的设计理念，配合OAuth2，可以安全的共享这些业务数据。既不影响业务系统的开发与应用（可以像调用本地接口一样的使用以上数据），又能做到安全管控。  

## 软件架构  
软件架构说明：  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;基于java开发，核心代码100%使用原生Java或者独立的公共组件，避免以依赖上的冲突。在外围嵌入代码层面（或者说集成代码），支持多种开发框架的集成（如spring boot）。    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平台采用分布式架构设计。

## 软件架构图  
- **功能图**  
<a href="doc/架构/功能图.png"><img src="doc/架构/功能图.png" width="80%"></a>  
<br>  

- **技术栈**  
<a href="doc/架构/技术栈.png"><img src="doc/架构/技术栈.png" width="80%"></a>  
<br>  

- **认证模式：基于session共享**  
<a href="doc/架构/认证模式/1基于session共享.png"><img src="doc/架构/认证模式/1基于session共享.png" width="40%"></a>  
<br>  

- **认证模式：基于cookie的JWT**  
<a href="doc/架构/认证模式/2基于cookie的JWT.png"><img src="doc/架构/认证模式/2基于cookie的JWT.png" width="40%"></a>  
<br>  

- **认证模式：基于AccessToken的JWT**  
AccessToken和JWT（Json web token）是2种token认证模式。AccessToken是一串随机字符串，从中不能获取到任何信息，你需要将它发送到服务器进行解析。JWT 中包含了主体、受众、权限、颁发时间、过期时间、用户信息字段等内容且具备签名，不可篡改，因此无需发送到服务器，可以本地验证。  
JWT中存放有大量用户信息，尤其是权限信息。部分信息不宜暴露到外网。AccessToken中没有任何信息，不会泄密。所以，外网终端获取AccessToken，然后回传给内网服务器，内网服务器通过AccessToken兑换含有信息jwt进行使用。从而保证了服务器的“无状态”。  
注：企业应用不同于互联网应用，网络可控，可以区分内外网。互联网应用用户和服务都是一张网，无法区分内外网。  
多系统应用间的AccessToken兑换另外的AccessToken？
<a href="doc/架构/认证模式/3基于AccessToken的JWT.png"><img src="doc/架构/认证模式/3基于AccessToken的JWT.png" width="40%"></a>  

## 安装教程  
EASecurity应用程序共分3部分：  
- **1.&nbsp;&nbsp;admin：**  
&nbsp;&nbsp;&nbsp;&nbsp;系统管理员应用，独立部署的程序。可部署到内网的网络安全大区（每个企业管理不一样，根据实际情况决定）。可使用源码部署（源码采用Grails框架），也可以是war包部署（编译后的程序），也可以使用docker部署（待完善）。部署步骤详见[《admin部署》](doc/部署/admin.md)。
<br>  

- **2.&nbsp;&nbsp;SecurityCentre：**  
&nbsp;&nbsp;&nbsp;&nbsp;认证及访问控制中心，独立部署的程序。终端用户真实访问的应用，需要部署到用户可以访问到网络。可使用源码部署（源码采用Springboot框架），也可以是war包部署（编译后的程序），也可以使用docker部署（待完善）。部署步骤详见[《SecurityCentre部署》](doc/部署/SecurityCentre.md)。
<br>  

- **3.&nbsp;&nbsp;客户端应用：**  
&nbsp;&nbsp;&nbsp;&nbsp;客户端应用为各项目自主研发的应用系统（以下简称应用系统）。为终端用户提供业务处理。对于应用系统的开发语言目前只Java，对于框架没有要求，可以是微服务架构、单体应用，可以是Spring体系也可以是纯Java的应用系统。EASecurity平台为不同的技术体系研发了多个集成组件，选择合适的进行集成即可。集成步骤详见[《framework集成》](doc/部署/framework.md)。  
<br>  

## 使用说明  

1.  xxxx
2.  xxxx
3.  xxxx

## 参与贡献  

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


## 特技  

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
