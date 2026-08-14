# Respostas

## Parte A

**1. O que acontece se você iniciar o cliente antes do servidor? Por que isso ocorre, considerando o funcionamento do TCP?**

Ao tentar fazer isso no servidor na linguagem Java é lançada uma exceção do tipo ConnectException: Connection refused, e no servidor em Python é lançado um erro chamado ConnectionRefusedError. Isso ocorre porque no TCP existe o chamado handshake, que é quando o servidor e o cliente estabelecem uma conexão antes de começarem a trocar mensagens entre si. Para isso, o servidor precisa estar esperando conexões em uma determinada porta antes do cliente tentar estabelecer uma comunicação. 

**2. O TCP garante que as mensagens cheguem na ordem em que foram enviadas. Qual mecanismo do protocolo é responsável por isso?**

É o mecanismo de números de sequência. Ao enviar dados usando o TCP, eles são divididos em segmentos, e cada um recebe um número de sequência que indica sua posição no conjunto de dados enviados. Dessa forma, o TCP consegue reorganizar os segmentos na ordem correta, garantindo que os dados sejam entregues à aplicação na mesma ordem em que foram enviados.

**3. Na sua implementação, o que aconteceria se dois clientes tentassem se conectar ao mesmo tempo? O código atual suporta isso? Justifique observando o código do servidor.**

O que aconteceu foi que o servidor aceitou a conexão do primeiro cliente e, posteriormente, o segundo também conseguiu se conectar. Porém o servidor só processou e respondeu as solicitações do primeiro cliente, deixando o segundo sem resposta. E, ao final, quando o primeiro cliente encerrou a conexão, a conexão do segundo foi descartada. De acordo com o código do servidor, ele só aceita um cliente por execução, pois não existe um loop que faça com que ele aceite novas conexões continuamente. Além disso, cada cliente deveria ser tratado em uma thread separada, para o atendimento de um não atrapalhar o dos demais, e isso também não é feito na minha implementação.

## Parte B

**1. No passo 2 da tarefa, o que aconteceu quando você enviou uma mensagem com o servidor desligado? Compare com o que aconteceria em TCP e explique a diferença observada, relacionando com o conceito de "sem conexão".**

O cliente ficou travado esperando uma resposta, mas sem receber nenhuma. No TCP, após ter desligado o servidor e tentado enviar uma mensagem pelo cliente, foi lançada uma exceção chamada SocketException: Connection reset, que apareceu no terminal do cliente. Isso aconteceu porque no TCP, para poder enviar mensagens, o cliente tem que ter uma conexão estabelecida com o servidor. Assim, ao desligar o servidor a conexão é quebrada e o erro aparece para o cliente. Já no UDP, cliente e servidor não se conectam antes, sendo que o cliente apenas envia as mensagens, sem saber se o servidor está disponível ou não, ou seja, sem garantia de entrega. Por isso, o cliente envia a mensagem e fica sem resposta.

**2. Cite dois exemplos de aplicações reais que usam UDP e explique, para cada uma, por que a confiabilidade do TCP não é essencial (ou até atrapalharia).**

Como primeiro exemplo vou citar chamadas de vídeo, como as feitas pelo Google Meet. Essas chamadas usam UDP para transmitir áudio e vídeo em tempo real, porque nesse caso os dados precisam chegar rapidamente. Dessa forma, usar o TCP atrapalharia, porque todo o processo que ele faz de confirmação e reordenação dos dados consome tempo. Assim, o vídeo avançaria vários quadros enquanto ele estivesse realizando esse processo, causando um atraso significativo na chamada de vídeo, que tecnicamente deve ser ao vivo.
Já para o segundo exemplo, vou citar os jogos online multiplayer em tempo real, como o Call of Duty. Esses jogos precisam enviar constamente a posição dos jogadores, objetos e as ações que acontecem no jogo o mais rápido possível para que todos possam ver o jogo acontecendo praticamente ao mesmo tempo. Nesse caso, usar o TCP não faria sentido, pois se um pacote com a posição de um jogador se perder, ele iria parar tudo para reenviar aquela posição antiga, mas no momento em que ela chegasse, o jogador já teria se movido várias vezes, tornando aquele dado desatualizado. Assim, é melhor simplesmente ignorar aquele pacote perdido e seguir usando as atualizações de posição mais recentes que continuam chegando.

**3. No código, o servidor UDP não mantém nenhum registro de "quem está conectado". Isso seria possível de implementar? O que mudaria na arquitetura da aplicação?**

Sim, mas isso seria implementado na lógica da aplicação, sem alterar o protocolo usado. Para isso, o servidor teria que manter uma estrutura de dados que armazenasse os clientes que já enviaram mensagens, por exemplo, associando o endereço IP e a porta de cada cliente a informações sobre ele. A cada datagrama recebido, o servidor verificaria se aquele cliente já está registrado e, caso não estivesse, poderia adicioná-lo à lista. O que mudaria na arquitetura da aplicação é que o servidor passaria a gerenciar o estado dos clientes. Também seria necessário definir mecanismos para controlar clientes inativos, como um tempo de expiração, já que o UDP não informa automaticamente quando um cliente deixou de participar da comunicação.

## Parte C

**1. Qual é a diferença fundamental entre enviar a mesma mensagem para 3 clientes usando unicast repetido 3 vezes e enviar uma única vez via multicast? Pense em termos de tráfego de rede.**

Ao enviar em unicast repetido, o servidor cria e envia 3 cópias separadas e indênticas da mensagem, cada uma percorrendo a rede até seu destino. Assim, são realizadas 3 transmissões, uma para cada destinatário. Pensando em termos de tráfego de rede, isso gera 3x o tráfego da mensagem, e esse número cresceria ainda mais conforme fosse aumentando o número de clientes. Usando o multicast, o tráfego de rede é reduzido, porque o servidor faz uma única transmissão para todos os destinatários. Os roteadores pela rede criam cópias somente nos pontos em que o caminho para diferentes clientes se separa. 

**2. O que é o TTL (time-to-live) configurado no socket multicast e por que ele é importante para controlar o alcance dos pacotes na rede?**

O TTL é um contador de saltos, sendo que salto é cada vez que o pacote passa por um roteador no caminho até o destino. Ele serve para limitar a quantidade de roteadores que um pacote multicast pode atravessar antes de ser descartado pela rede. No código ele é igual a 2, o que significa que o pacote pode passar por no máximo 2 roteadores, se ele chegar a um terceiro ele deve ser descartado. Ele é importante para evitar que um pacote seja espalhado indefinidamente por toda a internet, mantendo o tráfego mais restrito. 

**3. Se um dos clientes ficar temporariamente offline e voltar depois, ele recebe os avisos que perdeu? Por quê? Relacione com a arquitetura de comunicação em grupo.**

Não, apenas os clientes que se inscreveram no grupo e estão ativos no momento do envio receberão os avisos. Isso acontece porque o multicast baseado em UDP não possui, por padrão, um mecanismo de armazenamento e retransmissão das mensagens para clientes que estavam desconectados. As mensagens são enviadas para um endereço IP de grupo, sem estabelecer uma conexão individual ou garantir que cada cliente recebeu o pacote.

