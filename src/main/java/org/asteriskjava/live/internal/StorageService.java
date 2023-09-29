package org.asteriskjava.live.internal;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageService {
    static StorageService instance = new StorageService();
    private static final Logger LOGGER = LogManager.getLogger();
    private final String GCS_BUCKET = "textback_speech_storage";
    private StorageOptions storage;

    private StorageService() {
        try {
            CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(
                ServiceAccountCredentials.fromStream(getClass().getResourceAsStream("/rocket-storage.json")));
            storage = StorageOptions.newBuilder().setProjectId(Constants.GCP_PROJECT_ID)
                .setCredentials(credentialsProvider.getCredentials()).build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static StorageService getInstance() {
        return instance;
    }

    public void uploadFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            BlobId blobId = BlobId.of(GCS_BUCKET, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            try (WriteChannel writer = storage.getService().writer(blobInfo)) {
                writer.write(ByteBuffer.wrap(Files.readAllBytes(path)));
            }
            Files.delete(path);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
