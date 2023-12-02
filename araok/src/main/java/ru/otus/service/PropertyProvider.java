package ru.otus.service;


import ru.otus.property.RequestProperties;

import java.util.List;

public interface PropertyProvider {
    long getCountContentDownloads();

    List<RequestProperties> getRequests();
}
