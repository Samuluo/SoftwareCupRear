package com.example.demo.demo;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CodeGenerator {

    /**
     * Project package
     */
    private static String projectPackage;

    /**
     * controller package
     */
    private static String controllerPackage;

    /**
     * entity package
     */
    private static String entityPackage;

    /**
     * author
     */
    private static String author;

    /**
     * Database url
     */
    private static String url;
    /**
     * Database username
     */
    private static String username;
    /**
     * Database password
     */
    private static String password;
    /**
     * Database driver class
     */
    private static String driverClass;

    /**
     * 文件名后缀
     */
    private static String fileSuffix = ".java";

    /**
     * Init database information
     */
    static {
        Properties properties = new Properties();
        InputStream i = CodeGenerator.class.getResourceAsStream("/mybatis-plus.properties");
        try {
            properties.load(i);
            projectPackage = properties.getProperty("generator.parent.package");
            controllerPackage = properties.getProperty("controller.package");
            entityPackage = properties.getProperty("entity.package");
            url = properties.getProperty("generator.jdbc.url");
            username = properties.getProperty("generator.jdbc.username");
            password = properties.getProperty("generator.jdbc.password");
            driverClass = properties.getProperty("generator.jdbc.driverClass");
            author = properties.getProperty("author");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * main method, execute code generator
     */
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");

        String javaPath = projectPackage.replaceAll("\\.", "/");
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java"); // 输出文件目录
        gc.setFileOverride(true); // 是否覆盖已有文件
        gc.setOpen(false); // 是否打开输出目录
        gc.setAuthor(author);
        gc.setSwagger2(true);  // 实体属性 Swagger2 注解
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setBaseResultMap(true); // mapper.xml中生成BaseResultMap
        gc.setActiveRecord(true);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setUsername(username);
        dsc.setPassword(password);
        dsc.setDriverName(driverClass);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(projectPackage);
        pc.setController(controllerPackage);
        pc.setEntity(entityPackage);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };


        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {

            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }

        });


        // 如果模板引擎是 freemarker
        String entityPath = "/templates/entity.java.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(entityPath) {

            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + javaPath + "/entity/" + tableInfo.getEntityName() + fileSuffix;
            }

        });

        // 如果模板引擎是 freemarker
        String controllerPath = "/templates/controller.java.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(controllerPath) {

            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + javaPath + "/web/controller/" + tableInfo.getEntityName() + "Controller" + fileSuffix;
            }

        });


        cfg.setFileOutConfigList(focList);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setXml(null);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass("com.example.demo.common.BaseController");
        strategy.setEntityLombokModel(true);//启用lombok注解
        strategy.setChainModel(true);//启用lombok链式注解
        strategy.setInclude("record");
       //strategy.setTablePrefix("caps_");//去表前缀配置

        mpg.setGlobalConfig(gc);
        mpg.setDataSource(dsc);
        mpg.setPackageInfo(pc);
        mpg.setCfg(cfg);
        mpg.setTemplate(templateConfig);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }
}
