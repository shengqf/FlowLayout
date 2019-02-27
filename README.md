# FlowLayout
流式布局
参考：https://github.com/hongyangAndroid/FlowLayout

使用：\n
1、项目根目录build文件添加：\n
allprojects {
    repositories {
        ...

        maven { url 'https://jitpack.io' }
    }
}

2、需要使用的FlowLayout的模块的build文件添加：\n
dependencies {
    ...
    
    implementation 'com.github.shengqf:FlowLayout:1.0.0'
}

3、同步工程即可使用
