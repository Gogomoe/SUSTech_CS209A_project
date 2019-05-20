### JxBrowser 配置

本项目使用 JxBrowser 作为内置浏览器，请从官网 [下载6.22.1](https://storage.googleapis.com/cloud.teamdev.com/downloads/jxbrowser/6.22.1/jxbrowser-6.22.1-cross-desktop-win_mac_linux.zip)
版本的 JxBrowser ，并将对应平台的 jar 包放入项目根目录的 lib 文件夹下。

Java9及以后版本需要添加以下虚拟机参数

```shell
--add-exports javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED 
--add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED 
--add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED
```

### HanLP 配置

本项目使用 HanLP 进行词法分析，[下载data.zip](http://nlp.hankcs.com/download.php?file=data)
，并解压到项目根目录的 data 文件夹下。

可以参考 [Hanlp官网](https://github.com/hankcs/HanLP#%E6%96%B9%E5%BC%8F%E4%BA%8C%E4%B8%8B%E8%BD%BDjardatahanlpproperties)
进行配置。