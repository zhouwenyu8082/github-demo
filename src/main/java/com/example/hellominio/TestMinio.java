package com.example.hellominio;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TestMinio {
    public static void main(String[] args) {

        String endpoint = "http://192.168.19.131:9000";
        String accessKey = "minioadmin";
        String secretKey = "minioadmin";
        String bucketName = "hello-minio";

        //创建几乎全是构造器模式
        //builder()构造器endpoint(服务端地址) credential(用户名 密码)+build()+建立

        MinioClient minioClient = MinioClient.builder().endpoint(endpoint).
                credentials(accessKey, secretKey).build();


        //先判断桶是否存在
        //BucketExistsArgs.builder().bucket(bucketName).build() 判断构造器+判断桶的名字+建立
        try {
            boolean bucketExists=   minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            //如果桶不存在则创建
            if (!bucketExists){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                //权限json
                //设置hello-minio桶的访问权限
                String policy = """
                        {
                          "Statement" : [ {
                            "Action" : "s3:GetObject",
                            "Effect" : "Allow",
                            "Principal" : "*",
                            "Resource" : "arn:aws:s3:::hello-minio/*"
                          } ],
                          "Version" : "2012-10-17"
                        }""";

                //创建桶的访问权限  构造器+哪个桶的权限+权限json+建立
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
            }
            else {
                System.out.println("Bucket 'hello-minio' already exists.");

            }

            //上传文件
            //上传构造器+桶名+文件路径+文件命名+建立
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .filename("C:\\Users\\86178\\Pictures\\Saved Pictures\\wallhaven-8o23gk.jpg")
                    .object("wallhaven-578pl1.jpg").build());

            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
