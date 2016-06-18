# Orange

## 简介

一时兴起,纯kotlin编写的微服务demo,主要模块为web,job,migration,db,因为想不出要做什么于是就随便扒扒ab站玩,用到的轮子有

- web框架: [spark](http://sparkjava.com/documentation.html)

- migration: [flyway](http://flywaydb.org)

- schedule框架: [quartz](http://www.quartz-scheduler.org/)

- 数据库层: [exposed](https://github.com/JetBrains/Exposed)

- 还有一些七七八八的常用轮子如 jackson,logback,fuel

名字来源: 『四月は君の嘘』的ED オレンジ 

## 启动参数说明

- `orange.env`

    命令行参数姿势`-Dorange.env=xxxxx`

    默认为`dev`

    用于指定系统的运行环境,在指定了`orange.config`后就暂时并没有什么卵用,以后可能会用于做不同环境的差别更新

    约定值为如下几个`dev`,`test`,`staging`,`production`

- `orange.roles`

    命令行参数姿势`-Dorange.env=role1,role2,role3`

    默认为`web,db,job`

    指定该系统的角色,不同的角色将承担不同的任务,现在暂时有如下几个角色

  - web

  ​    提供web接口服务

  - job

  ​    执行后台任务

  - db

  ​    执行migration更新数据库

- `logback.configurationFile`

    命令行姿势`-Dlogback.configurationFile=/opt/orange/config/logback.xml`

    默认为`classpath:logback.xml`

    指定日志系统logback配置文件的位置,暂时只支持xml,还没找到姿势让这个配置文件由程序初始化时订制

## 部署

参考[`gitlab-ci.yml`](.gitlab-ci.yml)文件(没试过,不保证可用,但大致姿势可以参考)

在项目路径下执行

```
gradle build
```

gradle的application插件会在`build/distributions`下生成打包好的zip和tar,内容都是一样的,解压任意一个到目标目录,然后执行`./bin/orange`即可

启动命令参考如下:

```
    JAVA_OPTS="-Dorange.roles=db,web,job -Dorange.dev=staging -D" ./bin/orange
```

systemd简单地配置参考[systemd](systemd/orange)