package com.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author d4smart
 * @email d4smart@foxmail.com
 * Created on 2017/5/11 13:10.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
