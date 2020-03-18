package com.yaypay.challenge.services;

import java.util.List;

public interface DataService<IN> {
    void processData(List<IN> payload);
}
