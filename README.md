# 应用程序统计，采集应用打开痕迹

[![Download][icon_download]][download]

```
compile 'com.excellence:app-statistics:_latestVersion'
```

## 说明

* Android5.0以后使用，暂未兼容Android5.0以前版本
* 判断访问权限，是否弹出对话框进行申请
* 使用回调的方式返回结果

## 示例

* 全部应用数据

    ![All][All]

* 单个应用具体数据

    ![App][App]

* 单个应用使用一次期间的Activity痕迹

    ![Activity][Activity]


## 版本

| 版本 | 描述 |
| ---- | --- |
| [1.0.1][1.0.1] | 优化权限检测 **2018-7-27** |
| [1.0.0][1.0.0] | 判断访问权限，统计应用使用信息 **2018-7-25** |


## 感谢

* [Wingbu][Wingbu]
* [zp-zone][zp-zone]

<!-- 网站链接 -->

[download]:https://bintray.com/veizhang/maven/app-statistics/_latestVersion "Latest version"
[Wingbu]:https://github.com/Wingbu/UseTimeStatistic
[zp-zone]:https://github.com/zp-zone/TimeMirror

<!-- 图片链接 -->

[icon_download]:https://api.bintray.com/packages/veizhang/maven/app-statistics/images/download.svg
[All]:https://github.com/VeiZhang/AppStatistics/blob/master/imags/All.png
[App]:https://github.com/VeiZhang/AppStatistics/blob/master/imags/App.png
[Activity]:https://github.com/VeiZhang/AppStatistics/blob/master/imags/Activity.png

<!-- 版本 -->

[1.0.1]:https://bintray.com/veizhang/maven/app-statistics/1.0.1
[1.0.0]:https://bintray.com/veizhang/maven/app-statistics/1.0.0
