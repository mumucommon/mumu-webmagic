# mumu-webmagic 爬虫
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/mumucache/mumu-riak/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.weibo/motan.svg?label=Maven%20Central)](https://github.com/mumucommon/mumu-webmagic)
[![Build Status](https://travis-ci.org/mumucommon/mumu-webmagic.svg?branch=master)](https://travis-ci.org/mumucommon/mumu-webmagic)
[![codecov](https://codecov.io/gh/mumucommon/mumu-webmagic/branch/master/graph/badge.svg)](https://codecov.io/gh/mumucommon/mumu-webmagic)
[![OpenTracing-1.0 Badge](https://img.shields.io/badge/OpenTracing--1.0-enabled-blue.svg)](http://opentracing.io)  

***WebMagic是一个简单灵活的Java爬虫框架。基于WebMagic，你可以快速开发出一个高效、易维护的爬虫。***

## 特性：

- 简单的API，可快速上手
- 模块化的结构，可轻松扩展
- 提供多线程和分布式支持


## 架构
WebMagic的结构分为Downloader、PageProcessor、Scheduler、Pipeline四大组件，并由Spider将它们彼此组织起来。这四大组件对应爬虫生命周期中的下载、处理、管理和持久化等功能。WebMagic的设计参考了Scapy，但是实现方式更Java化一些。

而Spider则将这几个组件组织起来，让它们可以互相交互，流程化的执行，可以认为Spider是一个大的容器，它也是WebMagic逻辑的核心。  

WebMagic总体架构图如下：   
![](http://code4craft.github.io/images/posts/webmagic.png)

## 相关阅读  
[webmagic爬虫](http://webmagic.io/)  
[Bloom Filter](http://blog.csdn.net/jiaomeng/article/details/1495500)

## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**