CREATE TABLE contas (
        numero INTEGER PRIMARY KEY,
        cliente_id UUID NOT NULL,
        saldo NUMERIC(19, 2) NOT NULL,

        CONSTRAINT fk_contas_cliente
            FOREIGN KEY (cliente_id)
                REFERENCES clientes(id)
);
