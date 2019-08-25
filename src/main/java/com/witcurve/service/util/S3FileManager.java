package com.witcurve.service.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.witcurve.service.impl.AttachmentServiceImpl;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class S3FileManager {

    @Autowired
    TransferManager transferManager;

    @Autowired
    AmazonS3 amazonS3;

    private final Logger log  = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    public Map<String, String> uploadFromInputStream(String bucket, String fileName, MultipartFile stream, Map<String, String> attributes, boolean setMd5) throws WitcurveException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setUserMetadata(attributes);
            uploadFile(bucket, fileName, metadata, stream.getInputStream(), false);

        } catch (Exception e) {
            throw new WitcurveException("Error while uploading input String", e);
        }
        return attributes;
    }

    public Map<String, String> uploadFile(String bucket, String fileName, File file, Map<String, String> attributes, boolean setMd5) throws WitcurveException{
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setUserMetadata(attributes);
            uploadFile(bucket, fileName, metadata, file, false);

        } catch (Exception e) {
            log.info("\n \n Upload error : {} \n \n", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return attributes;
    }

    public void uploadFiles(String bucketName, String destinationDirectory, String directoryName, List<File> files, boolean asynchronous) throws WitcurveException {
        MultipleFileUpload upload = transferManager.uploadFileList(bucketName, destinationDirectory, new File(directoryName), files);
        try {
            if (!asynchronous) {
                upload.waitForCompletion();
            } else {
                upload.addProgressListener(new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressEvent progressEvent) {
                        if (progressEvent.getEventType() == ProgressEventType.CLIENT_REQUEST_FAILED_EVENT) {
                            try {
                                AmazonClientException e = upload.waitForException();
                            } catch (InterruptedException e) {
                                throw new WitcurveException("There was Error while uploading file", e);
                            }
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            throw new WitcurveException("There was Error while uploading file", e);
        }
    }

    public boolean isValidFile(String bucketName, String fileName) throws WitcurveException {
        try {
            amazonS3.getObjectMetadata(bucketName, fileName);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new WitcurveException("No such file is found", e);
            } else {
                throw new WitcurveException("There was problem while reading the file", e);
            }
        }
        return true;
    }

    public void copyFile(String sourceBucket, String sourceObjectKey, String destBucket, String destObjectKey) throws WitcurveException{
        try {
            amazonS3.copyObject(sourceBucket, sourceObjectKey, destBucket, destObjectKey);
        } catch (AmazonServiceException e) {
            throw new WitcurveException("Error while copying file", e);
        }
    }

    public Map<String, String> getObjectMetaData(String bucket, String key) {
        ObjectMetadata metaData = amazonS3.getObjectMetadata(bucket, key);
        return metaData.getUserMetadata();
    }

    public String uploadDirectory(String bucket, String directoryName, File file) throws WitcurveException {
        uploadDirectory(bucket, directoryName, file, true);
        return directoryName;
    }

    private void uploadDirectory(String bucketName, String fileName, File file,
                                 boolean asynchronous) throws WitcurveException {

        MultipleFileUpload upload = transferManager.uploadDirectory(bucketName, fileName, file, true);
        try {
            if (!asynchronous) {
                upload.waitForCompletion();
            } else {
                upload.addProgressListener(new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressEvent progressEvent) {
                        if (progressEvent.getEventType() == ProgressEventType.CLIENT_REQUEST_FAILED_EVENT) {
                            try {
                                AmazonClientException e = upload.waitForException();
                            } catch (InterruptedException e) {
                                throw new WitcurveException("There was Error while uploading file", e);
                            }
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            throw new WitcurveException("There was Error while uploading file", e);
        }
    }

    public void deleteFile(String bucketName, String fileName) throws WitcurveException {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            throw new WitcurveException("Error while deleting file", e);
        }
    }

    public S3Object getObject(String bucketName, String fileName) throws WitcurveException {
        try {
            return amazonS3.getObject(bucketName, fileName);
        } catch (AmazonServiceException e) {
            throw new WitcurveException("Error while download file", e);
        }
    }

    public void deleteObjectsInFolder(String bucketName, String folderPath) throws WitcurveException {
        try {
            for (S3ObjectSummary file : amazonS3.listObjects(bucketName, folderPath).getObjectSummaries()){
                amazonS3.deleteObject(bucketName, file.getKey());
            }
        } catch (RuntimeException e) {
            throw new WitcurveException("There was Error while deleting a folder", e);
        }
    }

    private void uploadFile(String bucketName, String fileName, ObjectMetadata objectMetaData, InputStream stream,
                            boolean asynchronous) throws WitcurveException{
        Upload upload = transferManager.upload(bucketName, fileName, stream, objectMetaData);
        try {
            if (!asynchronous) {
                upload.waitForUploadResult();
            } else {
                upload.addProgressListener(new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressEvent progressEvent) {
                        if (progressEvent.getEventType() == ProgressEventType.CLIENT_REQUEST_FAILED_EVENT) {
                            try {
                                AmazonClientException e = upload.waitForException();
                            } catch (InterruptedException e) {
                                throw new WitcurveException("There was Error while uploading file", e);
                            }
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            throw new WitcurveException("There was Error while uploading file", e);
        }
    }

    private void uploadFile(String bucketName, String fileName, ObjectMetadata objectMetaData, File file,
                            boolean asynchronous) throws WitcurveException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file).withMetadata(objectMetaData);
        Upload upload = transferManager.upload(putObjectRequest);
        if (!asynchronous) {
            try{
                upload.waitForUploadResult();
            }catch(InterruptedException ex ){
                throw new WitcurveException("There was Error while uploading file", ex);
            }

        } else {
            upload.addProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    if (progressEvent.getEventType() == ProgressEventType.CLIENT_REQUEST_FAILED_EVENT) {
                        try {
                            AmazonClientException e = upload.waitForException();
                            log.error(e.getMessage(), e);
                        } catch (InterruptedException e) {
                            throw new WitcurveException("There was Error while uploading file", e);
                        }
                    }
                }
            });
        }
    }

}
