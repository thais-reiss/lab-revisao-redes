# Respostas

## Parte A

**1. O que acontece se você iniciar o cliente antes do servidor? Por que isso ocorre, considerando o funcionamento do TCP?**

Ao tentar fazer isso no servidor na linguagem Java é lançada uma exceção do tipo ConnectException: Connection refused, e no servidor em Python é lançado um erro chamado ConnectionRefusedError. Isso ocorre porque no TCP existe o chamado handshake, que é quando o servidor e o cliente estabelecem uma conexão antes de começarem a trocar mensagens entre si. Para isso, o servidor precisa estar esperando conexões em uma determinada porta antes do cliente tentar estabelecer uma comunicação. 

**2. O TCP garante que as mensagens cheguem na ordem em que foram enviadas. Qual mecanismo do protocolo é responsável por isso?**

É o mecanismo de números de sequência. Ao enviar dados usando o TCP, eles são divididos em segmentos, e cada um recebe um número de sequência que indica sua posição no conjunto de dados enviados. Dessa forma, o TCP consegue reorganizar os segmentos na ordem correta, garantindo que os dados sejam entregues à aplicação na mesma ordem em que foram enviados.

**3. Na sua implementação, o que aconteceria se dois clientes tentassem se conectar ao mesmo tempo? O código atual suporta isso? Justifique observando o código do servidor.**

O que aconteceu foi que o servidor aceitou a conexão do primeiro cliente e, posteriormente, o segundo também conseguiu se conectar. Porém o servidor só processou e respondeu as solicitações do primeiro cliente, deixando o segundo sem resposta. E, ao final, quando o primeiro cliente encerrou a conexão, a conexão do segundo foi descartada. De acordo com o código do servidor, ele só aceita um cliente por execução, pois não existe um loop que faça com que ele aceite novas conexões continuamente. Além disso, cada cliente deveria ser tratado em uma thread separada, para o atendimento de um não atrapalhar o dos demais, e isso também não é feito na minha implementação.

