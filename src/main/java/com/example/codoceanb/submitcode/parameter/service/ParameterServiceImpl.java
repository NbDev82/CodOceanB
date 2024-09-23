package com.example.codoceanb.submitcode.parameter.service;

import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParameterServiceImpl implements ParameterService{

    @Override
    public StringBuilder createListParameter(List<Parameter> listParameters) {
        List<String> parameters = new ArrayList<>();
        StringBuilder listParameter = new StringBuilder();

        for(Parameter parameter: listParameters){
            String p = parameter.getInputDataType() + " " +
                    parameter.getName()+ ", ";
            parameters.add(p);
        }

        for(String parameter:parameters){
            listParameter.append(parameter);
        }

        return new StringBuilder(listParameter
                .substring(0, listParameter.length() - 2));
    }
}
