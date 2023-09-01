##工程简介：
kutils：支持双端公用的基础组件和方法，如Base64编解码能力等
kredux：kotlin-redux框架模块，实现redux能力
kmmkv：mmkv存储模块，封装了微信mmkv的能力
khttp：http请求模块，包含网络请求接口，协程能力支持
klogger：封装了双端的log能力，区分debug（调用第三方普通日志打印库）和release版本（调用xlog库）
iosApp：IOS可运行demo工程
androidApp：Android可运行demo工程
shared: IOS漏斗工程，用于向IOS侧输出符号

##开发流程：
1、打开settings.gradle.kts文件，通过打开头部注释决策当前开发模式，具体寒意参考尾注释，如：rootProject.name = "KmmCoreAllLocalRef"，表示全量编译，会比较耗时。
2、运行项目demo，查看库运行情况

##版本发布
本地调试通过后，发布单个模块（gradle->publishing->publish），视情况决定是否升级单个模块的版本号。发布版本需要本地配置maven账号信息，在local.properties中添加如下信息，信息在tencent maven oa上获取。
    publish_username=xxx
    publish_token=xxx
    1、使用如下单个命令发布单个模块：
        ./gradlew :khttp:publish
        ./gradlew :kloger:publish
        ./gradlew :kredux:publish
        ./gradlew :kutils:publish
        ./gradlew :kmmkv:publish
        ./gradlew :kfile:publish
    2、使用如下指令发布所有模块步骤：
        1）修改gradle.properties中发布链接，正式：MAVEN_REPO=https://mirrors.tencent.com/repository/maven/tenpay，snapshot：MAVEN_REPO=https://mirrors.tencent.com/repository/maven/tenpay-snapshot
        2）在local.properties中填写publish_username，publish_token
        3）./gradlew :khttp:publish :kloger:publish :kredux:publish :kutils:publish :kmmkv:publish :kfile:publish
    3、发布到本地maven（仅作调试使用）
        ./gradlew :khttp:publishToMavenLocal :kloger:publishToMavenLocal :kredux:publishToMavenLocal :kutils:publishToMavenLocal :kmmkv:publishToMavenLocal :kfile:publishToMavenLocal
