package com.suke.czx.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import com.suke.czx.common.exception.RRException;
import com.suke.czx.modules.gen.entity.ColumnEntity;
import com.suke.czx.modules.gen.entity.InfoRmationSchema;
import com.suke.czx.modules.gen.entity.MakerConfigEntity;
import com.suke.czx.modules.gen.entity.TableEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * 代码生成器   工具类
 *
 * @author czx
 * @email object_czx@163.com
 */
@Slf4j
@Component
public class GenUtils {

    public List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        // curd
        templates.add("template/Controller.java.vm");
        templates.add("template/Entity.java.vm");
        templates.add("template/Mapper.java.vm");
        templates.add("template/Mapper.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        // sql
        templates.add("template/menu.sql.vm");
        // vue
        templates.add("template/index.vue.vm");
        templates.add("template/index.js.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public void maker(MakerConfigEntity makerConfig, InfoRmationSchema table, List<ColumnEntity> columns) {
        //配置信息
        Properties config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.getTableName());
        tableEntity.setComments(table.getTableComment());
        //表名转换成Java类名
        String className = columnToJava(tableEntity.getTableName());
        tableEntity.setClassName(className);
        tableEntity.setClassNameMin(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (ColumnEntity column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.getColumnName());
            columnEntity.setDataType(column.getDataType());
            columnEntity.setColumnComment(column.getColumnComment());
            columnEntity.setExtra(column.getExtra());
            columnEntity.setColumnKey(column.getColumnKey());

            //列名转换成Java属性名
            String attrName = columnEntity.getColumnName();
            if (makerConfig.isUseHump()) {
                attrName = columnToJava(columnEntity.getColumnName());
            }
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrNameMin(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getProperty(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassNameMin());
        map.put("pathName", tableEntity.getClassNameMin().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("package", makerConfig.getPackagePath());
        map.put("moduleName", makerConfig.getModuleName());
        map.put("ModuleName", columnToJava(makerConfig.getModuleName()));
        map.put("requestMapping", makerConfig.getRequestMapping());
        map.put("author", makerConfig.getAuthor());
        map.put("useRestful", makerConfig.isUseRestful());
        map.put("email", makerConfig.getEmail());
        map.put("datetime", DateUtil.now());
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            String path = getFileName(makerConfig, template, tableEntity.getClassName(), makerConfig.getPackagePath(), makerConfig.getModuleName());
            if (StrUtil.isNotEmpty(path)) {
                String filePath = "";
                if(template.contains("menu.sql.vm")){
                    filePath = makerConfig.getFilePath();
                }else if(template.contains("index.js.vm")){
                    filePath = makerConfig.getUiFilePath();
                }else if(template.contains("index.vue.vm")){
                    filePath = makerConfig.getUiFilePath();
                }else {
                    filePath = makerConfig.getFilePath();
                }
                FileWriter writer = new FileWriter(filePath + path);
                writer.write(sw.toString());
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }


    /**
     * 获取配置信息
     */
    public Properties getConfig() {
        try {
            Resource resource = new ClassPathResource("generator.properties");
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            log.error("获取配置文件失败:{}",e.getMessage());
            throw new RRException("获取配置文件失败");
        }
    }

    /**
     * 获取文件名
     */
    public String getFileName(MakerConfigEntity makerConfig, String template, String className, String packageName, String moduleName) {
        String packagePath = File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        // sql
        if (makerConfig.isFileEntity() && template.contains("menu.sql.vm")) {
            return File.separator + "doc" + File.separator + className + ".sql";
        }

        // js
        if (makerConfig.isGenUi() && template.contains("index.js.vm")) {
            return File.separator + "src" + File.separator + "api" + File.separator + moduleName + File.separator + "index.js";
        }

        // vue
        if (makerConfig.isGenUi() && template.contains("index.vue.vm")) {
            return File.separator + "src" + File.separator + "views" + File.separator + moduleName + File.separator + "index.vue";
        }

        if (makerConfig.isFileEntity() && template.contains("Entity.java.vm")) {
            return packagePath + "entity" + File.separator + className + ".java";
        }

        if (makerConfig.isFileMapper() && template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (makerConfig.isFileServiceImpl() && template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (makerConfig.isFileServiceImpl() && template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (makerConfig.isFileController() && template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (makerConfig.isFileMapperXml() && template.contains("Mapper.xml.vm")) {
            return File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.xml";
        }
        return null;
    }
}