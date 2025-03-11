package gettothepoint.unicatapi.application.service;

import gettothepoint.unicatapi.domain.dto.StorageUpload;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    StorageUpload uploadFile(MultipartFile file);
}
