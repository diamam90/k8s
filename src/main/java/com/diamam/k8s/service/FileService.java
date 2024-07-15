package com.diamam.k8s.service;

import java.io.IOException;

public interface FileService {

    void addToFile(String payload) throws IOException;

    String readFromFile() throws IOException;

    void clean() throws IOException;
}
