package org.example.conversores;

import jakarta.persistence.Converter;
import jakarta.persistence.AttributeConverter;
import org.example.objetos.CPF;

@Converter
public class CPFConverter implements AttributeConverter<CPF, String> {

    @Override
    public String convertToDatabaseColumn(CPF cpf) {
        if(cpf == null) {
            return null;
        }
        return cpf.valor();
    }

    @Override
    public CPF convertToEntityAttribute(String s) {
        if(s == null) {
            return null;
        }
        return new CPF(s);
    }
}
