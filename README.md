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

## 碎碎念

1. 以前尝试用kotlin写web服务,考虑到kotlin对java的无缝接入,选择了最熟悉的springboot,结果磕磕绊绊地遇到了非常多坑,有来自cglib的,有来自各种类库的,还有kotlin自身一些诡异的类型推断及IDE牛头不对马尾的报错.

2. 曾经的spring以轻量级java框架著称,但现在的spring很难再轻盈了.springboot是我最喜欢的web框架,但每次修改代码等待重启40+秒实在是让人抓狂.在这40+s乘以n的等待中,我开始在想,当我仅仅只是需要一个简单的微服务时.在业务代码里,为什么要设计一层层的接口然后分别实现?为什么要ORM?为什么要IOC?甚至于为什么要AOP?回想以前用过的轮子,rails,sinatra,flask,expressjs,好像我只有在写spring的时候才要去纠结一大堆配置,繁琐费解的应用初始化,以及理解各种各样的框架模型

3. 于是就弄了现在这玩意儿,某种意义上来说这些个轮子就是各种丑陋的静态方法替代了单例bean,从开始选型到做完花了2天,大部分时间花在了gradle上,这个项目是由已经上线的公司内部项目剥离出来的demo

4. spark标榜着sinatra for java,一样是一页文档,sb几十个M,spark几百k,相对的它的功能也非常简单,就是内置jetty的web服务框架.对外的api简单易懂,`Spark.get`就是对服务器添加一个get方法的路由.唯一麻烦的在于kotlin的lambda并不能完全替代java8的FunctionInterface,造成了一些困扰,代码里我在`Helper`里重新装饰了一下get方法解决问题.

5. fuel有两个亮点,一个是利用kotlin的open class特性,给基础string添加方法, 简直exciting ;另一个是对http请求报错的处理上,像go一样返回了一个Pair对象,body + 封装好的错误,这和各种公司看到的五花八门的http工具类里的实现基本一致,真正省去了用轮子的人自己再包装一层的问题

6. exposed是jetbrains出的kotlin的sql轮子.想想kotlin也是jetbrains出品,说不定exposed以后就是kotlin的标配了.事实上这个轮子作为类似ActiveRecord一样的库相当不错,虽然api相当不齐全,文档基本没有.但好在实现非常简单,看源码如同看文档,不足的api可以自己来补充.exposed算是刷新了我对kotlin各种inline infix的认知,虽然依旧不是很明白,但这类似sql的语法真是直观得令人感动,举个例子

   ```
   Bog.select{ name.like("%bishi") and r_seventeen.eq(true) }
   ```

   基本核心思想和spark,fuel,以及现代化语言一样,让使用者尽可能地用代码来描述业务流.这玩意儿最坑的地方在于 Entity类没order方法,Table类没findById方法.

7. 一个通宵整理代码写demo,实际上花了2小时在回忆ac的用户接口上,想了半天才想起来这个古老的接口

8. Orange这个object是我希望能像rails那样以最简单的姿势获取各种配置,通过kotlin data class非常容易构造的特性,让配置最简化以及pojo对象化.jackson大法真是无敌了,连yml都有插件

9. migration是个好习惯,flyway是我认知里最简单的migration轮子了

10. kotlin为了兼容该死的jdk6和7而用了jodatime,导致java8time和jodatime之间还得通过时间戳转一次,真希望kotlin能有jdk8 only的版本

11. 由于kotlin和cglib可能八字不合,暂时不知道怎么mock对象,测试也就没怎么写了

12. rxjava系列的东西这个项目没怎么用,但这可能是个潜力股

13. 挂着刷接口,现在感觉好像被A,B站都屏蔽了= =

14. kotlin自身及其相关库lambda接口相当多,很容易写着写着就变成js那样的回调地狱,并且嵌套层次深了后一个小错误都很难找出来,IDE的报错经常匪夷所思,lambda+泛型的可读性相当差,整个错误描述都看的一脸懵逼

15. 所以适度for循环一下调节一下代码结构也挺好的

16. gradle对我来说就是java的rake,其诞生出的各种各样的圆形带魔法的轮子就理所当然了,想想ruby的部署工具mina,capistrano,rails的各种命令行工具,全都是仰赖于rake来执行.可见gradle应该还有很多可开发的空间,也许随着java9的模块化,java应用打包和发布姿势的变更,有朝一日我们能在服务器上看到java应用能依靠gradle执行脚本,像rails那样实现hotswap和在线控制台.