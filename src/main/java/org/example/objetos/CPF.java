package org.example.objetos;

public record CPF(String valor) {
    public CPF{
        if(valor == null){
            throw new IllegalArgumentException("CPF não pode ser null");
        }
        if(!valido(valor)){
            throw new IllegalArgumentException("CPF não é valido");
        }
    }
    private static boolean valido(String cpf){
        return cpf.length() == 11;
    }
}
