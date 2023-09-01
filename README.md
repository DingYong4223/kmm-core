# kmm-core

[![CI Status](https://img.shields.io/travis/delanding/KNetwork.svg?style=flat)](https://travis-ci.org/delanding/kmm-core)
[![Version](https://img.shields.io/cocoapods/v/KNetwork.svg?style=flat)](https://cocoapods.org/pods/kmm-core)
[![License](https://img.shields.io/cocoapods/l/KNetwork.svg?style=flat)](https://cocoapods.org/pods/kmm-core)
[![Platform](https://img.shields.io/cocoapods/p/KNetwork.svg?style=flat)](https://cocoapods.org/pods/kmm-core)

## Example
kutils：支持双端公用的基础组件和方法，如Base64编解码能力等
kredux：kotlin-redux框架模块，实现redux能力
kmmkv：mmkv存储模块，封装了微信mmkv的能力
khttp：http请求模块，包含网络请求接口，协程能力支持
klogger：封装了双端的log能力，区分debug（调用第三方普通日志打印库）和release版本（调用xlog库）
iosApp：IOS可运行demo工程
androidApp：Android可运行demo工程
shared: IOS漏斗工程，用于向IOS侧输出符号

## Develop
1、打开settings.gradle.kts文件，通过打开头部注释决策当前开发模式，具体寒意参考尾注释，如：rootProject.name = "KmmCoreAllLocalRef"，表示全量编译，会比较耗时。
2、运行项目demo，查看库运行情况

## Requirements

## Author
delanding, dingyong4223@163.com

## License
任何人可以使用，扩展能力请发merge合入，欢迎一起共建