package ru.synthiq.service;

import ru.synthiq.entity.AppDocument;
import ru.synthiq.entity.AppPhoto;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
}
