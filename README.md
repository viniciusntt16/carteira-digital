# Carteira Digital

Projeto de uma carteira digital desenvolvido em Java. A aplicação permite cadastrar clientes e contas, realizar depósitos, saques e transferências, além de gerar relatórios sobre as movimentações e os saldos das contas.

## Como executar os testes

Com o Maven instalado e estando na pasta raiz do projeto, execute:

```bash
mvn test
```text

Esse comando compila o projeto e executa todos os testes automatizados.

## Estrutura de pacotes

### `entidades`

Contém as classes que representam as entidades principais do domínio da carteira digital.

- `Cliente`: armazena as informações pessoais do cliente, como identificador, nome e CPF.
- `Conta`: representa uma conta bancária e armazena informações como número, cliente proprietário e saldo. Também contém validações relacionadas ao saldo, como as regras para saque e transferência.
- `Transacao`: representa uma movimentação realizada na carteira, como depósito, saque ou transferência. Armazena informações como tipo, valor, data/hora, conta de origem e conta de destino.

### `exceptions`

Contém as exceções da aplicação, utilizadas para indicar situações em que uma operação não pode ser realizada, como conta inexistente ou saldo insuficiente.

### `objetos`

Contém objetos de valor e enums utilizados no projeto.

- `CPF`: objeto de valor criado como `record`, responsável por representar o CPF do cliente.
- `TIPO`: enum que representa os tipos possíveis de transação: `DEPOSITO`, `SAQUE` e `TRANSFERENCIA`.

### `repositorios`

Contém as interfaces dos repositórios e suas implementações em memória.

- `ClienteRepository` e `ClienteEmMemoria`: responsáveis por armazenar e buscar clientes.
- `ContaRepository` e `ContaEmMemoria`: responsáveis por armazenar e buscar contas.
- `TransacaoRepository` e `TransacaoEmMemoria`: responsáveis por armazenar e listar transações.

As implementações em memória permitem testar o sistema sem a necessidade de um banco de dados.

### `servicos`

Contém os serviços que implementam as regras de negócio da aplicação.

- `ContaService`: realiza operações como depósito, saque e transferência entre contas.
- `RelatorioService`: gera relatórios usando Streams, como extrato de uma conta, total por tipo de transação, cliente com maior saldo, contas sem movimentação e saldo total por cliente.

### `test`

Contém os testes automatizados do projeto. Os testes verificam os cenários com dados cadastrados e também os cenários com repositórios vazios.

## Decisões tomadas

- Foi utilizado `BigDecimal` para representar valores monetários, evitando problemas de precisão que poderiam ocorrer com tipos como `double` e `float`.
- Os repositórios foram implementados em memória para manter o projeto simples e permitir testes sem dependência de banco de dados.
- As buscas por conta retornam `Optional`, pois uma conta com determinado número pode não existir.
- As exceções de regras de negócio foram implementadas como exceções unchecked.
- O `RelatorioService` utiliza Streams para filtrar, ordenar, agrupar e somar dados, sem utilizar laços tradicionais.
- O CPF foi representado como um objeto de valor, pois dois clientes com o mesmo CPF devem ser identificados pelo mesmo valor de CPF.

## O que eu faria diferente

- Criaria uma interface para controlar data e hora, como `Clock`, para facilitar testes que dependem do horário das transações.
- Persistiria os dados em um banco de dados em vez de utilizar apenas repositórios em memória.

## Perguntas da Fase 0

### Por que o CPF precisa de `equals` e `hashCode` baseados no valor, e o que o `record` fez por você nisso?

O CPF precisa de `equals` e `hashCode` baseados no valor porque dois objetos `CPF` que possuem a mesma sequência de números devem ser considerados iguais, mesmo que tenham sido criados em momentos diferentes.

Isso é importante, por exemplo, para impedir que dois clientes sejam cadastrados com o mesmo CPF e para que objetos possam ser usados corretamente em coleções como `Set` e `Map`.

Como `CPF` foi criado usando `record`, o Java gera automaticamente os métodos `equals`, `hashCode` e `toString`. O `equals` compara os valores dos campos do record, e o `hashCode` é calculado com base nesses mesmos valores. Portanto, dois CPFs com o mesmo valor terão `equals` retornando `true` e terão o mesmo `hashCode`.

### Suas exceções são checked ou unchecked? O que muda para quem chama o serviço?

As exceções do projeto são unchecked, ou seja, herdam de `RuntimeException`, porque sua função é apenas informar ao usuário que determinada operação não pode ser executada. Dessa forma, a decisão sobre o próximo passo fica a cargo de quem consumir a exceção.

### Por que o `buscarPorId` retorna `Optional` e não `null`? Em que situação usar `Optional` seria abuso?

`buscarPorId` retorna `Optional` porque uma conta com o número informado pode não existir. Em vez de retornar `null` e deixar o risco de `NullPointerException` para quem chamou o método, o `Optional` deixa a ausência do resultado explícita e obriga o código a tratar esse cenário.

Por exemplo, é possível lançar uma exceção caso a conta não exista:

```java
Conta conta = contaRepository.buscarPorId(numeroConta)
        .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));
```text

Também é possível verificar se a conta existe antes de realizar alguma operação.

O uso de `Optional` seria abuso quando não existe possibilidade real de ausência, como em um método `getSaldo()` de uma conta que sempre possui saldo. Também seria desnecessário retornar `Optional<List<Conta>>`, pois uma lista vazia já representa naturalmente a ausência de resultados.
A transferência é garantida porque, primeiro, são realizados testes para verificar se a conta que irá receber não é a mesma que está enviando e se ela existe. Após isso, o valor é debitado da conta de origem, garantindo que haja saldo suficiente; somente então o saldo da conta de destino é atualizado.

A regra de saldo foi mantida na própria conta para que a responsabilidade de gerenciá-lo permaneça nela. Dessa forma, independentemente de a conta ser utilizada em um serviço ou em uma transação, ela terá controle sobre o próprio saldo.

Para manter o padrão anterior, mantive na própria conta a validação de que o valor deve ser maior que zero, para que ela tenha controle sobre o próprio saldo.

Escolhi manter o método extrato retornando a exceção ContaNaoEncontrada, para informar ao usuário que a conta procurada não foi encontrada na base de dados.