# 前言

- 本项目为前后端分离项目，本仓库为后端地址，前端仓库请访问 [SoftwareCup(gitee.com)](https://gitee.com/zheng-cangping/software-cup)
- 本项目已部署至线上，请访问 [WCFun遥感解译平台](http://csc.para-dox.top)

# 后端使用说明

- 本项目需要同时运行Java后端与Python后端，其中Java运行在8088端口，Python运行在8082端口
- 运行Java：运行DemoApplication.java
- 运行Python：运行python/main.py文件，注意需要安装好PaddleRS所需要的环境
- 注：main.py文件启动时间较长，请耐心等待，第一次预测所需时间较长，后续较快
- 注：需要将四个导出的静态模型存放在model文件夹下，并分别命名为changeDetection,objectDetection,objectExtraction,terrainClassification