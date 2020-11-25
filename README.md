# DslBehavior

![](https://img.shields.io/badge/License-MIT-22EA0C) ![](https://img.shields.io/badge/Api-14+-D73220) ![](https://img.shields.io/badge/AndroidX-yes-3C2080)
![](https://img.shields.io/badge/Kotlin-yes-0DB922)

本库实现自`CoordinatorLayout`的`Behavior`.

使用此库时,请务必使用库中`RCoordinatorLayout`当做根布局, 而非系统的`CoordinatorLayout`.

库只做行为`Behavior`相关的协调, 不处理`View`相关的操作. 

> 子`View`请尽量使用`NestedScrollingChild`

# 相关效果

demo场景:

 - 普通布局, 比如`RCoordinatorLayout`+`RecyclerView`或者`RCoordinatorLayout`+`ViewGroup(+ViewGroup+(NestedScrollingChild)...)`
 - 联动布局, 比如`RCoordinatorLayout`+`背景放大Layout`+`头部RecyclerView`+`悬停TabLayout`+`底部ViewPager(VP内嵌套RecyclerView)`

Demo效果列表 | 上下回弹
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/demo.jpg) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/effect.jpg) 

智能阴影线 | 联动中效果
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/effect_line.jpg) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/effect2.jpg) 

标题栏渐变 | 联动中效果
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/gradient.jpg) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/gradient2.jpg) 

下拉刷新 | 联动中效果
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/refresh.jpg) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/refresh2.jpg) 

背景缩放 | 联动中效果
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/scale.jpg) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/scale2.jpg) 

## Gif效果

上下回弹 | 下拉刷新
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g1.gif) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g2.gif) 

智能提示线 | 标题栏渐变
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g3.gif) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g4.gif) 

背景放大 | 联动中效果
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g5.gif) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g7.gif) 

联动中标题栏渐变 | `酷安`效果
---|---
![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g5.gif) | ![](https://gitee.com/angcyo/DslBehavior/raw/master/png/g8.gif) 

# 使用文档

[使用文档wiki](https://github.com/angcyo/DslBehavior/wiki)

## 扫码下载体验

![](https://gitee.com/angcyo/DslBehavior/raw/master/png/code.png)

# 使用`JitPack`的方式, 引入库.

## 根目录中的 `build.gradle`

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

## APP目录中的 `build.gradle`

```kotlin
dependencies {
    implementation 'com.github.angcyo:DslBehavior:1.0.2'
}
```

---
**群内有`各(pian)种(ni)各(jin)样(qun)`的大佬,等你来撩.**

# 联系作者

[点此QQ对话](http://wpa.qq.com/msgrd?v=3&uin=664738095&site=qq&menu=yes)  `该死的空格`    [点此快速加群](https://shang.qq.com/wpa/qunwpa?idkey=cbcf9a42faf2fe730b51004d33ac70863617e6999fce7daf43231f3cf2997460)

[开源地址](https://github.com/angcyo/DslAdapter)

![](https://gitee.com/angcyo/res/raw/master/code/all_in1.jpg)

![](https://gitee.com/angcyo/res/raw/master/code/all_in2.jpg)
