Esse é um projeto em java para a construção de uma carteira digital, contendo um cliente, uma conta e Transação.

As exceptions são unchecked porque sua função é apenas informar ao usuário que determinada operação não pode ser executada. Dessa forma, a decisão sobre o próximo passo fica a cargo de quem consumir a exceção.

A transferência é garantida porque, primeiro, são realizados testes para verificar se a conta que irá receber não é a mesma que está enviando e se ela existe. Após isso, o valor é debitado da conta de origem, garantindo que haja saldo suficiente; somente então o saldo da conta de destino é atualizado.

A regra de saldo foi mantida na própria conta para que a responsabilidade de gerenciá-lo permaneça nela. Dessa forma, independentemente de a conta ser utilizada em um serviço ou em uma transação, ela terá controle sobre o próprio saldo.