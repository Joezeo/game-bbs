package com.joezeo.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.bean.DownloadStreamBean;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Component
public class UCloudProvider {
    @Autowired
    private ObjectAuthorization objectAuthorization;

    @Autowired
    private ObjectConfig objectConfig;

    @Value("${ucloud.ufile.bucketname}")
    private String bucketname;

    public String uploadImg(InputStream fileInputStream, String mimeType, String fileName) {
        String[] split = fileName.split("\\.");
        if (split.length <= 1) {
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
        String generatedNmae = UUID.randomUUID().toString() + "." + split[split.length - 1];

        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, objectConfig)
                    .putObject(fileInputStream, mimeType)
                    .nameAs(generatedNmae)
                    .toBucket(bucketname)
                    .execute();

            String url = UfileClient.object(objectAuthorization, objectConfig)
                    .getDownloadUrlFromPublicBucket(generatedNmae, bucketname)
                    .createUrl();
            return url;
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
    }

    public String uploadAvatar(InputStream inputStream, String mimetype, String filename) {
        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, objectConfig)
                    .putObject(inputStream, mimetype)
                    .nameAs(filename)
                    .toBucket(bucketname)
                    .execute();

            String url = UfileClient.object(objectAuthorization, objectConfig)
                    .getDownloadUrlFromPublicBucket(filename, bucketname)
                    .createUrl();
            return url;
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
    }

    public String uploadTopic(String description, String mimetype) {
        String filename = UUID.randomUUID().toString() + ".txt";
        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, objectConfig)
                    .putObject(new ByteArrayInputStream(description.getBytes()), mimetype)
                    .nameAs(filename)
                    .toBucket(bucketname)
                    .execute();

            String url = UfileClient.object(objectAuthorization, objectConfig)
                    .getDownloadUrlFromPublicBucket(filename, bucketname)
                    .createUrl();
            return url;
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
    }

    /**
     * 从UCloud获取帖子
     *
     * @param url
     * @return InputStream
     */
    public InputStream downloadTopic(String url) {
        try {
            DownloadStreamBean response = UfileClient.object(objectAuthorization, objectConfig)
                    .getStream(url)
                    .execute();
            return response.getInputStream();
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        } catch (UfileClientException e1) {
            e1.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
    }
}
