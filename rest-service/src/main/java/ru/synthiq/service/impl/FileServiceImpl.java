package ru.synthiq.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.synthiq.dao.AppDocumentDAO;
import ru.synthiq.dao.AppPhotoDAO;
import ru.synthiq.entity.AppDocument;
import ru.synthiq.entity.AppPhoto;
import ru.synthiq.service.FileService;
import ru.synthiq.utils.CryptoTool;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String documentId) {
        var id = cryptoTool.idOf(documentId);
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        var id = cryptoTool.idOf(photoId);
        return appPhotoDAO.findById(id).orElse(null);
    }
}
