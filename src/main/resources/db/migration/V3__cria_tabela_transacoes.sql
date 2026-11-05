CREATE TABLE transacoes (
        id UUID PRIMARY KEY,
        tipo VARCHAR(30) NOT NULL,
        valor NUMERIC(19, 2) NOT NULL,
        data_hora TIMESTAMP NOT NULL,

        conta_origem_numero INTEGER NOT NULL,
        conta_destino_numero INTEGER,

        CONSTRAINT fk_transacao_conta_origem
            FOREIGN KEY (conta_origem_numero)
                REFERENCES contas(numero),

        CONSTRAINT fk_transacao_conta_destino
            FOREIGN KEY (conta_destino_numero)
                REFERENCES contas(numero)
);
