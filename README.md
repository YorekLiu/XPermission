# XPermission

Android轻量级、链式API的动态权限处理框架，可实时解释权限用途，合规处理更方便。

## 使用方式

由于SAM特性，在Java、Kotlin中的使用都非常简单。

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

![video](https://raw.githubusercontent.com/YorekLiu/AmazingWidget/master/_screenshots/video.webp)