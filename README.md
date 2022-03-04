# XPermission

[![](https://jitpack.io/v/YorekLiu/XPermission.svg)](https://jitpack.io/#YorekLiu/XPermission)

Android轻量级、链式API的动态权限处理框架，可实时解释权限用途，合规处理更方便。

## 导入依赖  

根目录的build.gradle中添加jitpack仓库：

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

在app的build.gradle配置依赖项：

```gradle
implementation 'com.github.YorekLiu:XPermission:${version}'
```

## 使用方式

由于SAM(Single Abstract Method)特性，在Java、Kotlin中的使用都显得非常简单。

java:

```java
XPermission.get(this)
    .permissions(Manifest.permission.CAMERA)
    .request((isGranted, grantedList, deniedList) -> {
        ...
    });
```

kotlin:

```kotlin
XPermission.get(this)
    .permissions(Manifest.permission.CAMERA)
    .request { isGranted, grantedList, deniedList ->
        ...
    }
```

## 高级特性

本库可同步展示权限说明，权限文案可全局指定，也可以针对特定的场景自定义。

![video](https://raw.githubusercontent.com/YorekLiu/XPermission/master/_screenshots/video.webp)
