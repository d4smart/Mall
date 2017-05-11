package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/11 13:12.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        // 获取文件拓展名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + ext;
        logger.info("开始上传文件，原文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);

        File dirs = new File(path);
        if(!dirs.exists()) {
            dirs.setWritable(true);
            dirs.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile); // 文件上传成功

            // 将文件上传到ftp服务器上，上传完之后删除upload下的文件
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            logger.info("上传文件异常", e);
            // e.printStackTrace();
            return null;
        }

        return targetFile.getName();
    }
}
