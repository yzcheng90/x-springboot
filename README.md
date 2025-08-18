<h1> X-SpringBoot </h1>

![Image text](https://img.shields.io/badge/x--springboot-v6.0-green.svg)
![Image text](https://img.shields.io/badge/springboot-3.5.4-green.svg)
![Image text](https://img.shields.io/badge/MyBatis%20Plus-3.5.12-green.svg)

[更新日志](https://github.com/yzcheng90/X-SpringBoot/tree/master/doc/updateLog.md) | [项目地址](https://github.com/yzcheng90)  | [SpringCloud版本](https://github.com/yzcheng90/ms) |[前台项目地址](https://github.com/yzcheng90/x-springboot-ui)


## 项目说明
- X-SpringBoot 是一个轻量级的Java快速开发平台，基于各大开源项目组合而来，用于快速构建中小型API、RESTful API项目，该项目已经有过多个真实企业级项目的实践，稳定、简单、快速，使我们摆脱那些重复劳动。
- 本项目已大量重构,精简了大量代码减少第三方依赖，最干净的脚手架。
- 引入了lombok 大量简化了代码
- 引入了MyBatis Plus 大量简化了SQL
- 引入hutool 工具包 规范工具类
- 引入minio 分布式文件系统
- 新增RBAC权限控制
- 前后端完全脱离，前端代码可单独部署
- 自定义Spring Security 支持获取token
- 新增RBAC接口级权限控制
- 【租户管理】，支持多租户数据管理
- 【应用管理】，可配置 appKey、appSecret，针对开放API业务，使用appKey、appSecret验证
- 【短信管理】，可配置不同短信渠道商（不断完善），支持多模板，支持appKey、appSecret验证
- 接下来继续丰富通用业务进行开源，若有需求可提issues or contact the author

## 后台密码
- 账号密码：admin/admin

## 版本信息
- 核心框架：Spring Boot 3.5.4
- 安全框架：Spring Security 6.x
- 持久层框架：MyBatis Plus 3.5.12
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x 


## 环境 
- jdk 21
- mysql 8.0
- redis
- nginx


## 项目结构 
```
X-SpringBoot
├─doc  
│  ├─db.sql 项目SQL语句
│  ├─nginx.confi nginx 配置文件
│  ├─updateLog 更新日志
│
├─authentication 权限认证
├─common 公共模块
│  ├─annotation 自定义注解
│  ├─aspect 系统日志
│  ├─base base包
│  ├─exception 异常处理
│  ├─utils 一些工具类
│  └─xss XSS过滤
│ 
├─config 配置信息
├─interceptor token拦截器
│ 
├─modules 功能模块
│  ├─oss 文件服务模块
│  ├─sys 权限模块
│  └─gen 代码生成
│  └─apk 安卓APK管理
│  └─application 应用管理
│  └─msg 短信
│ 
├─Application 项目启动类
│  
├──resources 
│  ├─mapper SQL对应的XML文件


```

## 系统部分截图
![Image text](https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/20230122174113.png)
![Image text](https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/20230122174148.png)
![Image text](https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/20230122174204.png)


## 学习交流

x-springboot 是完全开源免费的项目，目前仍然在优化迭代中，旨在帮助开发者更方便地进行快速敏捷开发，有使用问题欢迎在交流群内提问。

|                                              Q群                                              |                                               微信群                                                |                                            作者微信                                            |
|:--------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:|
| <img src="https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/qq_group.jpg" width=170> | <img src="https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/wechat_group.jpg" width=170> | <img src="https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/wechat.jpg" width=170> |

> 添加微信请注明来意

## 贡献

如果您发现了任何问题或有改进建议，请创建一个[issue](x-springboot/issues/new)或提交一个PR。我们欢迎您的贡献！

## 支持

如果感觉本项目对你工作或学习有帮助，请帮我点一个✨Star,这将是对我极大的鼓励与支持, 也可以在下方请我喝一杯咖啡

|                                               微信                                               |                                            支付宝                                             |
|:----------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:|
| <img src="https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/wechat_pay.jpg" width=170> | <img src="https://github.com/yzcheng90/X-SpringBoot/blob/master/pic/alipay.jpg" width=170> |



