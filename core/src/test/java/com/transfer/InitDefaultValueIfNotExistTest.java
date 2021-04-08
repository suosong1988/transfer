package com.transfer;

import com.transfer.converter.InitDefaultValueIfNotExist;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * suosong
 * 2021/4/3
 */
@Component
public class InitDefaultValueIfNotExistTest implements InitDefaultValueIfNotExist {
    @Override
    public String init(Map<String, Object> sourceFields) {
        return "马六 InitDefaultValueIfNotExistTest";
    }
}
