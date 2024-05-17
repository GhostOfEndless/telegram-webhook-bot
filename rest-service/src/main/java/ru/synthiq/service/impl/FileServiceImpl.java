package ru.synthiq.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.synthiq.dao.AppDocumentDAO;
import ru.synthiq.dao.AppPhotoDAO;
import ru.synthiq.entity.AppDocument;
import ru.synthiq.entity.AppPhoto;
import ru.synthiq.entity.BinaryContent;
import ru.synthiq.service.FileService;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;

    @Override
    public AppDocument getDocument(String documentId) {
        // TODO добавить дешифрование хеш-строки
        var id = Long.parseLong(documentId);
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        // TODO добавить дешифрование хеш-строки
        var id = Long.parseLong(photoId);
        return appPhotoDAO.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            // TODO добавить генерацию имени временного файла
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }
}