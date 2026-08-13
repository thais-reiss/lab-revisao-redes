import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServidorTCP {
    public static void main(String[] args) throws IOException {
        int porta = 5022;
        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("[TCP] Servidor aguardando conexões na porta " + porta + "...");
            try (Socket cliente = servidor.accept();
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(cliente.getInputStream()));
                 PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true)) {

                System.out.println("[TCP] Cliente conectado: " + cliente.getRemoteSocketAddress());
                String mensagem;
                while ((mensagem = entrada.readLine()) != null) {
                    System.out.println("[TCP] Recebido: " + mensagem);
                    if(mensagem.equalsIgnoreCase("hora")) {
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String horaAtual = LocalTime.now().format(formato);
                        saida.println("Hora atual: " + horaAtual);
                    } else if (mensagem.equalsIgnoreCase("sair")) {
                        saida.println("Encerrando conexão. Até mais!");
                        break;
                    }else {
                        saida.println("Monitor responde: recebi sua mensagem -> \"" + mensagem + "\"");
                    }  
                }
            }
        }
        System.out.println("[TCP] Servidor encerrado.");
    }
}