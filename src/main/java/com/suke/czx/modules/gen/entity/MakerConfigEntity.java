package com.suke.czx.modules.gen.entity;

import lombok.Data;

/**
 * @author czx
 * @title: MakerConfigEntity
 * @projectName zhjg
 * @description:
 * @date 2020/6/168:57
 */
@Data
public class MakerConfigEntity {

    private String tableName;
    private String mainPath;
    private String packagePath;
    private String moduleName;
    private String requestMapping;
    private boolean useRestful;
    private boolean useHump;
    private String filePath;
    private String uiFilePath;
    private String author;
    private String email;

    private boolean fileController;
    private boolean fileServiceImpl;
    private boolean fileMapper;
    private boolean fileMapperXml;
    private boolean fileEntity;
    private boolean genUi;

}
