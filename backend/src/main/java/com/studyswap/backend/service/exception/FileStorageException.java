package com.studyswap.backend.service.exception;

/**
 * Exceção lançada quando ocorre algum erro durante o armazenamento de arquivos.
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
