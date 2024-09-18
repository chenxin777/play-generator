package com.chenxin.web.manager;

import cn.hutool.core.collection.CollUtil;
import com.chenxin.web.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cos 对象存储操作
 *
 * @author <a href="https://github.com/chenxin777">玩物志出品</a>
 *
 */
@Slf4j
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    private TransferManager transferManager;

    /**
     * @description bean架子啊
     * @author fangchenxin
     * @date 2024/8/15 10:16
     */
    @PostConstruct
    public void init() {
        log.info("CosManager 初始化成功");
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient, threadPool);
    }

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param localFilePath 本地文件路径
     * @return
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                new File(localFilePath));
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * @description 下载对象
     * @author fangchenxin
     * @date 2024/8/6 18:07
     * @param key 唯一键
     * @return com.qcloud.cos.model.COSObject
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }

    /**
     * @description 下载文件到本地
     * @author fangchenxin
     * @date 2024/8/15 10:13
     * @param key
     * @param localFilePath
     * @return com.qcloud.cos.transfer.Download
     */
    public Download download(String key, String localFilePath) throws InterruptedException {
        File downloadFile = new File(localFilePath);
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        Download download = transferManager.download(getObjectRequest, downloadFile);
        download.waitForCompletion();
        return download;
    }

    /**
     * @description 删除对象
     * @author fangchenxin
     * @date 2024/9/9 17:04
     * @param key
     */
    public void delObject(String key) {
        cosClient.deleteObject(cosClientConfig.getBucket(), key);
    }

    /**
     * @description 批量删除对象
     * @author fangchenxin
     * @date 2024/9/9 17:07
     * @param keyList
     * @return com.qcloud.cos.model.DeleteObjectsResult
     */
    public void delObjects(List<String> keyList) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket());
        List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for (String key : keyList) {
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(key));
        }
        deleteObjectsRequest.setKeys(keyVersions);
        DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
        deleteObjectsResult.getDeletedObjects().forEach(item -> System.out.println("已删除: " + item.getKey()));
    }

    /**
     * @description 删除目录下的对象
     * @author fangchenxin
     * @date 2024/9/9 20:18
     * @param delPrefix
     */
    public void delDir(String delPrefix) throws Exception {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(cosClientConfig.getBucket());
        listObjectsRequest.setPrefix(delPrefix);
        // 设置最大遍历出多少个对象
        listObjectsRequest.setMaxKeys(1000);
        // 保存每次列出的结果
        ObjectListing objectListing = null;
        do {
            objectListing = cosClient.listObjects(listObjectsRequest);
            List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            if (CollUtil.isEmpty(objectSummaries)) {
                break;
            }
            List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
            for (COSObjectSummary objectSummary : objectSummaries) {
                keyVersions.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
            }

            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket());
            deleteObjectsRequest.setKeys(keyVersions);
            DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deletedObjects = deleteObjectsResult.getDeletedObjects();
            deletedObjects.forEach(item -> System.out.println("已删除: " + item.getKey()));

            // 标记下一次开始位置
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());


    }

}
